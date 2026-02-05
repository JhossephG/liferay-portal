/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.internal.upgrade.v5_8_0;

import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectDefinitionSettingConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectDefinitionSetting;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectDefinitionSettingLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jhosseph Gonzalez
 */
public class LayoutPageTemplateStructureRelUpgradeProcess
	extends UpgradeProcess {

	public LayoutPageTemplateStructureRelUpgradeProcess(
		CompanyLocalService companyLocalService,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectDefinitionSettingLocalService
			objectDefinitionSettingLocalService) {

		_companyLocalService = companyLocalService;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectDefinitionSettingLocalService =
			objectDefinitionSettingLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		_companyLocalService.forEachCompany(
			company -> {
				try (LoggingTimer loggingTimer = new LoggingTimer()) {
					processConcurrently(
						StringBundler.concat(
							"select ctCollectionId, lPageTemplateStructu",
							"reRelId, data_ from LayoutPageTemplateStructure",
							"Rel where companyId = ", company.getCompanyId(),
							" and (data_ like '%", _PACKAGE_PATH,
							"OneToManyObjectRelationshipRelatedInfoCollection",
							"Provider%' or data_ like '%", _PACKAGE_PATH,
							"ManyToManyObjectRelationshipRelatedInfoCollection",
							"Provider%')"),
						"update LayoutPageTemplateStructureRel set data_ =" +
							"? where ctCollectionId = ? and " +
								"lPageTemplateStructureRelId = ?",
						resultSet -> new Object[] {
							resultSet.getLong("ctCollectionId"),
							resultSet.getLong("lPageTemplateStructureRelId"),
							GetterUtil.getString(resultSet.getString("data_"))
						},
						(values, preparedStatement) -> {
							String data_ = (String)values[2];

							if (data_.isEmpty()) {
								return;
							}

							LayoutStructure layoutStructure =
								LayoutStructure.of(data_);

							boolean changed = false;

							for (CollectionStyledLayoutStructureItem
									collectionStyledLayoutStructureItem :
										layoutStructure.
											getCollectionStyledLayoutStructureItems()) {

								JSONObject collectionJSONObject =
									collectionStyledLayoutStructureItem.
										getCollectionJSONObject();

								if (collectionJSONObject == null) {
									continue;
								}

								String key = collectionJSONObject.getString(
									"key");

								if (Validator.isNull(key) ||
									!StringUtil.startsWith(
										key, _PACKAGE_PATH)) {

									continue;
								}

								Matcher matcher =
									_manyToManyRelatedInfoCollectionProviderKeyPattern.
										matcher(key);

								if (!matcher.matches()) {
									matcher =
										_oneToManyRelatedInfoCollectionProviderKeyPattern.
											matcher(key);

									if (!matcher.matches()) {
										continue;
									}
								}

								String objectDefinitionOldClassName =
									collectionJSONObject.getString(
										"sourceItemType");

								if (Validator.isNull(
										objectDefinitionOldClassName)) {

									objectDefinitionOldClassName =
										collectionJSONObject.getString(
											"itemType");
								}

								if (Validator.isNull(
										objectDefinitionOldClassName) ||
									!ObjectDefinitionConstants.classNamePattern.
										matcher(
											objectDefinitionOldClassName
										).matches()) {

									continue;
								}

								ObjectDefinition objectDefinition =
									_getObjectDefinition(
										company.getCompanyId(),
										objectDefinitionOldClassName);

								if (objectDefinition == null) {
									continue;
								}

								String newKey = _getKey(
									objectDefinition, matcher);

								if (!key.equals(newKey)) {
									collectionJSONObject.put("key", newKey);
									changed = true;
								}
							}

							if (!changed) {
								return;
							}

							JSONObject jsonObject =
								layoutStructure.toJSONObject();

							preparedStatement.setString(
								1, jsonObject.toString());

							preparedStatement.setLong(2, (Long)values[0]);
							preparedStatement.setLong(3, (Long)values[1]);

							preparedStatement.addBatch();
						},
						null);
				}
				catch (Exception exception) {
					throw new RuntimeException(exception);
				}
			});
	}

	private String _getKey(ObjectDefinition objectDefinition, Matcher matcher) {
		return StringBundler.concat(
			matcher.group(1), objectDefinition.getClassName(),
			matcher.group(3));
	}

	private ObjectDefinition _getObjectDefinition(
		long companyId, String objectDefinitionSettingValue) {

		ObjectDefinitionSetting objectDefinitionSetting =
			_objectDefinitionSettingLocalService.fetchObjectDefinitionSetting(
				companyId, ObjectDefinitionSettingConstants.NAME_OLD_CLASS_NAME,
				objectDefinitionSettingValue);

		if (objectDefinitionSetting == null) {
			return null;
		}

		return _objectDefinitionLocalService.fetchObjectDefinition(
			objectDefinitionSetting.getObjectDefinitionId());
	}

	private static final String _PACKAGE_PATH =
		"com.liferay.object.internal.info.collection.provider.";

	private static final Pattern
		_manyToManyRelatedInfoCollectionProviderKeyPattern = Pattern.compile(
			StringBundler.concat(
				"^(com\\.liferay\\.object\\.internal\\.info\\.collection",
				"\\.provider\\.ManyToManyObjectRelationship",
				"RelatedInfoCollectionProvider_)",
				"(com\\.liferay\\.object\\.model\\.ObjectDefinition#[A-Za-z]",
				"\\d[A-Za-z]\\d)(_[A-Za-z0-9_]+)$"));
	private static final Pattern
		_oneToManyRelatedInfoCollectionProviderKeyPattern = Pattern.compile(
			StringBundler.concat(
				"^(com\\.liferay\\.object\\.internal\\.info\\.collection",
				"\\.provider\\.OneToManyObjectRelationship",
				"RelatedInfoCollectionProvider_)",
				"(com\\.liferay\\.object\\.model\\.ObjectDefinition#[A-Za-z]",
				"\\d[A-Za-z]\\d)(_[A-Za-z0-9_]+)$"));

	private final CompanyLocalService _companyLocalService;
	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectDefinitionSettingLocalService
		_objectDefinitionSettingLocalService;

}