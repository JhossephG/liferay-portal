/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.dto.v1_0.util;

import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.headless.admin.site.dto.v1_0.ClassNameReference;
import com.liferay.info.collection.provider.InfoCollectionProvider;
import com.liferay.info.collection.provider.RelatedInfoItemCollectionProvider;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.list.provider.item.selector.criterion.InfoListProviderItemSelectorReturnType;
import com.liferay.object.constants.ObjectDefinitionSettingConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectDefinitionSetting;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.object.service.ObjectDefinitionSettingLocalServiceUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Jhosseph Gonzalez
 */
public class CollectionUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	@Test
	public void testGetCollectionJSONObjectWithObjectDefinitionClassNameReference()
		throws Exception {

		// InfoCollectionProvider

		String className = "com.liferay.object.model.ObjectDefinition#C3D4";
		String key = RandomTestUtil.randomString();
		String label = RandomTestUtil.randomString();
		String oldClassName = "com.liferay.object.model.ObjectDefinition#A1B2";

		try (MockedStatic<CompanyThreadLocal> companyThreadLocalMockedStatic =
				Mockito.mockStatic(CompanyThreadLocal.class);
			MockedStatic<ExportImportThreadLocal>
				exportImportThreadLocalMockedStatic = Mockito.mockStatic(
					ExportImportThreadLocal.class);
			MockedStatic<ObjectDefinitionLocalServiceUtil>
				objectDefinitionLocalServiceUtilMockedStatic =
					Mockito.mockStatic(ObjectDefinitionLocalServiceUtil.class);
			MockedStatic<ObjectDefinitionSettingLocalServiceUtil>
				objectDefinitionSettingLocalServiceUtilMockedStatic =
					Mockito.mockStatic(
						ObjectDefinitionSettingLocalServiceUtil.class)) {

			ClassNameReference classNameReference = _createClassNameReference(
				oldClassName);

			long companyId = RandomTestUtil.randomLong();

			companyThreadLocalMockedStatic.when(
				CompanyThreadLocal::getCompanyId
			).thenReturn(
				companyId
			);

			InfoCollectionProvider infoCollectionProvider = Mockito.mock(
				InfoCollectionProvider.class);
			InfoItemServiceRegistry infoItemServiceRegistry = Mockito.mock(
				InfoItemServiceRegistry.class);

			_mockInfoCollectionProvider(
				className, infoCollectionProvider, infoItemServiceRegistry,
				oldClassName, key, label);

			exportImportThreadLocalMockedStatic.when(
				ExportImportThreadLocal::isImportInProcess
			).thenReturn(
				true
			);

			ObjectDefinition objectDefinition = Mockito.mock(
				ObjectDefinition.class);

			Mockito.when(
				objectDefinition.getClassName()
			).thenReturn(
				className
			);

			long objectDefinitionId = RandomTestUtil.randomLong();

			objectDefinitionLocalServiceUtilMockedStatic.when(
				() -> ObjectDefinitionLocalServiceUtil.fetchObjectDefinition(
					objectDefinitionId)
			).thenReturn(
				objectDefinition
			);

			ObjectDefinitionSetting objectDefinitionSetting = Mockito.mock(
				ObjectDefinitionSetting.class);

			Mockito.when(
				objectDefinitionSetting.getObjectDefinitionId()
			).thenReturn(
				objectDefinitionId
			);

			objectDefinitionSettingLocalServiceUtilMockedStatic.when(
				() ->
					ObjectDefinitionSettingLocalServiceUtil.
						fetchObjectDefinitionSetting(
							companyId,
							ObjectDefinitionSettingConstants.
								NAME_OLD_CLASS_NAME,
							oldClassName)
			).thenReturn(
				objectDefinitionSetting
			);

			_assertCollectionJSONObject(
				oldClassName,
				CollectionUtil.getCollectionJSONObject(
					classNameReference, companyId, infoItemServiceRegistry,
					RandomTestUtil.randomLong()),
				key, label);

			Mockito.verify(
				infoItemServiceRegistry
			).getInfoItemService(
				InfoCollectionProvider.class, className
			);

			Mockito.verify(
				infoItemServiceRegistry, Mockito.never()
			).getInfoItemService(
				RelatedInfoItemCollectionProvider.class, className
			);

			objectDefinitionLocalServiceUtilMockedStatic.verify(
				() -> ObjectDefinitionLocalServiceUtil.fetchObjectDefinition(
					objectDefinitionId));
			objectDefinitionSettingLocalServiceUtilMockedStatic.verify(
				() ->
					ObjectDefinitionSettingLocalServiceUtil.
						fetchObjectDefinitionSetting(
							companyId,
							ObjectDefinitionSettingConstants.
								NAME_OLD_CLASS_NAME,
							oldClassName));
		}

		// RelatedInfoItemCollectionProvider

		className = StringBundler.concat(
			"com.liferay.object.internal.info.collection.provider.",
			"OneToManyObjectRelationshipRelatedInfoCollectionProvider_",
			className, "_", RandomTestUtil.randomString());

		ClassNameReference classNameReference = _createClassNameReference(
			className);

		InfoItemServiceRegistry infoItemServiceRegistry = Mockito.mock(
			InfoItemServiceRegistry.class);
		key = RandomTestUtil.randomString();
		label = RandomTestUtil.randomString();
		RelatedInfoItemCollectionProvider relatedInfoItemCollectionProvider =
			Mockito.mock(RelatedInfoItemCollectionProvider.class);

		_mockRelatedInfoItemCollectionProvider(
			className, infoItemServiceRegistry, oldClassName, key, label,
			relatedInfoItemCollectionProvider);

		_assertCollectionJSONObject(
			oldClassName,
			CollectionUtil.getCollectionJSONObject(
				classNameReference, RandomTestUtil.randomLong(),
				infoItemServiceRegistry, RandomTestUtil.randomLong()),
			key, label);

		Mockito.verify(
			infoItemServiceRegistry
		).getInfoItemService(
			InfoCollectionProvider.class, className
		);

		Mockito.verify(
			infoItemServiceRegistry
		).getInfoItemService(
			RelatedInfoItemCollectionProvider.class, className
		);
	}

	private void _assertCollectionJSONObject(
		String itemType, JSONObject jsonObject, String key, String label) {

		Assert.assertEquals(itemType, jsonObject.getString("itemType"));
		Assert.assertEquals(key, jsonObject.getString("key"));
		Assert.assertEquals(label, jsonObject.getString("title"));
		Assert.assertEquals(
			InfoListProviderItemSelectorReturnType.class.getName(),
			jsonObject.getString("type"));
	}

	private ClassNameReference _createClassNameReference(String className) {
		ClassNameReference classNameReference = new ClassNameReference();

		classNameReference.setClassName(() -> className);

		return classNameReference;
	}

	private void _mockInfoCollectionProvider(
		String className, InfoCollectionProvider infoCollectionProvider,
		InfoItemServiceRegistry infoItemServiceRegistry, String itemType,
		String key, String label) {

		Mockito.when(
			infoCollectionProvider.getCollectionItemClassName()
		).thenReturn(
			itemType
		);

		Mockito.when(
			infoCollectionProvider.getKey()
		).thenReturn(
			key
		);

		Mockito.when(
			infoCollectionProvider.getLabel(LocaleUtil.getDefault())
		).thenReturn(
			label
		);

		Mockito.when(
			infoItemServiceRegistry.getInfoItemService(
				InfoCollectionProvider.class, className)
		).thenReturn(
			infoCollectionProvider
		);
	}

	private void _mockRelatedInfoItemCollectionProvider(
		String className, InfoItemServiceRegistry infoItemServiceRegistry,
		String itemType, String key, String label,
		RelatedInfoItemCollectionProvider relatedInfoItemCollectionProvider) {

		Mockito.when(
			infoItemServiceRegistry.getInfoItemService(
				InfoCollectionProvider.class, className)
		).thenReturn(
			null
		);

		Mockito.when(
			infoItemServiceRegistry.getInfoItemService(
				RelatedInfoItemCollectionProvider.class, className)
		).thenReturn(
			relatedInfoItemCollectionProvider
		);

		Mockito.when(
			relatedInfoItemCollectionProvider.getCollectionItemClassName()
		).thenReturn(
			itemType
		);

		Mockito.when(
			relatedInfoItemCollectionProvider.getKey()
		).thenReturn(
			key
		);

		Mockito.when(
			relatedInfoItemCollectionProvider.getLabel(LocaleUtil.getDefault())
		).thenReturn(
			label
		);
	}

}