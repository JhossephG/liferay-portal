/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.upgrade.v13_1_1.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.depot.constants.DepotConstants;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.test.util.DLTestUtil;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.related.models.test.util.ObjectEntryTestUtil;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.test.util.UpgradeTestUtil;

import java.io.Serializable;

import java.util.Collections;

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
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@After
	public void tearDown() throws Exception {
		if ((_fileEntryId > 0) &&
			(_dlAppLocalService.fetchFileEntry(_fileEntryId) != null)) {

			_dlAppLocalService.deleteFileEntry(_fileEntryId);
		}
	}

	@Test
	public void testUpgrade() throws Exception {
		DepotEntry depotEntry = _depotEntryLocalService.addDepotEntry(
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap(), DepotConstants.TYPE_SPACE,
			ServiceContextTestUtil.getServiceContext());

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.
				getObjectDefinitionByExternalReferenceCode(
					"L_CMS_BASIC_DOCUMENT", TestPropsValues.getCompanyId());

		String urlTitle = StringUtil.toLowerCase(RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext();

		serviceContext.setAttribute(
			"friendlyUrlMap",
			HashMapBuilder.put(
				"en_US", urlTitle
			).build());

		ObjectEntry objectEntry = ObjectEntryTestUtil.addObjectEntry(
			depotEntry.getGroupId(), objectDefinition.getObjectDefinitionId(),
			serviceContext,
			HashMapBuilder.<String, Serializable>put(
				"file", () -> _addFileEntry().getFileEntryId()
			).put(
				"title_i18n",
				HashMapBuilder.put(
					"en_US", RandomTestUtil.randomString()
				).build()
			).build());

		objectEntry = _objectEntryLocalService.getObjectEntry(
			objectEntry.getObjectEntryId());

		long dlFileEntryId = MapUtil.getLong(objectEntry.getValues(), "file");

		_fileEntryId = dlFileEntryId;

		long fileEntryClassNameId = _classNameLocalService.getClassNameId(
			FileEntry.class);

		_updateFileEntryFriendlyURLTitle(
			dlFileEntryId, fileEntryClassNameId, "en_US",
			StringUtil.toLowerCase(RandomTestUtil.randomString()));

		UpgradeProcess upgradeProcess = UpgradeTestUtil.getUpgradeStep(
			_upgradeStepRegistrator,
			"com.liferay.object.internal.upgrade.v13_1_1." +
				"AttachmentFileEntryFriendlyURLUpgradeProcess");

		upgradeProcess.upgrade();

		_assertFileEntryFriendlyURLEntry(
			dlFileEntryId, urlTitle, fileEntryClassNameId, "en_US");

		upgradeProcess.upgrade();

		_assertFileEntryFriendlyURLEntry(
			dlFileEntryId, urlTitle, fileEntryClassNameId, "en_US");
	}

	private FileEntry _addFileEntry() throws Exception {
		Company company = _companyLocalService.getCompanyById(
			TestPropsValues.getCompanyId());

		return _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), company.getGroupId(), 0,
			RandomTestUtil.randomString() + ".txt", ContentTypes.TEXT_PLAIN,
			RandomTestUtil.randomString(), StringPool.BLANK, StringPool.BLANK,
			StringPool.BLANK,
			new UnsyncByteArrayInputStream(DLTestUtil.randomTextFileBytes()), 0,
			null, null, null, ServiceContextTestUtil.getServiceContext());
	}

	private void _assertFileEntryFriendlyURLEntry(
		long dlFileEntryId, String expectedURLTitle, long fileEntryClassNameId,
		String languageId) {

		FriendlyURLEntry friendlyURLEntry =
			_friendlyURLEntryLocalService.fetchMainFriendlyURLEntry(
				fileEntryClassNameId, dlFileEntryId);

		Assert.assertEquals(
			expectedURLTitle, friendlyURLEntry.getUrlTitle(languageId));
	}

	private void _updateFileEntryFriendlyURLTitle(
			long dlFileEntryId, long fileEntryClassNameId, String languageId,
			String urlTitle)
		throws Exception {

		FileEntry fileEntry = _dlAppLocalService.fetchFileEntry(dlFileEntryId);

		_friendlyURLEntryLocalService.addFriendlyURLEntry(
			fileEntry.getGroupId(), fileEntryClassNameId, dlFileEntryId,
			languageId, Collections.singletonMap(languageId, urlTitle),
			ServiceContextTestUtil.getServiceContext());
	}

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private DepotEntryLocalService _depotEntryLocalService;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	private long _fileEntryId;

	@Inject
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Inject
	private ObjectEntryLocalService _objectEntryLocalService;

	@Inject(
		filter = "component.name=com.liferay.object.internal.upgrade.registry.ObjectServiceUpgradeStepRegistrator"
	)
	private UpgradeStepRegistrator _upgradeStepRegistrator;

}