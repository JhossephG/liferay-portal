/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.example.race.service;

import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * Provides a wrapper for {@link RaceLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see RaceLocalService
 * @generated
 */
public class RaceLocalServiceWrapper
	implements RaceLocalService, ServiceWrapper<RaceLocalService> {

	public RaceLocalServiceWrapper() {
		this(null);
	}

	public RaceLocalServiceWrapper(RaceLocalService raceLocalService) {
		_raceLocalService = raceLocalService;
	}

	/**
	 * Adds the race to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect RaceLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param race the race
	 * @return the race that was added
	 */
	@Override
	public com.example.race.model.Race addRace(
		com.example.race.model.Race race) {

		return _raceLocalService.addRace(race);
	}

	@Override
	public com.example.race.model.Race addRace(String name)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _raceLocalService.addRace(name);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _raceLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new race with the primary key. Does not add the race to the database.
	 *
	 * @param raceId the primary key for the new race
	 * @return the new race
	 */
	@Override
	public com.example.race.model.Race createRace(long raceId) {
		return _raceLocalService.createRace(raceId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _raceLocalService.deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the race with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect RaceLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param raceId the primary key of the race
	 * @return the race that was removed
	 * @throws PortalException if a race with the primary key could not be found
	 */
	@Override
	public com.example.race.model.Race deleteRace(long raceId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _raceLocalService.deleteRace(raceId);
	}

	/**
	 * Deletes the race from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect RaceLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param race the race
	 * @return the race that was removed
	 */
	@Override
	public com.example.race.model.Race deleteRace(
		com.example.race.model.Race race) {

		return _raceLocalService.deleteRace(race);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _raceLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _raceLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _raceLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _raceLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.example.race.model.impl.RaceModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _raceLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.example.race.model.impl.RaceModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _raceLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _raceLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _raceLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public com.example.race.model.Race fetchRace(long raceId) {
		return _raceLocalService.fetchRace(raceId);
	}

	/**
	 * Returns the race matching the UUID and group.
	 *
	 * @param uuid the race's UUID
	 * @param groupId the primary key of the group
	 * @return the matching race, or <code>null</code> if a matching race could not be found
	 */
	@Override
	public com.example.race.model.Race fetchRaceByUuidAndGroupId(
		String uuid, long groupId) {

		return _raceLocalService.fetchRaceByUuidAndGroupId(uuid, groupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _raceLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _raceLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _raceLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _raceLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _raceLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the race with the primary key.
	 *
	 * @param raceId the primary key of the race
	 * @return the race
	 * @throws PortalException if a race with the primary key could not be found
	 */
	@Override
	public com.example.race.model.Race getRace(long raceId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _raceLocalService.getRace(raceId);
	}

	/**
	 * Returns the race matching the UUID and group.
	 *
	 * @param uuid the race's UUID
	 * @param groupId the primary key of the group
	 * @return the matching race
	 * @throws PortalException if a matching race could not be found
	 */
	@Override
	public com.example.race.model.Race getRaceByUuidAndGroupId(
			String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _raceLocalService.getRaceByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns a range of all the races.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.example.race.model.impl.RaceModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @return the range of races
	 */
	@Override
	public java.util.List<com.example.race.model.Race> getRaces(
		int start, int end) {

		return _raceLocalService.getRaces(start, end);
	}

	/**
	 * Returns all the races matching the UUID and company.
	 *
	 * @param uuid the UUID of the races
	 * @param companyId the primary key of the company
	 * @return the matching races, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<com.example.race.model.Race>
		getRacesByUuidAndCompanyId(String uuid, long companyId) {

		return _raceLocalService.getRacesByUuidAndCompanyId(uuid, companyId);
	}

	/**
	 * Returns a range of races matching the UUID and company.
	 *
	 * @param uuid the UUID of the races
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of races
	 * @param end the upper bound of the range of races (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching races, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<com.example.race.model.Race>
		getRacesByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.example.race.model.Race> orderByComparator) {

		return _raceLocalService.getRacesByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of races.
	 *
	 * @return the number of races
	 */
	@Override
	public int getRacesCount() {
		return _raceLocalService.getRacesCount();
	}

	/**
	 * Updates the race in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect RaceLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param race the race
	 * @return the race that was updated
	 */
	@Override
	public com.example.race.model.Race updateRace(
		com.example.race.model.Race race) {

		return _raceLocalService.updateRace(race);
	}

	@Override
	public BasePersistence<?> getBasePersistence() {
		return _raceLocalService.getBasePersistence();
	}

	@Override
	public RaceLocalService getWrappedService() {
		return _raceLocalService;
	}

	@Override
	public void setWrappedService(RaceLocalService raceLocalService) {
		_raceLocalService = raceLocalService;
	}

	private RaceLocalService _raceLocalService;

}