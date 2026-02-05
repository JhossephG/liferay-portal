/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.internal.upgrade.v5_8_0;


import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Jhosseph Gonzalez
 */
public class LayoutPageTemplateStructureRelUpgradeProcess extends
	UpgradeProcess {
	@Override
	protected void doUpgrade() throws Exception {

		String sql = StringBundler.concat(
			"select ctCollectionId, lPageTemplateStructureRelId, ",
			"data_ from LayoutPageTemplateStructureRel where data_ ",
			"like '%", _OLD_CLASS_NAME, "%' ");
	}


}
