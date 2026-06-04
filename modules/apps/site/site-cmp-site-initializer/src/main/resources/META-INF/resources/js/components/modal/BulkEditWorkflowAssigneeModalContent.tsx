/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import {ClaySelect} from '@clayui/form';
import ClayModal from '@clayui/modal';
import {FDS_EVENT} from '@liferay/frontend-data-set-web';
import {IBulkActionFDSData} from '@liferay/site-cms-site-initializer';
import {fetch} from 'frontend-js-web';
import React, {useEffect, useId, useState} from 'react';

import {displayErrorToast} from '../../utils/toastUtil';

type Assignee = {
	id: number;
	name: string;
};

export default function BulkEditWorkflowAssigneeModalContent({
	closeModal,
	dataSetId,
	selectedData,
}: {
	closeModal: () => void;
	dataSetId: string;
	selectedData: IBulkActionFDSData;
}) {
	const [assignableUsers, setAssignableUsers] = useState<Assignee[]>([]);
	const [selectedUserId, setSelectedUserId] = useState(0);
	const [submitDisabled, setSubmitDisabled] = useState(false);

	const selectId = useId();

	useEffect(() => {
		fetch(
			'/o/headless-admin-workflow/v1.0/workflow-tasks/assignable-users',
			{
				body: JSON.stringify({
					workflowTaskIds: (selectedData as any).items.map(
						(item: any) => item.embedded?.id
					),
				}),
				headers: {'Content-Type': 'application/json'},
				method: 'POST',
			}
		)
			.then((response) => {
				if (!response.ok) {
					throw new Error();
				}

				return response.json();
			})
			.then((data) => {
				const usersByTask: Assignee[][] =
					data.workflowTaskAssignableUsers?.map(
						(entry: {assignableUsers: Assignee[]}) =>
							entry.assignableUsers ?? []
					) ?? [];

				const intersectedUsers = usersByTask.reduce(
					(common, taskUsers) =>
						common.filter((user) =>
							taskUsers.some((u) => u.id === user.id)
						),
					usersByTask[0] ?? []
				);

				setAssignableUsers(intersectedUsers);

				if (intersectedUsers.length) {
					setSelectedUserId(intersectedUsers[0].id);
				}
			})
			.catch(() => {
				displayErrorToast();
			});
	}, [selectedData]);

	const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
		event.preventDefault();

		if (!selectedUserId) {
			return;
		}

		setSubmitDisabled(true);

		try {
			const response = await fetch(
				'/o/headless-admin-workflow/v1.0/workflow-tasks/assign-to-user',
				{
					body: JSON.stringify(
						(selectedData as any).items.map((item: any) => ({
							assigneeId: selectedUserId,
							workflowTaskId: item.embedded?.id,
						}))
					),
					headers: {'Content-Type': 'application/json'},
					method: 'PATCH',
				}
			);

			if (!response.ok) {
				throw new Error();
			}

			Liferay.fire(FDS_EVENT.DISPLAY_UPDATED, {id: dataSetId});

			closeModal();
		}
		catch {
			displayErrorToast();

			setSubmitDisabled(false);
		}
	};

	return (
		<form onSubmit={handleSubmit}>
			<ClayModal.Header
				closeButtonAriaLabel={Liferay.Language.get('close')}
			>
				{Liferay.Language.get('assign-to-...')}
			</ClayModal.Header>

			<ClayModal.Body>
				<label htmlFor={selectId}>
					{Liferay.Language.get('assign-to')}
				</label>

				<ClaySelect
					disabled={!assignableUsers.length}
					id={selectId}
					name="assigneeId"
					onChange={(event) =>
						setSelectedUserId(Number(event.target.value))
					}
					value={selectedUserId}
				>
					{assignableUsers.map((user) => (
						<ClaySelect.Option
							key={user.id}
							label={user.name}
							value={user.id}
						/>
					))}
				</ClaySelect>
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton
							displayType="secondary"
							onClick={closeModal}
							type="button"
						>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<ClayButton
							disabled={submitDisabled || !assignableUsers.length}
							displayType="primary"
							type="submit"
						>
							{Liferay.Language.get('save')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</form>
	);
}
