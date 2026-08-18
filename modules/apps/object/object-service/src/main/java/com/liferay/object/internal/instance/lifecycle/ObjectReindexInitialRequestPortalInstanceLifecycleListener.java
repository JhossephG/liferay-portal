/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.instance.lifecycle;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectFolder;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFolderLocalService;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.instance.lifecycle.EveryNodeEveryStartup;
import com.liferay.portal.instance.lifecycle.InitialRequestPortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.index.IndexStatusManager;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jhosseph Gonzalez
 */
@Component(service = PortalInstanceLifecycleListener.class)
public class ObjectReindexInitialRequestPortalInstanceLifecycleListener
	extends InitialRequestPortalInstanceLifecycleListener
	implements EveryNodeEveryStartup {

	@Activate
	@Override
	protected void activate(BundleContext bundleContext) {
		super.activate(bundleContext);
	}

	@Override
	protected void doPortalInstanceRegistered(long companyId) throws Exception {
		_reindex(
			companyId,
			() -> _objectDefinitionLocalService.getObjectDefinitionsCount(
				companyId),
			_objectDefinitionIndexer);
		_reindex(
			companyId,
			() -> _objectFolderLocalService.getObjectFoldersCount(companyId),
			_objectFolderIndexer);
	}

	private void _reindex(
		long companyId, UnsafeSupplier<Integer, Exception> countUnsafeSupplier,
		Indexer<?> indexer) {

		String className = indexer.getClassName();

		try {
			if (_indexStatusManager.isIndexReadOnly()) {
				if (_log.isInfoEnabled()) {
					_log.info(
						StringBundler.concat(
							"Skipped the reindex of ", className,
							" for company ", companyId,
							" because the search index is read only"));
				}

				return;
			}

			int count = countUnsafeSupplier.get();

			if (count == 0) {
				return;
			}

			SearchContext searchContext = new SearchContext();

			searchContext.setCompanyId(companyId);
			searchContext.setEntryClassNames(new String[] {className});

			if (indexer.searchCount(searchContext) > 0) {
				return;
			}

			indexer.reindexCompany(companyId);

			if (_log.isInfoEnabled()) {
				_log.info(
					StringBundler.concat(
						"Reindexed ", className, " for company ", companyId,
						" because the search index had no documents"));
			}
		}
		catch (Exception exception) {
			_log.error(
				StringBundler.concat(
					"Unable to reindex ", className, " for company ",
					companyId),
				exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectReindexInitialRequestPortalInstanceLifecycleListener.class);

	@Reference
	private IndexStatusManager _indexStatusManager;

	@Reference(
		target = "(indexer.class.name=com.liferay.object.model.ObjectDefinition)"
	)
	private Indexer<ObjectDefinition> _objectDefinitionIndexer;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference(
		target = "(indexer.class.name=com.liferay.object.model.ObjectFolder)"
	)
	private Indexer<ObjectFolder> _objectFolderIndexer;

	@Reference
	private ObjectFolderLocalService _objectFolderLocalService;

}