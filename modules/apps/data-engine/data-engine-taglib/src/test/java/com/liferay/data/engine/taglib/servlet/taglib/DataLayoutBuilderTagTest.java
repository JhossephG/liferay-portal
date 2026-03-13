/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.data.engine.taglib.servlet.taglib;

import com.liferay.data.engine.taglib.servlet.taglib.DataLayoutBuilderTag.DataLayoutDDMFormAdapter;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.lang.reflect.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Igor Franca
 */
public class DataLayoutBuilderTagTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		BundleContext bundleContext = SystemBundleUtil.getBundleContext();

		_frameworkUtilMockedStatic.when(
			() -> FrameworkUtil.getBundle(Mockito.any())
		).thenReturn(
			bundleContext.getBundle()
		);
	}

	@AfterClass
	public static void tearDownClass() {
		_frameworkUtilMockedStatic.close();
	}

	@Before
	public void setUp() {
		DataLayoutBuilderTag dataLayoutBuilderTag = new DataLayoutBuilderTag();

		_dataLayoutDDMFormAdapter =
			dataLayoutBuilderTag.new DataLayoutDDMFormAdapter(
				Collections.singleton(LocaleUtil.US), null, null, null, null);
	}

	@Test
	public void testDataLayoutDDMFormAdapterPopulateDDMFormFieldSettingsContextDoesNotProcessNestedFields()
		throws Exception {

		Map<String, DDMFormField> ddmFormFieldsMap = new HashMap<>();

		ddmFormFieldsMap.put("nested", new DDMFormField("nested", "text"));

		Map<String, Object> nestedField = new HashMap<>();

		nestedField.put("fieldName", "nested");

		Map<String, Object> field = new HashMap<>();

		field.put("fieldName", "top");
		field.put("nestedFields", Collections.singletonList(nestedField));

		Map<String, Object> column = new HashMap<>();

		column.put("fields", Collections.singletonList(field));

		Map<String, Object> row = new HashMap<>();

		row.put("columns", Collections.singletonList(column));

		Map<String, Object> page = new HashMap<>();

		page.put("rows", Collections.singletonList(row));

		Map<String, Object> ddmFormTemplateContext = new HashMap<>();

		ddmFormTemplateContext.put("pages", Collections.singletonList(page));

		Method method = DataLayoutDDMFormAdapter.class.getDeclaredMethod(
			"_populateDDMFormFieldSettingsContext", Map.class, Map.class,
			Locale.class);

		method.setAccessible(true);

		method.invoke(
			_dataLayoutDDMFormAdapter, ddmFormFieldsMap, ddmFormTemplateContext,
			LocaleUtil.US);

		Assert.assertFalse(field.containsKey("settingsContext"));
		Assert.assertFalse(nestedField.containsKey("settingsContext"));
	}

	@Test
	public void testDataLayoutDDMFormAdapterCreateDDMFormFieldValue()
		throws Exception {

		_testDataLayoutDDMFormAdapterCreateDDMFormFieldValue(
			null, StringPool.BLANK);

		String propertyValue = RandomTestUtil.randomString();

		_testDataLayoutDDMFormAdapterCreateDDMFormFieldValue(
			propertyValue, propertyValue);
	}

	private void _testDataLayoutDDMFormAdapterCreateDDMFormFieldValue(
			String actualPropertyValue, String expectedPropertyValue)
		throws Exception {

		Value value = _dataLayoutDDMFormAdapter.createDDMFormFieldValue(
			Collections.singleton(LocaleUtil.US), new DDMFormField(),
			actualPropertyValue);

		Assert.assertTrue(value instanceof UnlocalizedValue);
		Assert.assertEquals(
			expectedPropertyValue, value.getString(LocaleUtil.US));
	}

	private static final MockedStatic<FrameworkUtil>
		_frameworkUtilMockedStatic = Mockito.mockStatic(FrameworkUtil.class);

	private DataLayoutDDMFormAdapter _dataLayoutDDMFormAdapter;

}