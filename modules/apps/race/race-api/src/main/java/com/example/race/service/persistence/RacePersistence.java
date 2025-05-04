/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.service.persistence;

import com.example.race.exception.NoSuchRaceException;
import com.example.race.model.Race;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the race service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see RaceUtil
 * @generated
 */
@ProviderType
public interface RacePersistence extends BasePersistence<Race> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link RaceUtil} to access the race persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the races where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching races
	 */
	public java.util.List<Race> findByUuid(String uuid);

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
	public java.util.List<Race> findByUuid(String uuid, int start, int end);

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
	public java.util.List<Race> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

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
	public java.util.List<Race> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public Race findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Returns the first race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

	/**
	 * Returns the last race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public Race findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Returns the last race in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

	/**
	 * Returns the races before and after the current race in the ordered set where uuid = &#63;.
	 *
	 * @param raceId the primary key of the current race
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next race
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public Race[] findByUuid_PrevAndNext(
			long raceId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Removes all the races where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of races where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching races
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the race where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchRaceException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public Race findByUUID_G(String uuid, long groupId)
		throws NoSuchRaceException;

	/**
	 * Returns the race where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByUUID_G(String uuid, long groupId);

	/**
	 * Returns the race where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache);

	/**
	 * Removes the race where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the race that was removed
	 */
	public Race removeByUUID_G(String uuid, long groupId)
		throws NoSuchRaceException;

	/**
	 * Returns the number of races where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching races
	 */
	public int countByUUID_G(String uuid, long groupId);

	/**
	 * Returns all the races where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching races
	 */
	public java.util.List<Race> findByUuid_C(String uuid, long companyId);

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
	public java.util.List<Race> findByUuid_C(
		String uuid, long companyId, int start, int end);

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
	public java.util.List<Race> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

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
	public java.util.List<Race> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public Race findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Returns the first race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

	/**
	 * Returns the last race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public Race findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Returns the last race in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

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
	public Race[] findByUuid_C_PrevAndNext(
			long raceId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Removes all the races where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of races where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching races
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns all the races where name = &#63;.
	 *
	 * @param name the name
	 * @return the matching races
	 */
	public java.util.List<Race> findByname(String name);

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
	public java.util.List<Race> findByname(String name, int start, int end);

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
	public java.util.List<Race> findByname(
		String name, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

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
	public java.util.List<Race> findByname(
		String name, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public Race findByname_First(
			String name,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Returns the first race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByname_First(
		String name,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

	/**
	 * Returns the last race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race
	 * @throws NoSuchRaceException if a matching race could not be found
	 */
	public Race findByname_Last(
			String name,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Returns the last race in the ordered set where name = &#63;.
	 *
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching race, or <code>null</code> if a matching race could not be found
	 */
	public Race fetchByname_Last(
		String name,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

	/**
	 * Returns the races before and after the current race in the ordered set where name = &#63;.
	 *
	 * @param raceId the primary key of the current race
	 * @param name the name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next race
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public Race[] findByname_PrevAndNext(
			long raceId, String name,
			com.liferay.portal.kernel.util.OrderByComparator<Race>
				orderByComparator)
		throws NoSuchRaceException;

	/**
	 * Removes all the races where name = &#63; from the database.
	 *
	 * @param name the name
	 */
	public void removeByname(String name);

	/**
	 * Returns the number of races where name = &#63;.
	 *
	 * @param name the name
	 * @return the number of matching races
	 */
	public int countByname(String name);

	/**
	 * Caches the race in the entity cache if it is enabled.
	 *
	 * @param race the race
	 */
	public void cacheResult(Race race);

	/**
	 * Caches the races in the entity cache if it is enabled.
	 *
	 * @param races the races
	 */
	public void cacheResult(java.util.List<Race> races);

	/**
	 * Creates a new race with the primary key. Does not add the race to the database.
	 *
	 * @param raceId the primary key for the new race
	 * @return the new race
	 */
	public Race create(long raceId);

	/**
	 * Removes the race with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param raceId the primary key of the race
	 * @return the race that was removed
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public Race remove(long raceId) throws NoSuchRaceException;

	public Race updateImpl(Race race);

	/**
	 * Returns the race with the primary key or throws a <code>NoSuchRaceException</code> if it could not be found.
	 *
	 * @param raceId the primary key of the race
	 * @return the race
	 * @throws NoSuchRaceException if a race with the primary key could not be found
	 */
	public Race findByPrimaryKey(long raceId) throws NoSuchRaceException;

	/**
	 * Returns the race with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param raceId the primary key of the race
	 * @return the race, or <code>null</code> if a race with the primary key could not be found
	 */
	public Race fetchByPrimaryKey(long raceId);

	/**
	 * Returns all the races.
	 *
	 * @return the races
	 */
	public java.util.List<Race> findAll();

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
	public java.util.List<Race> findAll(int start, int end);

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
	public java.util.List<Race> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator);

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
	public java.util.List<Race> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Race>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the races from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of races.
	 *
	 * @return the number of races
	 */
	public int countAll();

}