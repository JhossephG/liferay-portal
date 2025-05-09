/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.model;

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Race}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Race
 * @generated
 */
public class RaceWrapper
	extends BaseModelWrapper<Race> implements ModelWrapper<Race>, Race {

	public RaceWrapper(Race race) {
		super(race);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("raceId", getRaceId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("description", isDescription());
		attributes.put("location", getLocation());
		attributes.put("name", getName());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long raceId = (Long)attributes.get("raceId");

		if (raceId != null) {
			setRaceId(raceId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Boolean description = (Boolean)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String location = (String)attributes.get("location");

		if (location != null) {
			setLocation(location);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}
	}

	@Override
	public Race cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the company ID of this race.
	 *
	 * @return the company ID of this race
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this race.
	 *
	 * @return the create date of this race
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the description of this race.
	 *
	 * @return the description of this race
	 */
	@Override
	public boolean getDescription() {
		return model.getDescription();
	}

	/**
	 * Returns the group ID of this race.
	 *
	 * @return the group ID of this race
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the location of this race.
	 *
	 * @return the location of this race
	 */
	@Override
	public String getLocation() {
		return model.getLocation();
	}

	/**
	 * Returns the modified date of this race.
	 *
	 * @return the modified date of this race
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the name of this race.
	 *
	 * @return the name of this race
	 */
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	 * Returns the primary key of this race.
	 *
	 * @return the primary key of this race
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the race ID of this race.
	 *
	 * @return the race ID of this race
	 */
	@Override
	public long getRaceId() {
		return model.getRaceId();
	}

	/**
	 * Returns the user ID of this race.
	 *
	 * @return the user ID of this race
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this race.
	 *
	 * @return the user name of this race
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this race.
	 *
	 * @return the user uuid of this race
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this race.
	 *
	 * @return the uuid of this race
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns <code>true</code> if this race is description.
	 *
	 * @return <code>true</code> if this race is description; <code>false</code> otherwise
	 */
	@Override
	public boolean isDescription() {
		return model.isDescription();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this race.
	 *
	 * @param companyId the company ID of this race
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this race.
	 *
	 * @param createDate the create date of this race
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets whether this race is description.
	 *
	 * @param description the description of this race
	 */
	@Override
	public void setDescription(boolean description) {
		model.setDescription(description);
	}

	/**
	 * Sets the group ID of this race.
	 *
	 * @param groupId the group ID of this race
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the location of this race.
	 *
	 * @param location the location of this race
	 */
	@Override
	public void setLocation(String location) {
		model.setLocation(location);
	}

	/**
	 * Sets the modified date of this race.
	 *
	 * @param modifiedDate the modified date of this race
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the name of this race.
	 *
	 * @param name the name of this race
	 */
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	 * Sets the primary key of this race.
	 *
	 * @param primaryKey the primary key of this race
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the race ID of this race.
	 *
	 * @param raceId the race ID of this race
	 */
	@Override
	public void setRaceId(long raceId) {
		model.setRaceId(raceId);
	}

	/**
	 * Sets the user ID of this race.
	 *
	 * @param userId the user ID of this race
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this race.
	 *
	 * @param userName the user name of this race
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this race.
	 *
	 * @param userUuid the user uuid of this race
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this race.
	 *
	 * @param uuid the uuid of this race
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	public String toXmlString() {
		return model.toXmlString();
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected RaceWrapper wrap(Race race) {
		return new RaceWrapper(race);
	}

}