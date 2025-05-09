/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.service.impl;

import com.example.race.service.base.RaceLocalServiceBaseImpl;

import com.liferay.portal.aop.AopService;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.example.race.model.Race",
	service = AopService.class
)
public class RaceLocalServiceImpl extends RaceLocalServiceBaseImpl {
	@Override
	public Race addRace(String name) throws PortalException {
		long raceId = counterLocalService.increment(Race.class.getName());

		Race race = racePersistence.create(raceId);
		race.setName(name);

		return racePersistence.update(race);
	}
}