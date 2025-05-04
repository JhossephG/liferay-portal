/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.service.persistence.test;

import com.example.race.exception.NoSuchRaceException;
import com.example.race.model.Race;
import com.example.race.service.RaceLocalServiceUtil;
import com.example.race.service.persistence.RacePersistence;
import com.example.race.service.persistence.RaceUtil;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class RacePersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.example.race.service"));

	@Before
	public void setUp() {
		_persistence = RaceUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<Race> iterator = _races.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Race race = _persistence.create(pk);

		Assert.assertNotNull(race);

		Assert.assertEquals(race.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Race newRace = addRace();

		_persistence.remove(newRace);

		Race existingRace = _persistence.fetchByPrimaryKey(
			newRace.getPrimaryKey());

		Assert.assertNull(existingRace);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addRace();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Race newRace = _persistence.create(pk);

		newRace.setUuid(RandomTestUtil.randomString());

		newRace.setGroupId(RandomTestUtil.nextLong());

		newRace.setCompanyId(RandomTestUtil.nextLong());

		newRace.setUserId(RandomTestUtil.nextLong());

		newRace.setUserName(RandomTestUtil.randomString());

		newRace.setCreateDate(RandomTestUtil.nextDate());

		newRace.setModifiedDate(RandomTestUtil.nextDate());

		newRace.setName(RandomTestUtil.randomString());

		newRace.setDescription(RandomTestUtil.randomBoolean());

		_races.add(_persistence.update(newRace));

		Race existingRace = _persistence.findByPrimaryKey(
			newRace.getPrimaryKey());

		Assert.assertEquals(existingRace.getUuid(), newRace.getUuid());
		Assert.assertEquals(existingRace.getRaceId(), newRace.getRaceId());
		Assert.assertEquals(existingRace.getGroupId(), newRace.getGroupId());
		Assert.assertEquals(
			existingRace.getCompanyId(), newRace.getCompanyId());
		Assert.assertEquals(existingRace.getUserId(), newRace.getUserId());
		Assert.assertEquals(existingRace.getUserName(), newRace.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingRace.getCreateDate()),
			Time.getShortTimestamp(newRace.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingRace.getModifiedDate()),
			Time.getShortTimestamp(newRace.getModifiedDate()));
		Assert.assertEquals(existingRace.getName(), newRace.getName());
		Assert.assertEquals(
			existingRace.isDescription(), newRace.isDescription());
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid("");

		_persistence.countByUuid("null");

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUUID_G() throws Exception {
		_persistence.countByUUID_G("", RandomTestUtil.nextLong());

		_persistence.countByUUID_G("null", 0L);

		_persistence.countByUUID_G((String)null, 0L);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C("", RandomTestUtil.nextLong());

		_persistence.countByUuid_C("null", 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testCountByname() throws Exception {
		_persistence.countByname("");

		_persistence.countByname("null");

		_persistence.countByname((String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Race newRace = addRace();

		Race existingRace = _persistence.findByPrimaryKey(
			newRace.getPrimaryKey());

		Assert.assertEquals(existingRace, newRace);
	}

	@Test(expected = NoSuchRaceException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<Race> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"RACE_Race", "uuid", true, "raceId", true, "groupId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "name", true, "description", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Race newRace = addRace();

		Race existingRace = _persistence.fetchByPrimaryKey(
			newRace.getPrimaryKey());

		Assert.assertEquals(existingRace, newRace);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Race missingRace = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingRace);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		Race newRace1 = addRace();
		Race newRace2 = addRace();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newRace1.getPrimaryKey());
		primaryKeys.add(newRace2.getPrimaryKey());

		Map<Serializable, Race> races = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(2, races.size());
		Assert.assertEquals(newRace1, races.get(newRace1.getPrimaryKey()));
		Assert.assertEquals(newRace2, races.get(newRace2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, Race> races = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(races.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		Race newRace = addRace();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newRace.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, Race> races = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, races.size());
		Assert.assertEquals(newRace, races.get(newRace.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, Race> races = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(races.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		Race newRace = addRace();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newRace.getPrimaryKey());

		Map<Serializable, Race> races = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, races.size());
		Assert.assertEquals(newRace, races.get(newRace.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			RaceLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<Race>() {

				@Override
				public void performAction(Race race) {
					Assert.assertNotNull(race);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		Race newRace = addRace();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Race.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq("raceId", newRace.getRaceId()));

		List<Race> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Race existingRace = result.get(0);

		Assert.assertEquals(existingRace, newRace);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Race.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq("raceId", RandomTestUtil.nextLong()));

		List<Race> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		Race newRace = addRace();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Race.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("raceId"));

		Object newRaceId = newRace.getRaceId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in("raceId", new Object[] {newRaceId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingRaceId = result.get(0);

		Assert.assertEquals(existingRaceId, newRaceId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Race.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("raceId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"raceId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		Race newRace = addRace();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(newRace.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		Race newRace = addRace();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Race.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq("raceId", newRace.getRaceId()));

		List<Race> result = _persistence.findWithDynamicQuery(dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(Race race) {
		Assert.assertEquals(
			race.getUuid(),
			ReflectionTestUtil.invoke(
				race, "getColumnOriginalValue", new Class<?>[] {String.class},
				"uuid_"));
		Assert.assertEquals(
			Long.valueOf(race.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				race, "getColumnOriginalValue", new Class<?>[] {String.class},
				"groupId"));
	}

	protected Race addRace() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Race race = _persistence.create(pk);

		race.setUuid(RandomTestUtil.randomString());

		race.setGroupId(RandomTestUtil.nextLong());

		race.setCompanyId(RandomTestUtil.nextLong());

		race.setUserId(RandomTestUtil.nextLong());

		race.setUserName(RandomTestUtil.randomString());

		race.setCreateDate(RandomTestUtil.nextDate());

		race.setModifiedDate(RandomTestUtil.nextDate());

		race.setName(RandomTestUtil.randomString());

		race.setDescription(RandomTestUtil.randomBoolean());

		_races.add(_persistence.update(race));

		return race;
	}

	private List<Race> _races = new ArrayList<Race>();
	private RacePersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}