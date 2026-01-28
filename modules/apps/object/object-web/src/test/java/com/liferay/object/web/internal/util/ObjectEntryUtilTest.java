/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.web.internal.util;

import com.liferay.headless.delivery.dto.v1_0.Creator;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectEntryLocalServiceUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Lourdes Fernández Besada
 */
public class ObjectEntryUtilTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testToObjectEntryMapsCreatorAndDates() throws Exception {
		long userId = RandomTestUtil.randomLong();
		String userName = RandomTestUtil.randomString();

		Date createDate = new Date();
		Date modifiedDate = new Date(createDate.getTime() + 1000);

		Creator creator = new Creator();

		creator.setId(userId);
		creator.setName(userName);

		com.liferay.object.rest.dto.v1_0.ObjectEntry dtoObjectEntry =
			new com.liferay.object.rest.dto.v1_0.ObjectEntry();

		dtoObjectEntry.setCreator(creator);
		dtoObjectEntry.setDateCreated(createDate);
		dtoObjectEntry.setDateModified(modifiedDate);

		ObjectDefinition objectDefinition = Mockito.mock(ObjectDefinition.class);

		Mockito.when(
			objectDefinition.getObjectDefinitionId()
		).thenReturn(
			RandomTestUtil.randomLong()
		);

		ObjectEntry objectEntry = _createObjectEntryMock();

		try (MockedStatic<ObjectEntryLocalServiceUtil>
				objectEntryLocalServiceUtilMockedStatic =
					Mockito.mockStatic(ObjectEntryLocalServiceUtil.class)) {

			objectEntryLocalServiceUtilMockedStatic.when(
				() -> ObjectEntryLocalServiceUtil.createObjectEntry(0L)
			).thenReturn(
				objectEntry
			);

			ObjectEntry convertedObjectEntry = ObjectEntryUtil.toObjectEntry(
				objectDefinition, dtoObjectEntry);

			Assert.assertEquals(userId, convertedObjectEntry.getUserId());
			Assert.assertEquals(userName, convertedObjectEntry.getUserName());
			Assert.assertEquals(
				createDate, convertedObjectEntry.getCreateDate());
			Assert.assertEquals(
				modifiedDate, convertedObjectEntry.getModifiedDate());
		}
	}

	private ObjectEntry _createObjectEntryMock() {
		Map<String, Object> values = new HashMap<>();

		ObjectEntry objectEntry = Mockito.mock(ObjectEntry.class);

		Mockito.doAnswer(
			invocation -> {
				values.put("userId", invocation.getArgument(0, Long.class));

				return null;
			}
		).when(
			objectEntry
		).setUserId(
			Mockito.anyLong()
		);

		Mockito.doAnswer(
			invocation -> {
				values.put(
					"userName", invocation.getArgument(0, String.class));

				return null;
			}
		).when(
			objectEntry
		).setUserName(
			Mockito.anyString()
		);

		Mockito.doAnswer(
			invocation -> {
				values.put("createDate", invocation.getArgument(0, Date.class));

				return null;
			}
		).when(
			objectEntry
		).setCreateDate(
			Mockito.any(Date.class)
		);

		Mockito.doAnswer(
			invocation -> {
				values.put(
					"modifiedDate", invocation.getArgument(0, Date.class));

				return null;
			}
		).when(
			objectEntry
		).setModifiedDate(
			Mockito.any(Date.class)
		);

		Mockito.when(
			objectEntry.getUserId()
		).thenAnswer(
			invocation -> (Long)values.get("userId")
		);

		Mockito.when(
			objectEntry.getUserName()
		).thenAnswer(
			invocation -> (String)values.get("userName")
		);

		Mockito.when(
			objectEntry.getCreateDate()
		).thenAnswer(
			invocation -> (Date)values.get("createDate")
		);

		Mockito.when(
			objectEntry.getModifiedDate()
		).thenAnswer(
			invocation -> (Date)values.get("modifiedDate")
		);

		return objectEntry;
	}

}
