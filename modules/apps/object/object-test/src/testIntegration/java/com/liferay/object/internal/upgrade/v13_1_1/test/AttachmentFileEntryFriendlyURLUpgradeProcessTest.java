/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.upgrade.v13_1_1.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.test.util.DLTestUtil;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.field.builder.AttachmentObjectFieldBuilder;
import com.liferay.object.field.setting.builder.ObjectFieldSettingBuilder;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.related.models.test.util.ObjectEntryTestUtil;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.test.util.UpgradeTestUtil;

import java.io.Serializable;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jhosseph Gonzalez
 */
@RunWith(Arquillian.class)
public class AttachmentFileEntryFriendlyURLUpgradeProcessTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@After
	public void tearDown() throws Exception {
		if ((_fileEntryId > 0) &&
			(_dlAppLocalService.fetchFileEntry(_fileEntryId) != null)) {

			_dlAppLocalService.deleteFileEntry(_fileEntryId);
		}
	}

	@Test
	public void testUpgrade() throws Exception {
		_testUpgrade(false);
	}

	@Test
	public void testUpgradeWithLocalizedAttachmentObjectField()
		throws Exception {

		_testUpgrade(true);
	}

	private void _testUpgrade(boolean localized) throws Exception {
		String objectFieldName = "a" + RandomTestUtil.randomString();

		_objectDefinition = ObjectDefinitionTestUtil.publishObjectDefinition(
			ListUtil.fromArray(
				new AttachmentObjectFieldBuilder(
				).labelMap(
					RandomTestUtil.randomLocaleStringMap()
				).localized(
					localized
				).name(
					objectFieldName
				).objectFieldSettings(
					ListUtil.fromArray(
						new ObjectFieldSettingBuilder(
						).name(
							ObjectFieldSettingConstants.
								NAME_ACCEPTED_FILE_EXTENSIONS
						).value(
							"*"
						).build(),
						new ObjectFieldSettingBuilder(
						).name(
							ObjectFieldSettingConstants.NAME_FILE_SOURCE
						).value(
							ObjectFieldSettingConstants.
								VALUE_USER_COMPUTER_TO_CMS_BASIC_DOCUMENT
						).build(),
						new ObjectFieldSettingBuilder(
						).name(
							ObjectFieldSettingConstants.NAME_MAX_FILE_SIZE
						).value(
							"100"
						).build())
				).build()));

		_objectDefinition.setEnableFriendlyURLCustomization(true);
		_objectDefinition.setFriendlyURLSeparator(
			StringUtil.toLowerCase(RandomTestUtil.randomString()));

		_objectDefinition =
			_objectDefinitionLocalService.updateObjectDefinition(
				_objectDefinition);

		Company company = _companyLocalService.getCompanyById(
			TestPropsValues.getCompanyId());

		FileEntry fileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), company.getGroupId(), 0,
			RandomTestUtil.randomString() + ".txt", ContentTypes.TEXT_PLAIN,
			RandomTestUtil.randomString(), StringPool.BLANK, StringPool.BLANK,
			StringPool.BLANK,
			new UnsyncByteArrayInputStream(DLTestUtil.randomTextFileBytes()), 0,
			null, null, null, ServiceContextTestUtil.getServiceContext());

		_fileEntryId = fileEntry.getFileEntryId();

		String urlTitle = StringUtil.toLowerCase(RandomTestUtil.randomString());
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext();

		serviceContext.setAttribute(
			"friendlyUrlMap",
			HashMapBuilder.put(
				"en_US", urlTitle
			).build());

		ObjectField objectField = _objectFieldLocalService.fetchObjectField(
			_objectDefinition.getObjectDefinitionId(), objectFieldName);

		Serializable value = _fileEntryId;

		if (localized) {
			value = (Serializable)HashMapBuilder.put(
				"en_US", _fileEntryId
			).build();
		}

		ObjectEntry objectEntry = ObjectEntryTestUtil.addObjectEntry(
			0, _objectDefinition.getObjectDefinitionId(), serviceContext,
			HashMapBuilder.<String, Serializable>put(
				"able", RandomTestUtil.randomString()
			).put(
				localized ? objectField.getI18nObjectFieldName() :
					objectField.getName(),
				value
			).build());

		Map<String, Serializable> values = objectEntry.getValues();

		if (localized) {
			values = (Map<String, Serializable>)values.get(
				objectField.getI18nObjectFieldName());
		}

		long attachmentFileEntryId = GetterUtil.getLong(
			values.get(localized ? "en_US" : objectField.getName()));

		urlTitle = MapUtil.getString(
			objectEntry.getURLTitleMap(), objectEntry.getDefaultLanguageId());

		long fileEntryClassNameId = _classNameLocalService.getClassNameId(
			FileEntry.class);

		FriendlyURLEntry friendlyURLEntry =
			_friendlyURLEntryLocalService.fetchFriendlyURLEntry(
				fileEntry.getGroupId(), fileEntryClassNameId, urlTitle);

		_friendlyURLEntryLocalService.deleteFriendlyURLEntry(
			friendlyURLEntry.getFriendlyURLEntryId());

		UpgradeProcess upgradeProcess = UpgradeTestUtil.getUpgradeStep(
			_upgradeStepRegistrator,
			"com.liferay.object.internal.upgrade.v13_1_1." +
				"AttachmentFileEntryFriendlyURLUpgradeProcess");

		upgradeProcess.upgrade();

		friendlyURLEntry = _friendlyURLEntryLocalService.fetchFriendlyURLEntry(
			fileEntry.getGroupId(), fileEntryClassNameId, urlTitle);

		Assert.assertEquals(
			attachmentFileEntryId, friendlyURLEntry.getClassPK());
	}

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	private long _fileEntryId;

	@Inject
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@DeleteAfterTestRun
	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Inject
	private ObjectFieldLocalService _objectFieldLocalService;

	@Inject(
		filter = "component.name=com.liferay.object.internal.upgrade.registry.ObjectServiceUpgradeStepRegistrator"
	)
	private UpgradeStepRegistrator _upgradeStepRegistrator;

}