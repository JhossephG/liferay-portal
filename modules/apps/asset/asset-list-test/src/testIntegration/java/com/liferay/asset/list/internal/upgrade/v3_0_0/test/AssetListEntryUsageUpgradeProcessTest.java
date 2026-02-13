/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.list.internal.upgrade.v3_0_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.list.model.AssetListEntryUsage;
import com.liferay.asset.list.service.AssetListEntryUsageLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.object.constants.ObjectDefinitionSettingConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectDefinitionSetting;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectDefinitionSettingLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.test.util.UpgradeTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jhosseph Gonzalez
 */
@RunWith(Arquillian.class)
public class AssetListEntryUsageUpgradeProcessTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_layout = LayoutTestUtil.addTypeContentLayout(_group);

		_objectDefinition =
			ObjectDefinitionTestUtil.addCustomObjectDefinition();

		_oldClassName = "C_" + RandomTestUtil.randomString();

		_objectDefinitionSetting =
			_objectDefinitionSettingLocalService.addObjectDefinitionSetting(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(),
				ObjectDefinitionSettingConstants.NAME_OLD_CLASS_NAME,
				_oldClassName);
	}

	@After
	public void tearDown() throws Exception {
		_objectDefinitionLocalService.deleteObjectDefinition(_objectDefinition);

		_objectDefinitionSettingLocalService.deleteObjectDefinitionSetting(
			_objectDefinitionSetting);
	}

	@Test
	public void testUpgradeManyToManyObjectRelationshipRelatedInfoCollectionProvider()
		throws Exception {

		_testUpgrade("ManyToManyObjectRelationship");
	}

	@Test
	public void testUpgradeOneToManyObjectRelationshipRelatedInfoCollectionProvider()
		throws Exception {

		_testUpgrade("OneToManyObjectRelationship");
	}

	private void _runUpgrade() throws Exception {
		UpgradeProcess upgradeProcess = UpgradeTestUtil.getUpgradeStep(
			_upgradeStepRegistrator, _CLASS_NAME);

		upgradeProcess.upgrade();

		_entityCache.clearCache();
		_multiVMPool.clear();
	}

	private void _testUpgrade(String relationshipType) throws Exception {
		String keySuffix = RandomTestUtil.randomString();

		String key = StringBundler.concat(
			_INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX, relationshipType,
			"RelatedInfoCollectionProvider_", _group.getCompanyId(), "_",
			_oldClassName, "_", keySuffix);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group, TestPropsValues.getUserId());

		AssetListEntryUsage assetListEntryUsage =
			_assetListEntryUsageLocalService.addAssetListEntryUsage(
				TestPropsValues.getUserId(), _group.getGroupId(),
				_portal.getClassNameId(Layout.class),
				RandomTestUtil.randomString(), 0, key, _layout.getPlid(),
				serviceContext);

		Assert.assertEquals(key, assetListEntryUsage.getKey());

		_runUpgrade();

		assetListEntryUsage =
			_assetListEntryUsageLocalService.getAssetListEntryUsage(
				assetListEntryUsage.getAssetListEntryUsageId());

		Assert.assertEquals(
			StringBundler.concat(
				_INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX, relationshipType,
				"RelatedInfoCollectionProvider_",
				_objectDefinition.getClassName(), "_", keySuffix),
			assetListEntryUsage.getKey());
	}

	private static final String _CLASS_NAME =
		"com.liferay.asset.list.internal.upgrade.v3_0_0." +
			"AssetListEntryUsageUpgradeProcess";

	private static final String _INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX =
		"com.liferay.object.internal.info.collection.provider.";

	@Inject
	private AssetListEntryUsageLocalService _assetListEntryUsageLocalService;

	@Inject
	private EntityCache _entityCache;

	@DeleteAfterTestRun
	private Group _group;

	private Layout _layout;

	@Inject
	private MultiVMPool _multiVMPool;

	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	private ObjectDefinitionSetting _objectDefinitionSetting;

	@Inject
	private ObjectDefinitionSettingLocalService
		_objectDefinitionSettingLocalService;

	private String _oldClassName;

	@Inject
	private Portal _portal;

	@Inject(
		filter = "(&(component.name=com.liferay.asset.list.internal.upgrade.registry.AssetListServiceUpgradeStepRegistrator))"
	)
	private UpgradeStepRegistrator _upgradeStepRegistrator;

}