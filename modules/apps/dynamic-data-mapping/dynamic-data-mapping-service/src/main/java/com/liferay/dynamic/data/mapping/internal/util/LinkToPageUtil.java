/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.internal.util;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Jhosseph Gonzalez
 */
public class LinkToPageUtil {

	public static Layout fetchLayout(
		long companyId, long defaultGroupId, JSONObject jsonObject) {

		if (jsonObject == null) {
			return null;
		}

		long groupId = _resolveGroupId(
			companyId, defaultGroupId, jsonObject.getLong("groupId"),
			jsonObject);

		if (groupId == 0) {
			return null;
		}

		String externalReferenceCode = jsonObject.getString(
			"externalReferenceCode");
		Layout layout = null;

		if (Validator.isNotNull(externalReferenceCode)) {

			layout = LayoutLocalServiceUtil.fetchLayoutByExternalReferenceCode(
				externalReferenceCode, groupId);
		}

		if (layout != null) {
			return layout;
		}

		long layoutId = jsonObject.getLong("layoutId");
		boolean privateLayout = jsonObject.getBoolean("privateLayout");

		if (layoutId > 0) {
			layout = LayoutLocalServiceUtil.fetchLayout(
				groupId, privateLayout, layoutId);
		}

		if (layout != null) {
			return layout;
		}

		String uuid = jsonObject.getString("id");

		if (Validator.isNotNull(uuid)) {
			return LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
				uuid, groupId, privateLayout);
		}

		return null;
	}

	private static long _resolveGroupId(
		long companyId, long defaultGroupId, long groupId,
		JSONObject jsonObject) {

		String groupExternalReferenceCode = jsonObject.getString(
			"groupExternalReferenceCode");

		if (Validator.isNotNull(groupExternalReferenceCode) &&
			(companyId > 0)) {

			Group group =
				GroupLocalServiceUtil.fetchGroupByExternalReferenceCode(
					groupExternalReferenceCode, companyId);

			if (group != null) {
				return group.getGroupId();
			}
		}

		if (defaultGroupId > 0) {
			return defaultGroupId;
		}

		return groupId;
	}

}
