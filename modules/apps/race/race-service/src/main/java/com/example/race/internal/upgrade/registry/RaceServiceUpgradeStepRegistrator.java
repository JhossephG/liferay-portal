package com.example.race.internal.upgrade;

import com.example.race.internal.upgrade.v1_0_0.RaceUpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	service = UpgradeStepRegistrator.class
)
public class RaceServiceUpgradeStepRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"0.0.0", "1.0.0",
			new RaceUpgradeProcess()
		);
	}

}
