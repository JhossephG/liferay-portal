/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.model.impl;

import com.example.race.model.Race;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing Race in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class RaceCacheModel implements CacheModel<Race>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof RaceCacheModel)) {
			return false;
		}

		RaceCacheModel raceCacheModel = (RaceCacheModel)object;

		if (raceId == raceCacheModel.raceId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, raceId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", raceId=");
		sb.append(raceId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", description=");
		sb.append(description);
		sb.append(", location=");
		sb.append(location);
		sb.append(", name=");
		sb.append(name);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Race toEntityModel() {
		RaceImpl raceImpl = new RaceImpl();

		if (uuid == null) {
			raceImpl.setUuid("");
		}
		else {
			raceImpl.setUuid(uuid);
		}

		raceImpl.setRaceId(raceId);
		raceImpl.setGroupId(groupId);
		raceImpl.setCompanyId(companyId);
		raceImpl.setUserId(userId);

		if (userName == null) {
			raceImpl.setUserName("");
		}
		else {
			raceImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			raceImpl.setCreateDate(null);
		}
		else {
			raceImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			raceImpl.setModifiedDate(null);
		}
		else {
			raceImpl.setModifiedDate(new Date(modifiedDate));
		}

		raceImpl.setDescription(description);

		if (location == null) {
			raceImpl.setLocation("");
		}
		else {
			raceImpl.setLocation(location);
		}

		if (name == null) {
			raceImpl.setName("");
		}
		else {
			raceImpl.setName(name);
		}

		raceImpl.resetOriginalValues();

		return raceImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		raceId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		description = objectInput.readBoolean();
		location = objectInput.readUTF();
		name = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(raceId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeBoolean(description);

		if (location == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(location);
		}

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}
	}

	public String uuid;
	public long raceId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public boolean description;
	public String location;
	public String name;

}