/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.info.collection.provider.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.depot.constants.DepotConstants;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.info.collection.provider.CollectionQuery;
import com.liferay.info.collection.provider.RelatedInfoItemCollectionProvider;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.pagination.InfoPage;
import com.liferay.info.pagination.Pagination;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectDefinitionSettingConstants;
import com.liferay.object.constants.ObjectEntryFolderConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.field.builder.TextObjectFieldBuilder;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectDefinitionSettingLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;
import com.liferay.site.cms.site.initializer.test.util.CMSTestUtil;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jürgen Kappler
 */
@RunWith(Arquillian.class)
public class OneToManyObjectRelationshipInfoCollectionProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_childObjectDefinition = _addObjectDefinition(
			new TextObjectFieldBuilder(
			).labelMap(
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString())
			).name(
				"childTextObjectFieldName"
			).build());

		_childObjectDefinition =
			_objectDefinitionLocalService.publishCustomObjectDefinition(
				TestPropsValues.getUserId(),
				_childObjectDefinition.getObjectDefinitionId());

		_parentObjectDefinition = _addObjectDefinition(
			new TextObjectFieldBuilder(
			).labelMap(
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString())
			).name(
				"parentTextObjectFieldName"
			).build());

		_parentObjectDefinition =
			_objectDefinitionLocalService.publishCustomObjectDefinition(
				TestPropsValues.getUserId(),
				_parentObjectDefinition.getObjectDefinitionId());

		_objectRelationshipLocalService.addObjectRelationship(
			null, TestPropsValues.getUserId(),
			_parentObjectDefinition.getObjectDefinitionId(),
			_childObjectDefinition.getObjectDefinitionId(), 0,
			ObjectRelationshipConstants.DELETION_TYPE_CASCADE, false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			"oneToManyRelationshipName", false,
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY, null);
	}

	@Test
	public void testOneToManyObjectRelationshipRelatedInfoCollectionProviderWithCustomObjectDefinition()
		throws Exception {

		ObjectEntry parentObjectEntry = _objectEntryLocalService.addObjectEntry(
			_group.getGroupId(), TestPropsValues.getUserId(),
			_parentObjectDefinition.getObjectDefinitionId(),
			ObjectEntryFolderConstants.PARENT_OBJECT_ENTRY_FOLDER_ID_DEFAULT,
			null,
			HashMapBuilder.<String, Serializable>put(
				"parentTextObjectFieldName", RandomTestUtil.randomString()
			).build(),
			ServiceContextTestUtil.getServiceContext());

		ObjectEntry childObjectEntry1 = _addChildObjectEntry(
			_group, _childObjectDefinition, _parentObjectDefinition,
			parentObjectEntry);

		ObjectEntry childObjectEntry2 = _addChildObjectEntry(
			_group, _childObjectDefinition, _parentObjectDefinition,
			parentObjectEntry);

		RelatedInfoItemCollectionProvider relatedInfoItemCollectionProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				RelatedInfoItemCollectionProvider.class,
				_parentObjectDefinition.getClassName());

		Assert.assertNotNull(relatedInfoItemCollectionProvider);

		CollectionQuery collectionQuery = new CollectionQuery();

		collectionQuery.setPagination(Pagination.of(2, 0));
		collectionQuery.setRelatedItemObject(parentObjectEntry);

		InfoPage collectionInfoPage =
			relatedInfoItemCollectionProvider.getCollectionInfoPage(
				collectionQuery);

		List<ObjectEntry> objectEntries = collectionInfoPage.getPageItems();

		Assert.assertNotNull(objectEntries);
		Assert.assertEquals(objectEntries.toString(), 2, objectEntries.size());
		Assert.assertTrue(objectEntries.contains(childObjectEntry1));
		Assert.assertTrue(objectEntries.contains(childObjectEntry2));

		Assert.assertEquals(2, collectionInfoPage.getTotalCount());
	}

	@Test
	public void testOneToManyObjectRelationshipRelatedInfoCollectionProviderWithSystemObjectDefinition()
		throws Exception {

		// Modifiable system object definition as child

		_depotObjectDefinition1 = _publishCustomObjectDefinition(
			ObjectDefinitionConstants.SCOPE_DEPOT);

		Group cmsGroup = CMSTestUtil.getOrAddGroup(getClass());

		ObjectDefinition modifiableSystemObjectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinition(
				TestPropsValues.getCompanyId(), "CMSBlog");

		String objectRelationshipName1 = StringUtil.randomId();

		_objectRelationshipLocalService.addObjectRelationship(
			null, TestPropsValues.getUserId(),
			_depotObjectDefinition1.getObjectDefinitionId(),
			modifiableSystemObjectDefinition.getObjectDefinitionId(), 0,
			ObjectRelationshipConstants.DELETION_TYPE_CASCADE, false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			objectRelationshipName1, false,
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY, null);

		String name = PrincipalThreadLocal.getName();

		try {
			PrincipalThreadLocal.setName(TestPropsValues.getUserId());

			_depotEntry = _depotEntryLocalService.addDepotEntry(
				HashMapBuilder.put(
					LocaleUtil.getDefault(), RandomTestUtil.randomString()
				).build(),
				HashMapBuilder.put(
					LocaleUtil.getDefault(), RandomTestUtil.randomString()
				).build(),
				DepotConstants.TYPE_SPACE,
				ServiceContextTestUtil.getServiceContext(
					cmsGroup.getGroupId(), TestPropsValues.getUserId()));
		}
		finally {
			PrincipalThreadLocal.setName(name);
		}

		Group depotGroup = _depotEntry.getGroup();

		_objectDefinitionSettingLocalService.addObjectDefinitionSetting(
			TestPropsValues.getUserId(),
			_depotObjectDefinition1.getObjectDefinitionId(),
			ObjectDefinitionSettingConstants.NAME_ACCEPT_ALL_GROUPS,
			StringPool.TRUE);

		ObjectEntry depotObjectEntry1 = _addObjectEntry(
			depotGroup, _depotObjectDefinition1,
			HashMapBuilder.<String, Serializable>put(
				"customTextObjectFieldName", RandomTestUtil.randomString()
			).build());

		ObjectEntry cmsBlogObjectEntry1 = _addObjectEntry(
			depotGroup, modifiableSystemObjectDefinition,
			HashMapBuilder.<String, Serializable>put(
				StringBundler.concat(
					"r_", objectRelationshipName1, "_",
					_depotObjectDefinition1.getPKObjectFieldName()),
				depotObjectEntry1.getObjectEntryId()
			).put(
				"title_i18n",
				HashMapBuilder.put(
					"en_US", RandomTestUtil.randomString()
				).build()
			).build());

		ObjectEntry cmsBlogObjectEntry2 = _addObjectEntry(
			depotGroup, modifiableSystemObjectDefinition,
			HashMapBuilder.<String, Serializable>put(
				StringBundler.concat(
					"r_", objectRelationshipName1, "_",
					_depotObjectDefinition1.getPKObjectFieldName()),
				depotObjectEntry1.getObjectEntryId()
			).put(
				"title_i18n",
				HashMapBuilder.put(
					"en_US", RandomTestUtil.randomString()
				).build()
			).build());

		RelatedInfoItemCollectionProvider relatedInfoItemCollectionProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				RelatedInfoItemCollectionProvider.class,
				_depotObjectDefinition1.getClassName());

		Assert.assertEquals(
			modifiableSystemObjectDefinition.getClassName(),
			relatedInfoItemCollectionProvider.getCollectionItemClassName());

		_assertRelatedEntries(
			relatedInfoItemCollectionProvider, depotObjectEntry1,
			cmsBlogObjectEntry1, cmsBlogObjectEntry2);

		// Modifiable system object definition as parent

		_depotObjectDefinition2 = _publishCustomObjectDefinition(
			ObjectDefinitionConstants.SCOPE_DEPOT);

		String objectRelationshipName2 = StringUtil.randomId();

		_objectRelationshipLocalService.addObjectRelationship(
			null, TestPropsValues.getUserId(),
			modifiableSystemObjectDefinition.getObjectDefinitionId(),
			_depotObjectDefinition2.getObjectDefinitionId(), 0,
			ObjectRelationshipConstants.DELETION_TYPE_CASCADE, false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			objectRelationshipName2, false,
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY, null);

		_objectDefinitionSettingLocalService.addObjectDefinitionSetting(
			TestPropsValues.getUserId(),
			_depotObjectDefinition2.getObjectDefinitionId(),
			ObjectDefinitionSettingConstants.NAME_ACCEPT_ALL_GROUPS,
			StringPool.TRUE);

		ObjectEntry cmsBlogObjectEntry3 = _addObjectEntry(
			depotGroup, modifiableSystemObjectDefinition,
			HashMapBuilder.<String, Serializable>put(
				"title_i18n",
				HashMapBuilder.put(
					"en_US", RandomTestUtil.randomString()
				).build()
			).build());

		ObjectEntry depotObjectEntry2 = _addObjectEntry(
			depotGroup, _depotObjectDefinition2,
			HashMapBuilder.<String, Serializable>put(
				"customTextObjectFieldName", RandomTestUtil.randomString()
			).put(
				StringBundler.concat(
					"r_", objectRelationshipName2, "_",
					modifiableSystemObjectDefinition.getPKObjectFieldName()),
				cmsBlogObjectEntry3.getObjectEntryId()
			).build());

		ObjectEntry depotObjectEntry3 = _addObjectEntry(
			depotGroup, _depotObjectDefinition2,
			HashMapBuilder.<String, Serializable>put(
				"customTextObjectFieldName", RandomTestUtil.randomString()
			).put(
				StringBundler.concat(
					"r_", objectRelationshipName2, "_",
					modifiableSystemObjectDefinition.getPKObjectFieldName()),
				cmsBlogObjectEntry3.getObjectEntryId()
			).build());

		relatedInfoItemCollectionProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				RelatedInfoItemCollectionProvider.class,
				modifiableSystemObjectDefinition.getClassName());

		Assert.assertEquals(
			_depotObjectDefinition2.getClassName(),
			relatedInfoItemCollectionProvider.getCollectionItemClassName());

		_assertRelatedEntries(
			relatedInfoItemCollectionProvider, cmsBlogObjectEntry3,
			depotObjectEntry2, depotObjectEntry3);

		// Unmodifiable system object definition as child

		ObjectDefinition userObjectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinitionByClassName(
				TestPropsValues.getCompanyId(), User.class.getName());

		_customObjectDefinition1 = _publishCustomObjectDefinition(
			ObjectDefinitionConstants.SCOPE_SITE);

		_objectRelationshipLocalService.addObjectRelationship(
			null, TestPropsValues.getUserId(),
			_customObjectDefinition1.getObjectDefinitionId(),
			userObjectDefinition.getObjectDefinitionId(), 0,
			ObjectRelationshipConstants.DELETION_TYPE_CASCADE, false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			StringUtil.randomId(), false,
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY, null);

		Assert.assertNull(
			_infoItemServiceRegistry.getFirstInfoItemService(
				RelatedInfoItemCollectionProvider.class,
				_customObjectDefinition1.getClassName()));

		// Unmodifiable system object definition as parent

		_customObjectDefinition2 = _publishCustomObjectDefinition(
			ObjectDefinitionConstants.SCOPE_SITE);

		_objectRelationshipLocalService.addObjectRelationship(
			null, TestPropsValues.getUserId(),
			userObjectDefinition.getObjectDefinitionId(),
			_customObjectDefinition2.getObjectDefinitionId(), 0,
			ObjectRelationshipConstants.DELETION_TYPE_CASCADE, false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			StringUtil.randomId(), false,
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY, null);

		Assert.assertNull(
			_infoItemServiceRegistry.getFirstInfoItemService(
				RelatedInfoItemCollectionProvider.class,
				userObjectDefinition.getClassName()));
	}

	private ObjectEntry _addChildObjectEntry(
			Group group, ObjectDefinition objectDefinition,
			ObjectDefinition parentObjectDefinition,
			ObjectEntry parentObjectEntry)
		throws Exception {

		return _objectEntryLocalService.addObjectEntry(
			group.getGroupId(), TestPropsValues.getUserId(),
			objectDefinition.getObjectDefinitionId(),
			ObjectEntryFolderConstants.PARENT_OBJECT_ENTRY_FOLDER_ID_DEFAULT,
			null,
			HashMapBuilder.<String, Serializable>put(
				"childTextObjectFieldName", RandomTestUtil.randomString()
			).put(
				"r_oneToManyRelationshipName_" +
					parentObjectDefinition.getPKObjectFieldName(),
				parentObjectEntry.getObjectEntryId()
			).build(),
			ServiceContextTestUtil.getServiceContext());
	}

	private ObjectDefinition _addObjectDefinition(ObjectField objectField)
		throws Exception {

		return _addObjectDefinition(
			objectField, ObjectDefinitionConstants.SCOPE_SITE);
	}

	private ObjectDefinition _addObjectDefinition(
			ObjectField objectField, String scope)
		throws Exception {

		return _objectDefinitionLocalService.addCustomObjectDefinition(
			null, TestPropsValues.getUserId(), 0, null, true, false, true,
			false, true, false, false, false, false, null,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			ObjectDefinitionTestUtil.getRandomName(), null, null,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			true, scope, ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT,
			Collections.emptyList(), Arrays.asList(objectField),
			Collections.emptyList(), new ServiceContext());
	}

	private ObjectEntry _addObjectEntry(
			Group group, ObjectDefinition objectDefinition,
			Map<String, Serializable> values)
		throws Exception {

		return _objectEntryLocalService.addObjectEntry(
			group.getGroupId(), TestPropsValues.getUserId(),
			objectDefinition.getObjectDefinitionId(),
			ObjectEntryFolderConstants.PARENT_OBJECT_ENTRY_FOLDER_ID_DEFAULT,
			null, values,
			ServiceContextTestUtil.getServiceContext(group.getGroupId()));
	}

	private void _assertRelatedEntries(
			RelatedInfoItemCollectionProvider relatedInfoItemCollectionProvider,
			ObjectEntry parentObjectEntry, ObjectEntry... expectedObjectEntries)
		throws Exception {

		CollectionQuery collectionQuery = new CollectionQuery();

		collectionQuery.setPagination(
			Pagination.of(expectedObjectEntries.length, 0));
		collectionQuery.setRelatedItemObject(parentObjectEntry);

		InfoPage collectionInfoPage =
			relatedInfoItemCollectionProvider.getCollectionInfoPage(
				collectionQuery);

		List<ObjectEntry> objectEntries = collectionInfoPage.getPageItems();

		Assert.assertEquals(
			objectEntries.toString(), expectedObjectEntries.length,
			objectEntries.size());

		for (ObjectEntry expectedObjectEntry : expectedObjectEntries) {
			Assert.assertTrue(objectEntries.contains(expectedObjectEntry));
		}

		Assert.assertEquals(
			expectedObjectEntries.length, collectionInfoPage.getTotalCount());
	}

	private ObjectDefinition _publishCustomObjectDefinition(String scope)
		throws Exception {

		ObjectDefinition objectDefinition = _addObjectDefinition(
			new TextObjectFieldBuilder(
			).labelMap(
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString())
			).name(
				"customTextObjectFieldName"
			).build(),
			scope);

		return _objectDefinitionLocalService.publishCustomObjectDefinition(
			TestPropsValues.getUserId(),
			objectDefinition.getObjectDefinitionId());
	}

	@DeleteAfterTestRun
	private ObjectDefinition _childObjectDefinition;

	@DeleteAfterTestRun
	private ObjectDefinition _customObjectDefinition1;

	@DeleteAfterTestRun
	private ObjectDefinition _customObjectDefinition2;

	@DeleteAfterTestRun
	private DepotEntry _depotEntry;

	@Inject
	private DepotEntryLocalService _depotEntryLocalService;

	@DeleteAfterTestRun
	private ObjectDefinition _depotObjectDefinition1;

	@DeleteAfterTestRun
	private ObjectDefinition _depotObjectDefinition2;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private InfoItemServiceRegistry _infoItemServiceRegistry;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Inject
	private ObjectDefinitionSettingLocalService
		_objectDefinitionSettingLocalService;

	@Inject
	private ObjectEntryLocalService _objectEntryLocalService;

	@Inject
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	@DeleteAfterTestRun
	private ObjectDefinition _parentObjectDefinition;

}