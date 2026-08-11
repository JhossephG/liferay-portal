/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.text.extractor;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileVersion;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Jhosseph Gonzalez
 */
public class DLFileEntryTextExtractorUtil {

	public static String extractText(
		DLFileEntry dlFileEntry, DLFileVersion dlFileVersion) {

		DLFileEntryTextExtractor dlFileEntryTextExtractor =
			getDLFileEntryTextExtractor();

		return dlFileEntryTextExtractor.extractText(dlFileEntry, dlFileVersion);
	}

	public static DLFileEntryTextExtractor getDLFileEntryTextExtractor() {
		DLFileEntryTextExtractor dlFileEntryTextExtractor =
			_serviceTracker.getService();

		if (dlFileEntryTextExtractor == null) {
			throw new NullPointerException(
				"DL file entry text extractor is null");
		}

		return dlFileEntryTextExtractor;
	}

	private static final ServiceTracker
		<DLFileEntryTextExtractor, DLFileEntryTextExtractor> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			DLFileEntryTextExtractorUtil.class);

		ServiceTracker<DLFileEntryTextExtractor, DLFileEntryTextExtractor>
			serviceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), DLFileEntryTextExtractor.class,
				null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}