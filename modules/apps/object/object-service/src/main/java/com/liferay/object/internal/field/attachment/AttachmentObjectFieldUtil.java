/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.field.attachment;

import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.field.setting.util.ObjectFieldSettingUtil;
import com.liferay.object.model.ObjectField;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Objects;

/**
 * @author Jhosseph Gonzalez
 */
public class AttachmentObjectFieldUtil {

	public static boolean isUserComputerAttachmentObjectField(
		ObjectField objectField) {

		if (!objectField.compareBusinessType(
				ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

			return false;
		}

		String fileSource = ObjectFieldSettingUtil.getValue(
			ObjectFieldSettingConstants.NAME_FILE_SOURCE, objectField);

		if (Objects.equals(
				fileSource,
				ObjectFieldSettingConstants.
					VALUE_USER_COMPUTER_TO_CMS_BASIC_DOCUMENT)) {

			return true;
		}

		if (!Objects.equals(
				fileSource,
				ObjectFieldSettingConstants.
					VALUE_USER_COMPUTER_TO_DOCS_AND_MEDIA)) {

			return false;
		}

		return !GetterUtil.getBoolean(
			ObjectFieldSettingUtil.getValue(
				ObjectFieldSettingConstants.NAME_SHOW_FILES_IN_LIBRARY,
				objectField));
	}

}