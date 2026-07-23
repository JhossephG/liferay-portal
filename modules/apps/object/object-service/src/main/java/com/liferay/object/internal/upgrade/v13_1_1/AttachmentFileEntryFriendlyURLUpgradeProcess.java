/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.upgrade.v13_1_1;

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.definition.util.ObjectDefinitionUtil;
import com.liferay.object.internal.field.attachment.AttachmentObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jhosseph Gonzalez
 */
public class AttachmentFileEntryFriendlyURLUpgradeProcess
	extends UpgradeProcess {

	public AttachmentFileEntryFriendlyURLUpgradeProcess(
		ClassNameLocalService classNameLocalService,
		CompanyLocalService companyLocalService,
		DLAppLocalService dlAppLocalService,
		FriendlyURLEntryLocalService friendlyURLEntryLocalService,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectEntryLocalService objectEntryLocalService,
		ObjectFieldLocalService objectFieldLocalService) {

		_classNameLocalService = classNameLocalService;
		_companyLocalService = companyLocalService;
		_dlAppLocalService = dlAppLocalService;
		_friendlyURLEntryLocalService = friendlyURLEntryLocalService;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectEntryLocalService = objectEntryLocalService;
		_objectFieldLocalService = objectFieldLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		_companyLocalService.forEachCompanyId(this::_upgradeCompany);
	}

	private List<ObjectField> _getAttachmentObjectFields(
		long objectDefinitionId) {

		return TransformUtil.transform(
			_objectFieldLocalService.getObjectFieldsByBusinessType(
				objectDefinitionId,
				ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT),
			objectField -> {
				if (AttachmentObjectFieldUtil.
						isUserComputerAttachmentObjectField(objectField)) {

					return objectField;
				}

				return null;
			});
	}

	private boolean _isInSync(
		FileEntry fileEntry, long fileEntryClassNameId,
		Map<String, String> urlTitleMap) {

		FriendlyURLEntry fileEntryFriendlyURLEntry =
			_friendlyURLEntryLocalService.fetchMainFriendlyURLEntry(
				fileEntryClassNameId, fileEntry.getFileEntryId());

		if (fileEntryFriendlyURLEntry == null) {
			return false;
		}

		Map<String, String> fileEntryURLTitleMap =
			fileEntryFriendlyURLEntry.getLanguageIdToUrlTitleMap();

		for (Map.Entry<String, String> entry : urlTitleMap.entrySet()) {
			if (!Objects.equals(
					fileEntryURLTitleMap.get(entry.getKey()),
					entry.getValue())) {

				return false;
			}
		}

		return true;
	}

	private boolean _syncFileEntry(
			String defaultLanguageId, long dlFileEntryId,
			long fileEntryClassNameId, Map<String, String> urlTitleMap)
		throws PortalException {

		if (dlFileEntryId <= 0) {
			return false;
		}

		FileEntry fileEntry = _dlAppLocalService.fetchFileEntry(dlFileEntryId);

		if ((fileEntry == null) ||
			_isInSync(fileEntry, fileEntryClassNameId, urlTitleMap)) {

			return false;
		}

		Map<String, String> fileEntryURLTitleMap = new HashMap<>();

		for (Map.Entry<String, String> entry : urlTitleMap.entrySet()) {
			fileEntryURLTitleMap.put(
				entry.getKey(),
				_friendlyURLEntryLocalService.getUniqueUrlTitle(
					fileEntry.getGroupId(), fileEntryClassNameId,
					fileEntry.getFileEntryId(), entry.getValue(),
					entry.getKey()));
		}

		_friendlyURLEntryLocalService.addFriendlyURLEntry(
			fileEntry.getGroupId(), fileEntryClassNameId,
			fileEntry.getFileEntryId(), defaultLanguageId, fileEntryURLTitleMap,
			new ServiceContext());

		return true;
	}

	private void _upgradeCompany(long companyId) throws PortalException {
		long fileEntryClassNameId = _classNameLocalService.getClassNameId(
			FileEntry.class);

		for (ObjectDefinition objectDefinition :
				_objectDefinitionLocalService.getObjectDefinitions(
					companyId, WorkflowConstants.STATUS_APPROVED)) {

			if (!Objects.equals(
					objectDefinition.getExternalReferenceCode(),
					"L_CMS_BASIC_DOCUMENT") ||
				!objectDefinition.isEnableFriendlyURLCustomization() ||
				ObjectDefinitionUtil.isDefaultFriendlyURLSeparator(
					objectDefinition.getFriendlyURLSeparator())) {

				continue;
			}

			List<ObjectField> attachmentObjectFields =
				_getAttachmentObjectFields(
					objectDefinition.getObjectDefinitionId());

			if (attachmentObjectFields.isEmpty()) {
				continue;
			}

			_upgradeObjectDefinition(
				attachmentObjectFields, companyId, fileEntryClassNameId,
				objectDefinition);
		}
	}

	private void _upgradeObjectDefinition(
			List<ObjectField> attachmentObjectFields, long companyId,
			long fileEntryClassNameId, ObjectDefinition objectDefinition)
		throws PortalException {

		long objectEntryClassNameId = _classNameLocalService.getClassNameId(
			objectDefinition.getClassName());

		AtomicInteger syncedObjectEntriesCount = new AtomicInteger();

		ActionableDynamicQuery actionableDynamicQuery =
			_objectEntryLocalService.getActionableDynamicQuery();

		actionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> dynamicQuery.add(
				RestrictionsFactoryUtil.eq(
					"objectDefinitionId",
					objectDefinition.getObjectDefinitionId())));
		actionableDynamicQuery.setCompanyId(companyId);
		actionableDynamicQuery.setPerformActionMethod(
			(ObjectEntry objectEntry) -> {
				try {
					if (_upgradeObjectEntry(
							attachmentObjectFields, fileEntryClassNameId,
							objectEntry, objectEntryClassNameId)) {

						syncedObjectEntriesCount.incrementAndGet();
					}
				}
				catch (PortalException portalException) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Unable to sync the friendly URL of a file ",
								"entry attached to object entry ",
								objectEntry.getObjectEntryId(), " for company ",
								companyId),
							portalException);
					}
				}
			});

		actionableDynamicQuery.performActions();

		if (_log.isInfoEnabled() && (syncedObjectEntriesCount.get() > 0)) {
			_log.info(
				StringBundler.concat(
					"Synced the friendly URLs of file entries attached to ",
					syncedObjectEntriesCount.get(),
					" object entries of object definition ",
					objectDefinition.getObjectDefinitionId()));
		}
	}

	private boolean _upgradeObjectEntry(
			List<ObjectField> attachmentObjectFields, long fileEntryClassNameId,
			ObjectEntry objectEntry, long objectEntryClassNameId)
		throws PortalException {

		FriendlyURLEntry friendlyURLEntry =
			_friendlyURLEntryLocalService.fetchMainFriendlyURLEntry(
				objectEntryClassNameId, objectEntry.getObjectEntryId());

		if (friendlyURLEntry == null) {
			return false;
		}

		Map<String, String> urlTitleMap =
			friendlyURLEntry.getLanguageIdToUrlTitleMap();

		if (urlTitleMap.isEmpty()) {
			return false;
		}

		Map<String, Serializable> values = objectEntry.getValues();

		boolean synced = false;

		for (ObjectField objectField : attachmentObjectFields) {
			if (objectField.isLocalized()) {
				Map<String, Serializable> localizedValues =
					(Map<String, Serializable>)values.get(
						objectField.getI18nObjectFieldName());

				if (localizedValues == null) {
					continue;
				}

				for (Map.Entry<String, String> entry : urlTitleMap.entrySet()) {
					String languageId = entry.getKey();

					if (_syncFileEntry(
							languageId,
							GetterUtil.getLong(localizedValues.get(languageId)),
							fileEntryClassNameId,
							Collections.singletonMap(
								languageId, entry.getValue()))) {

						synced = true;
					}
				}
			}
			else if (_syncFileEntry(
						friendlyURLEntry.getDefaultLanguageId(),
						GetterUtil.getLong(values.get(objectField.getName())),
						fileEntryClassNameId, urlTitleMap)) {

				synced = true;
			}
		}

		return synced;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AttachmentFileEntryFriendlyURLUpgradeProcess.class);

	private final ClassNameLocalService _classNameLocalService;
	private final CompanyLocalService _companyLocalService;
	private final DLAppLocalService _dlAppLocalService;
	private final FriendlyURLEntryLocalService _friendlyURLEntryLocalService;
	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectFieldLocalService _objectFieldLocalService;

}