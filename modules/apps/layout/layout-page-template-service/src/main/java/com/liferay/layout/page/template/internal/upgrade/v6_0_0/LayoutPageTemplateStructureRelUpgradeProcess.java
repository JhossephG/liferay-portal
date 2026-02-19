/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.internal.upgrade.v6_0_0;

import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jhosseph Gonzalez
 */
public class LayoutPageTemplateStructureRelUpgradeProcess
	extends UpgradeProcess {

	public LayoutPageTemplateStructureRelUpgradeProcess() {
	}

	@Override
	protected void doUpgrade() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			processConcurrently(
				StringBundler.concat(
					"select ctCollectionId, lPageTemplateStructureRelId, ",
					"companyId, data_ from LayoutPageTemplateStructureRel ",
					"where (data_ like '%",
					_INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX,
					"OneToManyObjectRelationshipRelatedInfoCollection",
					"Provider%' or data_ like '%",
					_INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX,
					"ManyToManyObjectRelationshipRelatedInfoCollection",
					"Provider%')"),
				"update LayoutPageTemplateStructureRel set data_ = ? " +
					"where ctCollectionId = ? and lPageTemplateStructureRelId = ?",
				resultSet -> new Object[] {
					resultSet.getLong("ctCollectionId"),
					resultSet.getLong("lPageTemplateStructureRelId"),
					resultSet.getLong("companyId"),
					GetterUtil.getString(resultSet.getString("data_"))
				},
				(values, preparedStatement) -> {
					String data_ = (String)values[3];

					if (data_.isEmpty()) {
						return;
					}

					LayoutStructure layoutStructure = LayoutStructure.of(data_);

					boolean changed = false;

					for (CollectionStyledLayoutStructureItem item :
							layoutStructure.
								getCollectionStyledLayoutStructureItems()) {

						JSONObject collectionJSONObject =
							item.getCollectionJSONObject();

						if (collectionJSONObject == null) {
							continue;
						}

						String key = collectionJSONObject.getString("key");

						if (!StringUtil.startsWith(
								key,
								_INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX)) {

							continue;
						}

						Matcher matcher =
							_manyToManyObjectRelationshipRelatedInfoCollectionProviderKeyPattern.
								matcher(key);

						if (!matcher.matches()) {
							matcher =
								_oneToManyObjectRelationshipRelatedInfoCollectionProviderKeyPattern.
									matcher(key);
						}

						if (!matcher.matches()) {
							continue;
						}

						String sourceItemType = collectionJSONObject.getString(
							"sourceItemType");

						if (Validator.isNull(sourceItemType)) {
							sourceItemType = collectionJSONObject.getString(
								"itemType");
						}

						if (Validator.isNull(sourceItemType)) {
							continue;
						}

						Matcher objectDefinitionClassNameMatcher =
							_objectDefinitionClassNamePattern.matcher(
								sourceItemType);

						if (!objectDefinitionClassNameMatcher.matches()) {
							continue;
						}

						long companyId = (Long)values[2];

						String objectDefinitionClassName =
							_getObjectDefinitionClassName(
								companyId, sourceItemType);

						if (Validator.isNull(objectDefinitionClassName)) {
							continue;
						}

						String newKey = _getKey(
							matcher, objectDefinitionClassName);

						if (Objects.equals(newKey, key)) {
							continue;
						}

						collectionJSONObject.put("key", newKey);

						changed = true;
					}

					if (!changed) {
						return;
					}

					JSONObject jsonObject = layoutStructure.toJSONObject();

					preparedStatement.setString(1, jsonObject.toString());
					preparedStatement.setLong(2, (Long)values[0]);
					preparedStatement.setLong(3, (Long)values[1]);

					preparedStatement.addBatch();
				},
				null);
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private String _fetchObjectDefinitionClassName(
			Connection connection, long companyId, String sourceItemType)
		throws Exception {

		String sql =
			"select className from ObjectDefinition where companyId = ? and className = ?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setLong(1, companyId);
			ps.setString(2, sourceItemType);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString(1);
				}
			}
		}

		return null;
	}

	private String _fetchObjectDefinitionClassNameById(
			Connection connection, long objectDefinitionId)
		throws Exception {

		String sql =
			"select className from ObjectDefinition where objectDefinitionId = ?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setLong(1, objectDefinitionId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString(1);
				}
			}
		}

		return null;
	}

	private long _fetchObjectDefinitionIdByOldClassName(
			Connection connection, long companyId, String sourceItemType)
		throws Exception {

		String sql =
			"select objectDefinitionId from ObjectDefinitionSetting " +
				"where companyId = ? and name = ? and value = ?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setLong(1, companyId);
			ps.setString(2, "oldClassName");
			ps.setString(3, sourceItemType);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
			}
		}

		return 0;
	}

	private String _getKey(Matcher matcher, String objectDefinitionClassName) {
		return StringBundler.concat(
			matcher.group(1), objectDefinitionClassName, "_", matcher.group(4));
	}

	private String _getObjectDefinitionClassName(
			long companyId, String sourceItemType)
		throws Exception {

		try (Connection connection = DataAccess.getConnection()) {
			String className = _fetchObjectDefinitionClassName(
				connection, companyId, sourceItemType);

			if (className != null) {
				return className;
			}

			long objectDefinitionId = _fetchObjectDefinitionIdByOldClassName(
				connection, companyId, sourceItemType);

			if (objectDefinitionId <= 0) {
				return null;
			}

			return _fetchObjectDefinitionClassNameById(
				connection, objectDefinitionId);
		}
	}

	private static final String _INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX =
		"com.liferay.object.internal.info.collection.provider.";

	private static final String
		_INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX_REGEX =
			StringBundler.concat(
				"com\\.liferay\\.object\\.internal\\.info\\.collection\\.",
				"provider\\.");

	private static final String _OBJECT_DEFINITION_CLASS_NAME_REGEX =
		StringBundler.concat(
			"com\\.liferay\\.object\\.model\\.ObjectDefinition#",
			"[A-Za-z]\\d[A-Za-z]\\d");

	private static final Pattern
		_manyToManyObjectRelationshipRelatedInfoCollectionProviderKeyPattern =
			Pattern.compile(
				StringBundler.concat(
					"^(", _INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX_REGEX,
					"ManyToManyObjectRelationshipRelatedInfoCollectionProvider",
					"_)(\\d+)_(.+)_([A-Za-z0-9_]+)$"));
	private static final Pattern _objectDefinitionClassNamePattern =
		Pattern.compile(
			StringBundler.concat(
				"^", _OBJECT_DEFINITION_CLASS_NAME_REGEX, "$"));
	private static final Pattern
		_oneToManyObjectRelationshipRelatedInfoCollectionProviderKeyPattern =
			Pattern.compile(
				StringBundler.concat(
					"^(", _INFO_COLLECTION_PROVIDER_CLASS_NAME_PREFIX_REGEX,
					"OneToManyObjectRelationshipRelatedInfoCollectionProvider",
					"_)(\\d+)_(.+)_([A-Za-z0-9_]+)$"));

}