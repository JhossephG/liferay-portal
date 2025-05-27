/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.web.internal.object.entries.portlet.action;

import com.liferay.object.model.ObjectEntry;
<<<<<<< HEAD
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectEntryService;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
=======
import com.liferay.object.rest.resource.v1_0.ObjectEntryResource;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
>>>>>>> d562a42 (LPD-44845 feature: create ExpireObjectEntryMVCActionCommand and expire action in ViewObjectEntriesDisplayContext)
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import jakarta.portlet.ActionRequest;
import jakarta.portlet.ActionResponse;

<<<<<<< HEAD
=======
import java.util.Objects;

>>>>>>> d562a42 (LPD-44845 feature: create ExpireObjectEntryMVCActionCommand and expire action in ViewObjectEntriesDisplayContext)
/**
 * @author Jhosseph Gonzalez
 */
public class ExpireObjectEntryMVCActionCommand extends BaseMVCActionCommand {

	public ExpireObjectEntryMVCActionCommand(
		ObjectEntryLocalService objectEntryLocalService,
<<<<<<< HEAD
		ObjectEntryService objectEntryService) {

		_objectEntryLocalService = objectEntryLocalService;
		_objectEntryService = objectEntryService;
=======
		ObjectEntryResource.Factory objectEntryResourceFactory) {

		_objectEntryLocalService = objectEntryLocalService;
		_objectEntryResourceFactory = objectEntryResourceFactory;
>>>>>>> d562a42 (LPD-44845 feature: create ExpireObjectEntryMVCActionCommand and expire action in ViewObjectEntriesDisplayContext)
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPD-17564")) {
			throw new UnsupportedOperationException();
		}

<<<<<<< HEAD
		long objectEntryId = ParamUtil.getLong(actionRequest, "objectEntryId");

		int status = _objectEntryLocalService.getObjectEntry(
			objectEntryId
		).getStatus();

		if ((status == WorkflowConstants.STATUS_DRAFT) ||
			(status == WorkflowConstants.STATUS_PENDING)) {

			return;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			ObjectEntry.class.getName(), actionRequest);

		_objectEntryService.expireObjectEntry(
			themeDisplay.getUserId(), objectEntryId, serviceContext);
	}

	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectEntryService _objectEntryService;
=======
		ObjectEntryResource.Builder builder =
			_objectEntryResourceFactory.create();

		long objectEntryId = ParamUtil.getLong(actionRequest, "objectEntryId");

		ObjectEntry serviceBuilderObjectEntry =
			_objectEntryLocalService.getObjectEntry(objectEntryId);

		if (!Objects.equals(
				serviceBuilderObjectEntry.getStatus(),
				WorkflowConstants.STATUS_DRAFT) &&
			!Objects.equals(
				serviceBuilderObjectEntry.getStatus(),
				WorkflowConstants.STATUS_PENDING)) {

			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			ObjectEntryResource objectEntryResource = builder.user(
				themeDisplay.getUser()
			).preferredLocale(
				themeDisplay.getLocale()
			).build();

			objectEntryResource.patchExpireObjectEntry(objectEntryId);
		}
	}

	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectEntryResource.Factory _objectEntryResourceFactory;
>>>>>>> d562a42 (LPD-44845 feature: create ExpireObjectEntryMVCActionCommand and expire action in ViewObjectEntriesDisplayContext)

}