/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import {FDS_EVENT} from '@liferay/frontend-data-set-web';
import classNames from 'classnames';
import {fetch} from 'frontend-js-web';
import React, {useCallback, useEffect, useState} from 'react';

import './TasksOverview.scss';

import './WorkflowTasksOverview.scss';

const CONTAINER_CLASS_NAME =
	'lfr-cmp__tasks-overview-container lfr-cmp__workflow-tasks-overview-container';

const WORKFLOW_TASKS_URL =
	'/o/headless-admin-workflow/v1.0/workflow-tasks?pageSize=1';

function StatCard({
	count,
	icon,
	iconClassName,
	label,
}: {
	count: number;
	icon: string;
	iconClassName: string;
	label: string;
}) {
	return (
		<div className="align-items-center d-flex statistic-button">
			<span className={classNames('inline-item mr-3', iconClassName)}>
				<ClayIcon className="lexicon-icon-lg" symbol={icon} />
			</span>

			<div>
				<div className="font-weight-bold text-5">{count}</div>

				<div className="text-4 text-secondary">{label}</div>
			</div>
		</div>
	);
}

export default function WorkflowTasksOverview() {
	const [completedCount, setCompletedCount] = useState(0);
	const [loading, setLoading] = useState(true);
	const [pendingCount, setPendingCount] = useState(0);
	const [totalCount, setTotalCount] = useState(0);

	const fetchCounts = useCallback(async () => {
		setLoading(true);

		const fetchWorkflowCount = (completed: boolean) =>
			fetch(WORKFLOW_TASKS_URL, {
				body: JSON.stringify({completed}),
				headers: {'Content-Type': 'application/json'},
				method: 'POST',
			})
				.then((response) => {
					if (!response.ok) {
						throw new Error();
					}

					return response.json();
				})
				.then((data) => data.totalCount ?? 0);

		try {
			const [completedCount, pendingCount] = await Promise.all([
				fetchWorkflowCount(true),
				fetchWorkflowCount(false),
			]);

			setCompletedCount(completedCount);
			setPendingCount(pendingCount);
			setTotalCount(completedCount + pendingCount);
		}
		catch {
			setCompletedCount(0);
			setPendingCount(0);
			setTotalCount(0);
		}
		finally {
			setLoading(false);
		}
	}, []);

	useEffect(() => {
		fetchCounts();
	}, [fetchCounts]);

	useEffect(() => {
		Liferay.on(FDS_EVENT.DISPLAY_UPDATED, fetchCounts);

		return () => {
			Liferay.detach(FDS_EVENT.DISPLAY_UPDATED, fetchCounts);
		};
	}, [fetchCounts]);

	if (loading) {
		return (
			<div className={CONTAINER_CLASS_NAME}>
				<ClayLoadingIndicator />
			</div>
		);
	}

	if (totalCount === 0) {
		return null;
	}

	return (
		<div className={CONTAINER_CLASS_NAME}>
			<ClayLayout.ContainerFluid className="c-px-0" size={false}>
				<ClayLayout.Row className="justify-content-center">
					<ClayLayout.Col className="c-px-2" size={3}>
						<StatCard
							count={pendingCount}
							icon="time"
							iconClassName="text-secondary"
							label={Liferay.Language.get('pending')}
						/>
					</ClayLayout.Col>

					<ClayLayout.Col className="c-px-2" size={3}>
						<StatCard
							count={completedCount}
							icon="check-circle-full"
							iconClassName="text-success"
							label={Liferay.Language.get('completed')}
						/>
					</ClayLayout.Col>
				</ClayLayout.Row>
			</ClayLayout.ContainerFluid>
		</div>
	);
}
