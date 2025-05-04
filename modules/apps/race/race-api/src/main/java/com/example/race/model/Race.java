/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the Race service. Represents a row in the &quot;RACE_Race&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see RaceModel
 * @generated
 */
@ImplementationClassName("com.example.race.model.impl.RaceImpl")
@ProviderType
public interface Race extends PersistedModel, RaceModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.example.race.model.impl.RaceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Race, Long> RACE_ID_ACCESSOR =
		new Accessor<Race, Long>() {

			@Override
			public Long get(Race race) {
				return race.getRaceId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<Race> getTypeClass() {
				return Race.class;
			}

		};

}