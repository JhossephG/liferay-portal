/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	_fromDDMFormToDataDefinitionPropertyName,
	fieldToDataDefinition,
} from '../../../src/main/resources/META-INF/resources/js/utils/dataConverter';

describe('dataConverter', () => {
	it('is getting component form data property', () => {
		expect(_fromDDMFormToDataDefinitionPropertyName('fieldName')).toBe(
			'name'
		);
		expect(_fromDDMFormToDataDefinitionPropertyName('nestedFields')).toBe(
			'nestedDataDefinitionFields'
		);
		expect(
			_fromDDMFormToDataDefinitionPropertyName('predefinedValue')
		).toBe('defaultValue');
		expect(_fromDDMFormToDataDefinitionPropertyName('type')).toBe(
			'fieldType'
		);
		expect(_fromDDMFormToDataDefinitionPropertyName('otherProperty')).toBe(
			'otherProperty'
		);
	});

	it('is getting data definition field', () => {
		expect(
			fieldToDataDefinition({
				nestedFields: [],
				settingsContext: {pages: []},
			})
		).toMatchObject({
			customProperties: {},
			nestedDataDefinitionFields: [],
		});
	});
	it('is getting data definition field when settingsContext is not provided', () => {
		expect(
			fieldToDataDefinition({
				fieldName: 'myField',
				indexable: true,
				nestedFields: [
					{
						fieldName: 'nestedField',
						type: 'text',
					},
				],
				required: true,
				type: 'text',
			})
		).toMatchObject({
			customProperties: {},
			fieldType: 'text',
			indexable: true,
			name: 'myField',
			nestedDataDefinitionFields: [
				{
					customProperties: {},
					fieldType: 'text',
					name: 'nestedField',
					nestedDataDefinitionFields: [],
				},
			],
			required: true,
		});
	});

	it('is getting data definition field when settingsContext does not include pages', () => {
		expect(
			fieldToDataDefinition({
				fieldName: 'myField',
				nestedFields: null,
				required: false,
				settingsContext: {},
				type: 'text',
			})
		).toMatchObject({
			customProperties: {},
			fieldType: 'text',
			name: 'myField',
			nestedDataDefinitionFields: [],
			required: false,
		});
	});


	it('uses direct field properties when settingsContext pages is empty', () => {
		expect(
			fieldToDataDefinition({
				fieldName: 'normalizedNestedField',
				indexable: true,
				settingsContext: {pages: []},
				type: 'text',
			})
		).toMatchObject({
			customProperties: {},
			fieldType: 'text',
			indexable: true,
			name: 'normalizedNestedField',
			nestedDataDefinitionFields: [],
		});
	});

	it('returns an empty data definition when field is undefined', () => {
		expect(fieldToDataDefinition()).toMatchObject({
			customProperties: {},
			nestedDataDefinitionFields: [],
		});
	});

	it('ignores invalid nested field entries', () => {
		expect(
			fieldToDataDefinition({
				nestedFields: [undefined, null],
				settingsContext: {pages: []},
			})
		).toMatchObject({
			customProperties: {},
			nestedDataDefinitionFields: [],
		});
	});

});
