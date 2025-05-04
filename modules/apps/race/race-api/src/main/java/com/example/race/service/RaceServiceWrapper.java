/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link RaceService}.
 *
 * @author Brian Wing Shun Chan
 * @see RaceService
 * @generated
 */
public class RaceServiceWrapper
	implements RaceService, ServiceWrapper<RaceService> {

	public RaceServiceWrapper() {
		this(null);
	}

	public RaceServiceWrapper(RaceService raceService) {
		_raceService = raceService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _raceService.getOSGiServiceIdentifier();
	}

	@Override
	public RaceService getWrappedService() {
		return _raceService;
	}

	@Override
	public void setWrappedService(RaceService raceService) {
		_raceService = raceService;
	}

	private RaceService _raceService;

}