/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.petra.lang.SafeCloseable;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.test.util.FIPSModeTestUtil;
import com.liferay.portal.kernel.test.util.PropsValuesTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Dante Wang
 */
public class URLUtilTest {

	@BeforeClass
	public static void setUpClass() {
		_propsUtilMockedStatic.when(
			() -> PropsUtil.get(PropsKeys.DNS_SECURITY_ADDRESS_TIMEOUT_SECONDS)
		).thenReturn(
			String.valueOf(2)
		);

		_propsUtilMockedStatic.when(
			() -> PropsUtil.get(PropsKeys.DNS_SECURITY_THREAD_LIMIT)
		).thenReturn(
			String.valueOf(10)
		);

		_propsUtilMockedStatic.when(
			() -> PropsUtil.get(PropsKeys.DNS_SECURITY_THREAD_QUEUE_LIMIT)
		).thenReturn(
			String.valueOf(5)
		);
	}

	@AfterClass
	public static void tearDownClass() {
		_propsUtilMockedStatic.close();
	}

	@Test
	public void testDownloadAndInstallJar() throws Exception {
		_testDownload(_SHA1_REAL);
		_testDownload(_SHA1_FAKE);

		Path tempFilePath = Files.createTempFile(
			RandomTestUtil.randomString(), RandomTestUtil.randomString());

		try (SafeCloseable safeCloseable =
				PropsValuesTestUtil.swapWithSafeCloseable(
					"FIPS_ENABLED", true)) {

			FIPSModeTestUtil.assertSecurityException(
				"Algorithm \"SHA-1\" is not allowed in FIPS mode",
				() -> URLUtil.download(
					URLUtilTest.class.getResource("dependencies/test.jar"),
					tempFilePath, _SHA1_REAL));

			Assert.assertEquals(0, Files.size(tempFilePath));
		}
		finally {
			Files.deleteIfExists(tempFilePath);
		}
	}

	@Test
	public void testIsValidURL() {

		// Unsupported scheme

		Assert.assertFalse(
			URLUtil.isValidURL("ftp://liferay.com", new String[0], false));

		// Missing host

		Assert.assertFalse(
			URLUtil.isValidURL("http:///path", new String[0], false));

		// Host not in allowlist

		Assert.assertFalse(
			URLUtil.isValidURL(
				"https://8.8.8.8", new String[] {"liferay.com"}, true));

		// Host in allowlist

		Assert.assertTrue(
			URLUtil.isValidURL(
				"https://liferay.com", new String[] {"liferay.com"}, true));

		// Local address allowed when local network access is enabled

		Assert.assertTrue(
			URLUtil.isValidURL("http://127.0.0.1", new String[0], true));

		// Local address rejected when local network access is disabled

		Assert.assertFalse(
			URLUtil.isValidURL("http://127.0.0.1", new String[0], false));

		// Public address allowed when local network access is disabled

		Assert.assertTrue(
			URLUtil.isValidURL("http://8.8.8.8", new String[0], false));
	}

	private void _testDownload(String sha1) throws Exception {
		URL url = URLUtilTest.class.getResource("dependencies/test.jar");

		Path tempFilePath = Files.createTempFile(
			RandomTestUtil.randomString(), RandomTestUtil.randomString());

		try {
			URLUtil.download(url, tempFilePath, sha1);

			if (sha1.equals(_SHA1_FAKE)) {
				Assert.fail(
					"Download should fail when invalid SHA1 is provided");
			}
		}
		catch (Exception exception) {
			if (sha1.equals(_SHA1_REAL)) {
				throw exception;
			}

			String message = exception.getMessage();

			Assert.assertTrue(
				message,
				message.contains(
					StringBundler.concat(
						"because ", _SHA1_FAKE, " does not equal ",
						_SHA1_REAL)));
		}
		finally {
			Files.deleteIfExists(tempFilePath);
		}
	}

	private static final String _SHA1_FAKE = "SHA1_FAKE";

	private static final String _SHA1_REAL =
		"98e93e8db707aa2d31118c9c88a2d6b642d896fd";

	private static final MockedStatic<PropsUtil> _propsUtilMockedStatic =
		Mockito.mockStatic(PropsUtil.class, Mockito.CALLS_REAL_METHODS);

}