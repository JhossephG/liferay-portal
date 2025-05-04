/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.service.persistence;

import com.example.race.model.Race;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the race service. This utility wraps <code>com.example.race.service.persistence.impl.RacePersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see RacePersistence
 * @generated
 */
public class RaceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(Race race) {
		getPersistence().clearCache(race);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, Race> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<Race> findWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<Race> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<Race> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<Race> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static Race update(Race race) {
		return getPersistence().update(race);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static Race update(Race race, ServiceContext serviceContext) {
		return getPersistence().update(race, serviceContext);
	}

	/**
	 * Returns all the races where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching races
	 */
	public static List<Race> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the races where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @return the range of matching races
	 */
	public static List<Race> findByUuid(String uuid, int start, int end) {
		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the races where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching races
	 */
	public static List<Race> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Race> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the races where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching races
	 */
	public static List<Race> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Race> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public static Race findByUuid_First(
			String uuid, OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByUuid_First(
		String uuid, OrderByComparator<Race> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public static Race findByUuid_Last(
			String uuid, OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByUuid_Last(
		String uuid, OrderByComparator<Race> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the races before and after the current race in the ordered set where uuid = &#63;.
	 *
	 * @param raceId the primary key of the current race
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next race
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public static Race[] findByUuid_PrevAndNext(
			long raceId, String uuid, OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByUuid_PrevAndNext(
			raceId, uuid, orderByComparator);
	}

	/**
	 * Removes all the races where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of races where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching races
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the race where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchRaceException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public static Race findByUUID_G(String uuid, long groupId)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the race where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the race where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the race where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the race that was removed
	 */
	public static Race removeByUUID_G(String uuid, long groupId)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of races where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching races
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the races where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching races
	 */
	public static List<Race> findByUuid_C(String uuid, long companyId) {
		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the races where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @return the range of matching races
	 */
	public static List<Race> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the races where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching races
	 */
	public static List<Race> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Race> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the races where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching races
	 */
	public static List<Race> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Race> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public static Race findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<Race> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public static Race findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<Race> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the races before and after the current race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param raceId the primary key of the current race
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next race
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public static Race[] findByUuid_C_PrevAndNext(
			long raceId, String uuid, long companyId,
			OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByUuid_C_PrevAndNext(
			raceId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the races where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of races where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching races
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the races where name = &#63;.
	 *
	 * @param name the name
	 * @return the matching races
	 */
	public static List<Race> findByname(String name) {
		return getPersistence().findByname(name);
	}

	/**
	 * Returns a range of all the races where name = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param name the name
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @return the range of matching races
	 */
	public static List<Race> findByname(String name, int start, int end) {
		return getPersistence().findByname(name, start, end);
	}

	/**
	 * Returns an ordered range of all the races where name = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param name the name
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching races
	 */
	public static List<Race> findByname(
		String name, int start, int end,
		OrderByComparator<Race> orderByComparator) {

		return getPersistence().findByname(name, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the races where name = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param name the name
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching races
	 */
	public static List<Race> findByname(
		String name, int start, int end,
		OrderByComparator<Race> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByname(
			name, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public static Race findByname_First(
			String name, OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByname_First(name, orderByComparator);
	}

	/**
	 * Returns the first race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByname_First(
		String name, OrderByComparator<Race> orderByComparator) {

		return getPersistence().fetchByname_First(name, orderByComparator);
	}

	/**
	 * Returns the last race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public static Race findByname_Last(
			String name, OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByname_Last(name, orderByComparator);
	}

	/**
	 * Returns the last race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race, or <code>null</code> if a matching race could not be found
	 */
	public static Race fetchByname_Last(
		String name, OrderByComparator<Race> orderByComparator) {

		return getPersistence().fetchByname_Last(name, orderByComparator);
	}

	/**
	 * Returns the races before and after the current race in the ordered set where name = &#63;.
	 *
	 * @param raceId the primary key of the current race
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next race
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public static Race[] findByname_PrevAndNext(
			long raceId, String name, OrderByComparator<Race> orderByComparator)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByname_PrevAndNext(
			raceId, name, orderByComparator);
	}

	/**
	 * Removes all the races where name = &#63; from the database.
	 *
	 * @param name the name
	 */
	public static void removeByname(String name) {
		getPersistence().removeByname(name);
	}

	/**
	 * Returns the number of races where name = &#63;.
	 *
	 * @param name the name
	 * @return the number of matching races
	 */
	public static int countByname(String name) {
		return getPersistence().countByname(name);
	}

	/**
	 * Caches the race in the entity cache if it is enabled.
	 *
	 * @param race the race
	 */
	public static void cacheResult(Race race) {
		getPersistence().cacheResult(race);
	}

	/**
	 * Caches the races in the entity cache if it is enabled.
	 *
	 * @param races the races
	 */
	public static void cacheResult(List<Race> races) {
		getPersistence().cacheResult(races);
	}

	/**
	 * Creates a new race with the primary key. Does not add the race to the database.
	 *
	 * @param raceId the primary key for the new race
	 * @return the new race
	 */
	public static Race create(long raceId) {
		return getPersistence().create(raceId);
	}

	/**
	 * Removes the race with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param raceId the primary key of the race
	 * @return the race that was removed
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public static Race remove(long raceId)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().remove(raceId);
	}

	public static Race updateImpl(Race race) {
		return getPersistence().updateImpl(race);
	}

	/**
	 * Returns the race with the primary key or throws a <code>NoSuchRaceException</code> if it could not be found.
	 *
	 * @param raceId the primary key of the race
	 * @return the race
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public static Race findByPrimaryKey(long raceId)
		throws com.example.race.exception.NoSuchRaceException {

		return getPersistence().findByPrimaryKey(raceId);
	}

	/**
	 * Returns the race with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param raceId the primary key of the race
	 * @return the race, or <code>null</code> if a race with the primary key could not be found
	 */
	public static Race fetchByPrimaryKey(long raceId) {
		return getPersistence().fetchByPrimaryKey(raceId);
	}

	/**
	 * Returns all the races.
	 *
	 * @return the races
	 */
	public static List<Race> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the races.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @return the range of races
	 */
	public static List<Race> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the races.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of races
	 */
	public static List<Race> findAll(
		int start, int end, OrderByComparator<Race> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the races.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>RaceModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of races
	 */
	public static List<Race> findAll(
		int start, int end, OrderByComparator<Race> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the races from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of races.
	 *
	 * @return the number of races
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static RacePersistence getPersistence() {
		return _persistence;
	}

	public static void setPersistence(RacePersistence persistence) {
		_persistence = persistence;
	}

	private static volatile RacePersistence _persistence;

}