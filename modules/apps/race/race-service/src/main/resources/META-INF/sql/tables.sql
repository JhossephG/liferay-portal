create table RACE_Race (
	uuid_ VARCHAR(75) null,
	raceId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	description BOOLEAN,
	location VARCHAR(75) null,
	name VARCHAR(75) null
);