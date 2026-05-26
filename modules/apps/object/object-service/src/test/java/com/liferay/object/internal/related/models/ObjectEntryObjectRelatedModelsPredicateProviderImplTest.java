/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.related.models;

import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntryTable;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.GroupThreadLocal;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Jhosseph Gonzalez
 */
public class ObjectEntryObjectRelatedModelsPredicateProviderImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testObjectEntry1toMObjectRelatedModelsPredicateProviderImplWithCompanyScope()
		throws Exception {

		_testObjectEntry1toMObjectRelatedModelsPredicateProviderImpl(
			ObjectDefinitionConstants.SCOPE_COMPANY);
	}

	@Test
	public void testObjectEntry1toMObjectRelatedModelsPredicateProviderImplWithSiteScope()
		throws Exception {

		_testObjectEntry1toMObjectRelatedModelsPredicateProviderImpl(
			ObjectDefinitionConstants.SCOPE_SITE);
	}

	@Test
	public void testObjectEntryMtoMObjectRelatedModelsPredicateProviderImplWithCompanyScope()
		throws Exception {

		_testObjectEntryMtoMObjectRelatedModelsPredicateProviderImpl(
			ObjectDefinitionConstants.SCOPE_COMPANY);
	}

	@Test
	public void testObjectEntryMtoMObjectRelatedModelsPredicateProviderImplWithSiteScope()
		throws Exception {

		_testObjectEntryMtoMObjectRelatedModelsPredicateProviderImpl(
			ObjectDefinitionConstants.SCOPE_SITE);
	}

	private void _assertPredicateSqlContainsIndexFilters(String predicateSql) {
		Assert.assertTrue(
			predicateSql,
			predicateSql.contains("ObjectEntry.objectDefinitionId"));
		Assert.assertTrue(
			predicateSql, predicateSql.contains("ObjectEntry.companyId"));
		Assert.assertTrue(
			predicateSql, predicateSql.contains("ObjectEntry.groupId"));
	}

	private ObjectDefinition _createObjectDefinition(
		long companyId, String dbTableName, long objectDefinitionId,
		String scope) {

		ObjectDefinition objectDefinition = Mockito.mock(
			ObjectDefinition.class);

		Mockito.when(
			objectDefinition.getCompanyId()
		).thenReturn(
			companyId
		);

		Mockito.when(
			objectDefinition.getDBTableName()
		).thenReturn(
			dbTableName
		);

		Mockito.when(
			objectDefinition.getExtensionDBTableName()
		).thenReturn(
			dbTableName + "x"
		);

		Mockito.when(
			objectDefinition.getObjectDefinitionId()
		).thenReturn(
			objectDefinitionId
		);

		Mockito.when(
			objectDefinition.getPKObjectFieldDBColumnName()
		).thenReturn(
			_PK_OBJECT_FIELD_DB_COLUMN_NAME
		);

		Mockito.when(
			objectDefinition.getPKObjectFieldName()
		).thenReturn(
			_PK_OBJECT_FIELD_NAME
		);

		Mockito.when(
			objectDefinition.getScope()
		).thenReturn(
			scope
		);

		return objectDefinition;
	}

	private ObjectFieldLocalService
		_createObjectFieldLocalServiceWithEmptyFields() {

		ObjectFieldLocalService objectFieldLocalService = Mockito.mock(
			ObjectFieldLocalService.class);

		Mockito.when(
			objectFieldLocalService.getLocalizedObjectFields(Mockito.anyLong())
		).thenReturn(
			Collections.emptyList()
		);

		Mockito.when(
			objectFieldLocalService.getObjectFields(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			Collections.emptyList()
		);

		return objectFieldLocalService;
	}

	private ObjectFieldLocalService
		_createObjectFieldLocalServiceWithRelationshipField(
			long objectDefinitionId, String dbTableName,
			String relationshipColumnName) {

		ObjectFieldLocalService objectFieldLocalService =
			_createObjectFieldLocalServiceWithEmptyFields();

		ObjectField relationshipObjectField = Mockito.mock(ObjectField.class);

		Mockito.when(
			relationshipObjectField.compareBusinessType(
				ObjectFieldConstants.BUSINESS_TYPE_AUTO_INCREMENT)
		).thenReturn(
			false
		);

		Mockito.when(
			relationshipObjectField.getDBColumnNames()
		).thenReturn(
			new String[] {relationshipColumnName}
		);

		Mockito.when(
			relationshipObjectField.getDBType()
		).thenReturn(
			ObjectFieldConstants.DB_TYPE_LONG
		);

		Mockito.when(
			relationshipObjectField.hasInsertValues()
		).thenReturn(
			true
		);

		Mockito.when(
			relationshipObjectField.isLocalized()
		).thenReturn(
			false
		);

		Mockito.when(
			objectFieldLocalService.getObjectFields(
				objectDefinitionId, dbTableName)
		).thenReturn(
			List.of(relationshipObjectField)
		);

		return objectFieldLocalService;
	}

	private ObjectRelationship _createObjectRelationship(
		long objectDefinitionId1, long objectDefinitionId2) {

		ObjectRelationship objectRelationship = Mockito.mock(
			ObjectRelationship.class);

		Mockito.when(
			objectRelationship.getDBTableName()
		).thenReturn(
			"MappingTable_"
		);

		Mockito.when(
			objectRelationship.getName()
		).thenReturn(
			_RELATIONSHIP_NAME
		);

		Mockito.when(
			objectRelationship.getObjectDefinitionId1()
		).thenReturn(
			objectDefinitionId1
		);

		Mockito.when(
			objectRelationship.getObjectDefinitionId2()
		).thenReturn(
			objectDefinitionId2
		);

		Mockito.when(
			objectRelationship.isReverse()
		).thenReturn(
			false
		);

		return objectRelationship;
	}

	private void _testObjectEntry1toMObjectRelatedModelsPredicateProviderImpl(
			String scope)
		throws Exception {

		long objectDefinitionId = RandomTestUtil.randomLong();

		String dbTableName = "Object_" + objectDefinitionId + "_";

		ObjectDefinition objectDefinition = _createObjectDefinition(
			RandomTestUtil.randomLong(), dbTableName, objectDefinitionId,
			scope);
		ObjectFieldLocalService objectFieldLocalService =
			_createObjectFieldLocalServiceWithRelationshipField(
				objectDefinitionId, dbTableName, _RELATIONSHIP_COLUMN_NAME);

		ObjectRelationship objectRelationship = _createObjectRelationship(
			objectDefinitionId, objectDefinitionId);

		Predicate predicate;

		try (SafeCloseable safeCloseable =
				GroupThreadLocal.setGroupIdWithSafeCloseable(
					RandomTestUtil.randomLong())) {

			predicate =
				new ObjectEntry1toMObjectRelatedModelsPredicateProviderImpl(
					objectDefinition, objectFieldLocalService
				).getPredicate(
					objectRelationship,
					ObjectEntryTable.INSTANCE.externalReferenceCode.eq(
						RandomTestUtil.randomString()),
					objectDefinition
				);
		}

		_assertPredicateSqlContainsIndexFilters(predicate.toString());
	}

	private void _testObjectEntryMtoMObjectRelatedModelsPredicateProviderImpl(
			String scope)
		throws Exception {

		long companyId = RandomTestUtil.randomLong();
		long objectDefinitionId = RandomTestUtil.randomLong();
		long childObjectDefinitionId = RandomTestUtil.randomLong();

		ObjectDefinition objectDefinition = _createObjectDefinition(
			companyId, "Object_" + objectDefinitionId + "_", objectDefinitionId,
			ObjectDefinitionConstants.SCOPE_COMPANY);
		ObjectFieldLocalService objectFieldLocalService =
			_createObjectFieldLocalServiceWithEmptyFields();
		ObjectRelationship objectRelationship = _createObjectRelationship(
			objectDefinitionId, childObjectDefinitionId);
		ObjectDefinition relatedObjectDefinition = _createObjectDefinition(
			companyId, "Object_" + childObjectDefinitionId + "_",
			childObjectDefinitionId, scope);

		Predicate predicate;

		try (SafeCloseable safeCloseable =
				GroupThreadLocal.setGroupIdWithSafeCloseable(
					RandomTestUtil.randomLong())) {

			predicate =
				new ObjectEntryMtoMObjectRelatedModelsPredicateProviderImpl(
					objectDefinition, objectFieldLocalService
				).getPredicate(
					objectRelationship,
					ObjectEntryTable.INSTANCE.externalReferenceCode.eq(
						RandomTestUtil.randomString()),
					relatedObjectDefinition
				);
		}

		_assertPredicateSqlContainsIndexFilters(predicate.toString());
	}

	private static final String _PK_OBJECT_FIELD_DB_COLUMN_NAME =
		"objectEntryId";

	private static final String _PK_OBJECT_FIELD_NAME = "c_objectEntryId";

	private static final String _RELATIONSHIP_COLUMN_NAME =
		"r_testRelationship_c_objectEntryId";

	private static final String _RELATIONSHIP_NAME = "testRelationship";

}