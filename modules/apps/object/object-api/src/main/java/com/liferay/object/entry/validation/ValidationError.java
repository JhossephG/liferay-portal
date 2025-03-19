/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.entry.validation;

/**
 * @author Jhosseph Gonzalez
 */
public class ValidationError {

	public ValidationError(String errorMessage) {
		_errorMessage = errorMessage;
	}

	public ValidationError(
		String errorMessage, String objectFieldName, String validationKey) {

		_errorMessage = errorMessage;
		_objectFieldName = objectFieldName;
		_validationKey = validationKey;
	}

	public String getErrorMessage() {
		return _errorMessage;
	}

	public String getObjectFieldName() {
		return _objectFieldName;
	}

	public String getValidationKey() {
		return _validationKey;
	}

	private final String _errorMessage;
	private String _objectFieldName;
	private String _validationKey;

}