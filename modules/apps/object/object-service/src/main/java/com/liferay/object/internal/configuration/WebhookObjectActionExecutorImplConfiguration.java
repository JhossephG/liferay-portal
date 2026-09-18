/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Jhosseph Gonzalez
 */
@ExtendedObjectClassDefinition(
	category = "object", scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.object.internal.configuration.WebhookObjectActionExecutorImplConfiguration",
	localization = "content/Language",
	name = "webhook-object-action-executor-configuration-name"
)
public interface WebhookObjectActionExecutorImplConfiguration {

	@Meta.AD(
		description = "webhook-url-hosts-allowed-help",
		name = "webhook-url-hosts-allowed", required = false
	)
	public String[] webhookURLHostsAllowed();

	@Meta.AD(
		deflt = "false",
		description = "webhook-url-local-network-access-enabled-help",
		name = "webhook-url-local-network-access-enabled", required = false
	)
	public boolean webhookURLLocalNetworkAccessEnabled();

}