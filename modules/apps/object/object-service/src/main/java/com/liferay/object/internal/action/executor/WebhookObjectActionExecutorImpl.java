/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.action.executor;

import com.liferay.object.action.executor.ObjectActionExecutor;
import com.liferay.object.constants.ObjectActionExecutorConstants;
import com.liferay.object.exception.ObjectActionParametersException;
import com.liferay.object.internal.configuration.WebhookObjectActionExecutorImplConfiguration;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.InetAddressUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.net.URI;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(service = ObjectActionExecutor.class)
public class WebhookObjectActionExecutorImpl implements ObjectActionExecutor {

	@Override
	public void execute(
			long companyId, long objectActionId,
			UnicodeProperties parametersUnicodeProperties,
			JSONObject payloadJSONObject, long userId)
		throws Exception {

		String url = parametersUnicodeProperties.get("url");

		if (!_isAllowedURL(companyId, url)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to send the webhook to disallowed URL " + url);
			}

			throw new ObjectActionParametersException(
				"The URL is not allowed for object action " + objectActionId);
		}

		Http.Options options = new Http.Options();

		options.addHeader(
			HttpHeaders.CONTENT_TYPE, ContentTypes.APPLICATION_JSON);
		options.addHeader(
			"x-api-key", parametersUnicodeProperties.get("secret"));
		options.setBody(
			payloadJSONObject.toString(), ContentTypes.APPLICATION_JSON,
			StringPool.UTF8);
		options.setFollowRedirects(false);
		options.setLocation(url);
		options.setPost(true);

		_http.URLtoString(options);
	}

	@Override
	public String getKey() {
		return ObjectActionExecutorConstants.KEY_WEBHOOK;
	}

	private boolean _isAllowedURL(long companyId, String url) {
		try {
			URI uri = new URI(url);

			String scheme = StringUtil.toLowerCase(uri.getScheme());

			if (!Objects.equals(scheme, Http.HTTP) &&
				!Objects.equals(scheme, Http.HTTPS)) {

				return false;
			}

			String host = uri.getHost();

			if (Validator.isNull(host)) {
				return false;
			}

			WebhookObjectActionExecutorImplConfiguration
				webhookObjectActionExecutorImplConfiguration =
					_configurationProvider.getCompanyConfiguration(
						WebhookObjectActionExecutorImplConfiguration.class,
						companyId);

			String[] webhookURLHostsAllowed =
				webhookObjectActionExecutorImplConfiguration.
					webhookURLHostsAllowed();

			if (ArrayUtil.isNotEmpty(webhookURLHostsAllowed) &&
				!ArrayUtil.contains(webhookURLHostsAllowed, host, true)) {

				return false;
			}

			if (webhookObjectActionExecutorImplConfiguration.
					webhookURLLocalNetworkAccessEnabled()) {

				return true;
			}

			return !InetAddressUtil.isLocalInetAddress(
				InetAddressUtil.getInetAddressByName(host));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return false;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WebhookObjectActionExecutorImpl.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Http _http;

}