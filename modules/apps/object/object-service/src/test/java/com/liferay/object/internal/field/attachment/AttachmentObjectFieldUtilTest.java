/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.field.attachment;

import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectFieldSetting;
import com.liferay.object.model.impl.ObjectFieldImpl;
import com.liferay.object.model.impl.ObjectFieldSettingImpl;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Jhosseph Gonzalez
 */
public class AttachmentObjectFieldUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testIsUserComputerAttachmentObjectField() {
		Assert.assertFalse(
			AttachmentObjectFieldUtil.isUserComputerAttachmentObjectField(
				_createObjectField(
					ObjectFieldConstants.BUSINESS_TYPE_TEXT,
					ObjectFieldSettingConstants.
						VALUE_USER_COMPUTER_TO_CMS_BASIC_DOCUMENT,
					false)));

		Assert.assertFalse(
			AttachmentObjectFieldUtil.isUserComputerAttachmentObjectField(
				_createObjectField(
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT,
					ObjectFieldSettingConstants.VALUE_DOCS_AND_MEDIA, false)));

		Assert.assertTrue(
			AttachmentObjectFieldUtil.isUserComputerAttachmentObjectField(
				_createObjectField(
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT,
					ObjectFieldSettingConstants.
						VALUE_USER_COMPUTER_TO_CMS_BASIC_DOCUMENT,
					false)));

		Assert.assertTrue(
			AttachmentObjectFieldUtil.isUserComputerAttachmentObjectField(
				_createObjectField(
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT,
					ObjectFieldSettingConstants.
						VALUE_USER_COMPUTER_TO_DOCS_AND_MEDIA,
					false)));

		Assert.assertFalse(
			AttachmentObjectFieldUtil.isUserComputerAttachmentObjectField(
				_createObjectField(
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT,
					ObjectFieldSettingConstants.
						VALUE_USER_COMPUTER_TO_DOCS_AND_MEDIA,
					true)));
	}

	private ObjectField _createObjectField(
		String businessType, String fileSource, boolean showFilesInLibrary) {

		ObjectField objectField = new ObjectFieldImpl();

		objectField.setBusinessType(businessType);

		ObjectFieldSetting fileSourceObjectFieldSetting =
			new ObjectFieldSettingImpl();

		fileSourceObjectFieldSetting.setName(
			ObjectFieldSettingConstants.NAME_FILE_SOURCE);
		fileSourceObjectFieldSetting.setValue(fileSource);

		ObjectFieldSetting showFilesInLibraryObjectFieldSetting =
			new ObjectFieldSettingImpl();

		showFilesInLibraryObjectFieldSetting.setName(
			ObjectFieldSettingConstants.NAME_SHOW_FILES_IN_LIBRARY);
		showFilesInLibraryObjectFieldSetting.setValue(
			String.valueOf(showFilesInLibrary));

		objectField.setObjectFieldSettings(
			Arrays.asList(
				fileSourceObjectFieldSetting,
				showFilesInLibraryObjectFieldSetting));

		return objectField;
	}

}