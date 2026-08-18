/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.instance.lifecycle.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectFolder;
import com.liferay.portal.instance.lifecycle.EveryNodeEveryStartup;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.search.IndexStatusManagerThreadLocal;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jhosseph Gonzalez
 */
@RunWith(Arquillian.class)
public class ObjectReindexInitialRequestPortalInstanceLifecycleListenerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testPortalInstanceRegistered() throws Exception {
		Assert.assertTrue(
			_portalInstanceLifecycleListener instanceof EveryNodeEveryStartup);

		long companyId = TestPropsValues.getCompanyId();

		// Object definition

		_testPortalInstanceRegistered(
			ObjectDefinition.class.getName(), companyId);

		// Object folder

		_testPortalInstanceRegistered(ObjectFolder.class.getName(), companyId);

		// Read only search index

		_testPortalInstanceRegisteredWithReadOnlySearchIndex(
			ObjectDefinition.class.getName(), companyId);
	}

	private long _deleteEntityDocumentsAndGetSearchCount(
			String className, long companyId)
		throws Exception {

		long searchCount = _getSearchCount(className, companyId);

		Assert.assertTrue(searchCount > 0);

		_indexWriterHelper.deleteEntityDocuments(companyId, className, true);

		Assert.assertEquals(0, _getSearchCount(className, companyId));

		return searchCount;
	}

	private long _getSearchCount(String className, long companyId)
		throws Exception {

		Indexer<?> indexer = IndexerRegistryUtil.nullSafeGetIndexer(className);

		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(companyId);
		searchContext.setEntryClassNames(new String[] {className});

		return indexer.searchCount(searchContext);
	}

	private void _testPortalInstanceRegistered(String className, long companyId)
		throws Exception {

		long expectedCount = _deleteEntityDocumentsAndGetSearchCount(
			className, companyId);

		_portalInstanceLifecycleListener.portalInstanceRegistered(
			_companyLocalService.getCompany(companyId));

		Assert.assertEquals(
			expectedCount, _getSearchCount(className, companyId));
	}

	private void _testPortalInstanceRegisteredWithReadOnlySearchIndex(
			String className, long companyId)
		throws Exception {

		long expectedCount = _deleteEntityDocumentsAndGetSearchCount(
			className, companyId);

		IndexStatusManagerThreadLocal.setIndexReadOnly(true);

		_portalInstanceLifecycleListener.portalInstanceRegistered(
			_companyLocalService.getCompany(companyId));

		IndexStatusManagerThreadLocal.setIndexReadOnly(false);

		Assert.assertEquals(0, _getSearchCount(className, companyId));

		_portalInstanceLifecycleListener.portalInstanceRegistered(
			_companyLocalService.getCompany(companyId));

		Assert.assertEquals(
			expectedCount, _getSearchCount(className, companyId));
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private IndexWriterHelper _indexWriterHelper;

	@Inject(
		filter = "component.name=com.liferay.object.internal.instance.lifecycle.ObjectReindexInitialRequestPortalInstanceLifecycleListener"
	)
	private PortalInstanceLifecycleListener _portalInstanceLifecycleListener;

}