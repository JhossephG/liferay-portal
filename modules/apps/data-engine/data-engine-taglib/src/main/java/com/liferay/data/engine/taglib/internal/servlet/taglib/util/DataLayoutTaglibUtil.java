/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.data.engine.taglib.internal.servlet.taglib.util;

import com.liferay.data.engine.rest.dto.v2_0.DataDefinition;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Gabriel Albuquerque
 * @author Leonardo Barros
 */
public class DataLayoutTaglibUtil {

	public static DataDefinition getDataDefinition(
			long dataDefinitionId, HttpServletRequest httpServletRequest)
		throws Exception {

		Map<Long, DataDefinition> dataDefinitions =
			(Map<Long, DataDefinition>)httpServletRequest.getAttribute(
				_REQUEST_ATTRIBUTE_DATA_DEFINITIONS);

		if (dataDefinitions == null) {
			dataDefinitions = new HashMap<>();

			httpServletRequest.setAttribute(
				_REQUEST_ATTRIBUTE_DATA_DEFINITIONS, dataDefinitions);
		}

		DataDefinition dataDefinition = dataDefinitions.get(dataDefinitionId);

		if (dataDefinition != null) {
			return dataDefinition;
		}

		DataDefinitionResource dataDefinitionResource =
			_getDataDefinitionResource(httpServletRequest);

		dataDefinition = dataDefinitionResource.getDataDefinition(
			dataDefinitionId);

		dataDefinitions.put(dataDefinitionId, dataDefinition);

		return dataDefinition;
	}

	public static JSONArray getFieldTypesJSONArray(
			HttpServletRequest httpServletRequest, Set<String> scopes,
			boolean searchableFieldsDisabled)
		throws Exception {

		Map<String, String> fieldTypesJSONArrayMap = _getFieldTypesJSONArrayMap(
			httpServletRequest);

		String cacheKey = _getFieldTypesJSONArrayCacheKey(
			scopes, searchableFieldsDisabled);

		String fieldTypesJSONArrayString = fieldTypesJSONArrayMap.get(cacheKey);

		if (fieldTypesJSONArrayString != null) {
			return JSONFactoryUtil.createJSONArray(fieldTypesJSONArrayString);
		}

		JSONArray fieldTypesJSONArray = JSONFactoryUtil.createJSONArray();

		try {
			JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
				_getDataDefinitionFieldTypes(httpServletRequest));

			if (SetUtil.isEmpty(scopes)) {
				fieldTypesJSONArray = jsonArray;
			}
			else {
				for (JSONObject jsonObject : (Iterable<JSONObject>)jsonArray) {
					if (ListUtil.exists(
							Arrays.asList(
								StringUtil.split(
									jsonObject.getString("scope"))),
							scopes::contains)) {

						fieldTypesJSONArray.put(jsonObject);

						if (searchableFieldsDisabled) {
							_setFieldIndexTypeNone(
								jsonObject.getJSONObject("settingsContext"));
						}
					}
				}
			}

			fieldTypesJSONArrayString = fieldTypesJSONArray.toString();

			fieldTypesJSONArrayMap.put(cacheKey, fieldTypesJSONArrayString);

			return JSONFactoryUtil.createJSONArray(fieldTypesJSONArrayString);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return fieldTypesJSONArray;
		}
	}

	private static String _getDataDefinitionFieldTypes(
			HttpServletRequest httpServletRequest)
		throws Exception {

		String dataDefinitionFieldTypes =
			(String)httpServletRequest.getAttribute(
				_REQUEST_ATTRIBUTE_DATA_DEFINITION_FIELD_TYPES);

		if (dataDefinitionFieldTypes != null) {
			return dataDefinitionFieldTypes;
		}

		DataDefinitionResource dataDefinitionResource =
			_getDataDefinitionResource(httpServletRequest);

		dataDefinitionFieldTypes =
			dataDefinitionResource.
				getDataDefinitionDataDefinitionFieldFieldTypes();

		httpServletRequest.setAttribute(
			_REQUEST_ATTRIBUTE_DATA_DEFINITION_FIELD_TYPES,
			dataDefinitionFieldTypes);

		return dataDefinitionFieldTypes;
	}

	private static DataDefinitionResource _getDataDefinitionResource(
			HttpServletRequest httpServletRequest)
		throws Exception {

		DataDefinitionResource.Factory dataDefinitionResourceFactory =
			_dataDefinitionResourceFactorySnapshot.get();

		DataDefinitionResource.Builder dataDefinitionResourceBuilder =
			dataDefinitionResourceFactory.create();

		return dataDefinitionResourceBuilder.checkPermissions(
			false
		).httpServletRequest(
			httpServletRequest
		).user(
			PortalUtil.getUser(httpServletRequest)
		).build();
	}

	private static String _getFieldTypesJSONArrayCacheKey(
		Set<String> scopes, boolean searchableFieldsDisabled) {

		if (SetUtil.isEmpty(scopes)) {
			return "*";
		}

		return searchableFieldsDisabled + "#" +
			StringUtil.merge(new TreeSet<>(scopes));
	}

	private static Map<String, String> _getFieldTypesJSONArrayMap(
		HttpServletRequest httpServletRequest) {

		Map<String, String> fieldTypesJSONArrayMap =
			(Map<String, String>)httpServletRequest.getAttribute(
				_REQUEST_ATTRIBUTE_FIELD_TYPES_JSON_ARRAYS);

		if (fieldTypesJSONArrayMap != null) {
			return fieldTypesJSONArrayMap;
		}

		fieldTypesJSONArrayMap = new HashMap<>();

		httpServletRequest.setAttribute(
			_REQUEST_ATTRIBUTE_FIELD_TYPES_JSON_ARRAYS, fieldTypesJSONArrayMap);

		return fieldTypesJSONArrayMap;
	}

	private static void _setFieldIndexTypeNone(JSONObject jsonObject) {
		for (JSONObject pageJSONObject :
				(Iterable<JSONObject>)jsonObject.getJSONArray("pages")) {

			for (JSONObject rowJSONObject :
					(Iterable<JSONObject>)pageJSONObject.getJSONArray("rows")) {

				for (JSONObject columnJSONObject :
						(Iterable<JSONObject>)rowJSONObject.getJSONArray(
							"columns")) {

					for (JSONObject fieldJSONObject :
							(Iterable<JSONObject>)columnJSONObject.getJSONArray(
								"fields")) {

						if (Objects.equals(
								fieldJSONObject.getString("fieldName"),
								"indexType")) {

							fieldJSONObject.put("value", "none");

							return;
						}
					}
				}
			}
		}
	}

	private static final String _REQUEST_ATTRIBUTE_DATA_DEFINITION_FIELD_TYPES =
		DataLayoutTaglibUtil.class.getName() + "#DATA_DEFINITION_FIELD_TYPES";

	private static final String _REQUEST_ATTRIBUTE_DATA_DEFINITIONS =
		DataLayoutTaglibUtil.class.getName() + "#DATA_DEFINITIONS";

	private static final String _REQUEST_ATTRIBUTE_FIELD_TYPES_JSON_ARRAYS =
		DataLayoutTaglibUtil.class.getName() + "#FIELD_TYPES_JSON_ARRAYS";

	private static final Log _log = LogFactoryUtil.getLog(
		DataLayoutTaglibUtil.class);

	private static final Snapshot<DataDefinitionResource.Factory>
		_dataDefinitionResourceFactorySnapshot = new Snapshot<>(
			DataLayoutTaglibUtil.class, DataDefinitionResource.Factory.class);

}