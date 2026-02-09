/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.internal.upgrade.v5_8_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.change.tracking.test.util.BaseCTUpgradeProcessTestCase;
import com.liferay.info.list.provider.item.selector.criterion.InfoListProviderItemSelectorReturnType;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureRelLocalService;
import com.liferay.layout.provider.LayoutStructureProvider;
import com.liferay.layout.test.util.ContentLayoutTestUtil;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectDefinitionSettingConstants;
import com.liferay.object.internal.info.collection.provider.BaseObjectRelationshipRelatedInfoCollectionProvider;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectDefinitionSetting;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectDefinitionSettingLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.change.tracking.CTModel;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.change.tracking.CTService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.test.util.UpgradeTestUtil;
import com.liferay.segments.service.SegmentsExperienceLocalService;

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
public class LayoutPageTemplateStructureRelUpgradeProcessTest
	extends BaseCTUpgradeProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_layout = LayoutTestUtil.addTypeContentLayout(_group);

		_draftLayout = _layout.fetchDraftLayout();

		_segmentsExperienceId =
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				_draftLayout.getPlid());

		_objectDefinition =
			ObjectDefinitionTestUtil.addCustomObjectDefinition();

		_oldClassName = ObjectDefinitionConstants.
							CLASS_NAME_PREFIX_CUSTOM_OBJECT_DEFINITION + "A1B2";

		_objectDefinitionSettingLocalService.addObjectDefinitionSetting(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(),
			ObjectDefinitionSettingConstants.NAME_OLD_CLASS_NAME, _oldClassName);

		ObjectDefinitionSetting objectDefinitionSetting =
			_objectDefinitionSettingLocalService.fetchObjectDefinitionSetting(
				_objectDefinition.getObjectDefinitionId(),
				ObjectDefinitionSettingConstants.NAME_OLD_CLASS_NAME);

		Assert.assertNotNull(objectDefinitionSetting);

		ServiceContextThreadLocal.pushServiceContext(
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));
	}

	@After
	public void tearDown() throws Exception {
		_objectDefinitionLocalService.deleteObjectDefinition(_objectDefinition);

		ServiceContextThreadLocal.popServiceContext();
	}

	@Test
	public void testUpgrade() throws Exception {
		String key1 = StringBundler.concat(
			_PACKAGE_PATH, "OneToManyObjectRelationship",
			"RelatedInfoCollectionProvider_",
			_oldClassName, "_", _KEY_SUFFIX_1);

		String key2 = StringBundler.concat(
			_PACKAGE_PATH, "OneToManyObjectRelationship",
			"RelatedInfoCollectionProvider_",
			_oldClassName, "_", _KEY_SUFFIX_2);

		String key3 = StringBundler.concat(
			_PACKAGE_PATH, "ManyToManyObjectRelationship",
			"RelatedInfoCollectionProvider_",
			_oldClassName, "_", _KEY_SUFFIX_2);

		ContentLayoutTestUtil.addCollectionDisplayToLayout(
			JSONUtil.put(
				"itemType", _oldClassName
				).put(
				"key", key1
			).put(
				"type",
				InfoListProviderItemSelectorReturnType.class.getName()
			),
			_draftLayout, _layoutStructureProvider, null, null, 0,
			_segmentsExperienceId);

		ContentLayoutTestUtil.addCollectionDisplayToLayout(
			JSONUtil.put(
				"sourceItemType", _oldClassName
			).put(
				"key", key2
			).put(
				"type",
				InfoListProviderItemSelectorReturnType.class.getName()
			),
			_draftLayout, _layoutStructureProvider, null, null, 1,
			_segmentsExperienceId);

		ContentLayoutTestUtil.addCollectionDisplayToLayout(
			JSONUtil.put(
				"itemType", _oldClassName
			).put(
				"key", key3
			).put(
				"type",
				InfoListProviderItemSelectorReturnType.class.getName()
			),
			_draftLayout, _layoutStructureProvider, null, null, 2,
			_segmentsExperienceId);

		ContentLayoutTestUtil.addCollectionDisplayToLayout(
			JSONUtil.put(
				"key", _OTHER_PROVIDER_KEY
			).put(
				"type",
				InfoListProviderItemSelectorReturnType.class.getName()
			),
			_draftLayout, _layoutStructureProvider, null, null, 3,
			_segmentsExperienceId);

		LayoutStructure layoutStructure =
			_layoutStructureProvider.getLayoutStructure(
				_draftLayout.getPlid(), _segmentsExperienceId);

		_layoutPageTemplateStructureLocalService.
			updateLayoutPageTemplateStructureData(
				_draftLayout.getUserId(), _draftLayout.getGroupId(),
				_draftLayout.getPlid(), _segmentsExperienceId,
				layoutStructure.toString());

		ContentLayoutTestUtil.publishLayout(_draftLayout, _layout);

		long publishedSegmentsExperienceId =
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				_layout.getPlid());

		Assert.assertEquals(
			key1,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_layout, publishedSegmentsExperienceId,
				_KEY_SUFFIX_1));
		Assert.assertEquals(
			key2,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_layout, publishedSegmentsExperienceId,
				_KEY_SUFFIX_2));
		Assert.assertEquals(
			key3,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_layout, publishedSegmentsExperienceId,
				_KEY_SUFFIX_3));
		_assertLayoutHasCollectionKey(
			_layout, publishedSegmentsExperienceId, _OTHER_PROVIDER_KEY);

		Assert.assertEquals(
			key1,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_draftLayout, _segmentsExperienceId,
				_KEY_SUFFIX_1));
		Assert.assertEquals(
			key2,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_draftLayout, _segmentsExperienceId, _KEY_SUFFIX_2));
		Assert.assertEquals(
			key3,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_draftLayout, _segmentsExperienceId, _KEY_SUFFIX_3));
		_assertLayoutHasCollectionKey(
			_draftLayout, _segmentsExperienceId, _OTHER_PROVIDER_KEY);

		runUpgrade();

		String upgradedKey1 = StringBundler.concat(
			_PACKAGE_PATH, "OneToManyObjectRelationship",
			"RelatedInfoCollectionProvider_",_objectDefinition.getClassName(),
			"_", _KEY_SUFFIX_1);

		String upgradedKey2 = StringBundler.concat(
			_PACKAGE_PATH, "OneToManyObjectRelationship",
			"RelatedInfoCollectionProvider_",_objectDefinition.getClassName(),
			"_", _KEY_SUFFIX_2);

		String upgradedKey3 = StringBundler.concat(
			_PACKAGE_PATH, "ManyToManyObjectRelationship",
			"RelatedInfoCollectionProvider_",_objectDefinition.getClassName(),
			"_", _KEY_SUFFIX_3);

		Assert.assertEquals(
			upgradedKey1,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_layout, publishedSegmentsExperienceId,
				_KEY_SUFFIX_1));
		Assert.assertEquals(
			upgradedKey2,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_layout, publishedSegmentsExperienceId,
				_KEY_SUFFIX_2));
		Assert.assertEquals(
			upgradedKey3,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_layout, publishedSegmentsExperienceId,
				_KEY_SUFFIX_3));
		_assertLayoutHasCollectionKey(
			_layout, publishedSegmentsExperienceId, _OTHER_PROVIDER_KEY);

		Assert.assertEquals(
			upgradedKey1,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_draftLayout, _segmentsExperienceId,
				_KEY_SUFFIX_1));
		Assert.assertEquals(
			upgradedKey2,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_draftLayout, _segmentsExperienceId, _KEY_SUFFIX_2));
		Assert.assertEquals(
			upgradedKey3,
			_getLayoutPageTemplateStructureDataKeyByKeySuffix(
				_draftLayout, _segmentsExperienceId, _KEY_SUFFIX_3));
		_assertLayoutHasCollectionKey(
			_draftLayout, _segmentsExperienceId, _OTHER_PROVIDER_KEY);
	}

	@Override
	protected CTModel<?> addCTModel() throws Exception {
		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					_draftLayout.getGroupId(), _draftLayout.getPlid());

		return _layoutPageTemplateStructureRelLocalService.
			fetchLayoutPageTemplateStructureRel(
				layoutPageTemplateStructure.getLayoutPageTemplateStructureId(),
				_segmentsExperienceLocalService.
					fetchDefaultSegmentsExperienceId(_draftLayout.getPlid()));
	}

	@Override
	protected CTService<?> getCTService() {
		return _layoutPageTemplateStructureRelLocalService;
	}

	@Override
	protected void runUpgrade() throws Exception {
		UpgradeProcess upgradeProcess = UpgradeTestUtil.getUpgradeStep(
			_upgradeStepRegistrator, _CLASS_NAME);

		upgradeProcess.upgrade();

		_entityCache.clearCache();
		_multiVMPool.clear();
	}

	@Override
	protected CTModel<?> updateCTModel(CTModel<?> ctModel) throws Exception {
		LayoutPageTemplateStructureRel layoutPageTemplateStructureRel =
			(LayoutPageTemplateStructureRel)ctModel;

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				getLayoutPageTemplateStructure(
					layoutPageTemplateStructureRel.
						getLayoutPageTemplateStructureId());

		Layout layout = _layoutLocalService.getLayout(
			layoutPageTemplateStructure.getPlid());

		ContentLayoutTestUtil.addCollectionDisplayToLayout(
			JSONUtil.put(
				"key", _OTHER_PROVIDER_KEY
			).put(
				"type",
				InfoListProviderItemSelectorReturnType.class.getName()
			),
			layout, _layoutStructureProvider, null, null, 0,
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				layout.getPlid()));

		return _layoutPageTemplateStructureRelLocalService.
			getLayoutPageTemplateStructureRel(
				layoutPageTemplateStructureRel.
					getLayoutPageTemplateStructureRelId());
	}

	private String _getLayoutPageTemplateStructureDataKeyByKeySuffix(
		Layout layout, long segmentsExperienceId, String keySuffix) {

		LayoutStructure layoutStructure =
			_layoutStructureProvider.getLayoutStructure(
				layout.getPlid(), segmentsExperienceId);

		String foundKey = null;
		int count = 0;

		for (CollectionStyledLayoutStructureItem item :
			layoutStructure.getCollectionStyledLayoutStructureItems()) {

			JSONObject collectionJSONObject = item.getCollectionJSONObject();

			if (collectionJSONObject == null) {
				continue;
			}

			String key = collectionJSONObject.getString("key");

			if (StringUtil.startsWith(key, _PACKAGE_PATH) &&
				StringUtil.endsWith(key, "_" + keySuffix)) {

				foundKey = key;
				count++;
			}
		}

		if (count == 1) {
			return foundKey;
		}

		if (count == 0) {
			Assert.fail(
				"No collection item found with key starting with " +
				_PACKAGE_PATH + " and ending with _" + keySuffix);
		}
		else {
			Assert.fail(
				"Expected exactly 1 collection item with key starting with " +
				_PACKAGE_PATH + " and ending with _" + keySuffix +
				", but found " + count);
		}

		return null;
	}

	private void _assertLayoutHasCollectionKey(
		Layout layout, long segmentsExperienceId, String expectedKey) {

		LayoutStructure layoutStructure =
			_layoutStructureProvider.getLayoutStructure(
				layout.getPlid(), segmentsExperienceId);

		for (CollectionStyledLayoutStructureItem item :
			layoutStructure.getCollectionStyledLayoutStructureItems()) {

			JSONObject collectionJSONObject = item.getCollectionJSONObject();

			if (collectionJSONObject == null) {
				continue;
			}

			if (expectedKey.equals(collectionJSONObject.getString("key"))) {
				return;
			}
		}

		Assert.fail("No collection item found with key " + expectedKey);
	}

	private String _getKeyClassName(String className) {
		return StringUtil.replace(
			className, new char[] {'.', '#'}, new char[] {'_', '_'});
	}

	private static final String _CLASS_NAME =
		"com.liferay.layout.page.template.internal.upgrade.v5_8_0." +
		"LayoutPageTemplateStructureRelUpgradeProcess";

	private static final String _KEY_SUFFIX_1 = "entryA";

	private static final String _KEY_SUFFIX_2 = "entryB";

	private static final String _KEY_SUFFIX_3 = "entryC";

	private static final String _PACKAGE_PATH =
		BaseObjectRelationshipRelatedInfoCollectionProvider.class.getPackage() +
		StringPool.PERIOD;

	private static final String _OTHER_PROVIDER_KEY =
		"com.liferay.asset.internal.info.collection.provider." +
		"RecentContentInfoCollectionProvider";

	@Inject(
		filter = "(&(component.name=com.liferay.layout.page.template.internal.upgrade.registry.LayoutPageTemplateServiceUpgradeStepRegistrator))"
	)
	private static UpgradeStepRegistrator _upgradeStepRegistrator;

	private Layout _draftLayout;

	@Inject
	private com.liferay.portal.kernel.dao.orm.EntityCache _entityCache;

	@DeleteAfterTestRun
	private Group _group;

	private Layout _layout;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Inject
	private LayoutPageTemplateStructureRelLocalService
		_layoutPageTemplateStructureRelLocalService;

	@Inject
	private LayoutStructureProvider _layoutStructureProvider;

	@Inject
	private MultiVMPool _multiVMPool;

	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Inject
	private ObjectDefinitionSettingLocalService
		_objectDefinitionSettingLocalService;

	private String _oldClassName;

	private long _segmentsExperienceId;

	@Inject
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

}
