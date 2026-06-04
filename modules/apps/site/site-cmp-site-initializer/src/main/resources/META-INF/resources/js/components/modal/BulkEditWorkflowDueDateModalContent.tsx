/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import ClayModal from '@clayui/modal';
import {FDS_EVENT} from '@liferay/frontend-data-set-web';
import {IBulkActionFDSData} from '@liferay/site-cms-site-initializer';
import {fetch} from 'frontend-js-web';
import moment from 'moment';
import React, {useId, useState} from 'react';

import {displayErrorToast} from '../../utils/toastUtil';
import DateField, {dateConfig} from '../DateField';

export default function BulkEditWorkflowDueDateModalContent({
	closeModal,
	dataSetId,
	selectedData,
}: {
	closeModal: () => void;
	dataSetId: string;
	selectedData: IBulkActionFDSData;
}) {
	const [dueDate, setDueDate] = useState('');
	const [submitDisabled, setSubmitDisabled] = useState(false);

	const dateFieldId = useId();

	const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
		event.preventDefault();

		if (!dueDate) {
			return;
		}

		setSubmitDisabled(true);

		try {

			// Bulk due-date update has no time picker; midnight UTC is used as
			// the default time, matching the date-only intent of the selection.

			const response = await fetch(
				'/o/headless-admin-workflow/v1.0/workflow-tasks/update-due-date',
				{
					body: JSON.stringify(
						(selectedData as any).items.map((item: any) => ({
							dueDate:
								moment(dueDate, dateConfig.momentFormat).format(
									'YYYY-MM-DD'
								) + 'T00:00:00.000Z',
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
				{Liferay.Language.get('update-due-date')}
			</ClayModal.Header>

			<ClayModal.Body>
				<label htmlFor={dateFieldId}>
					{Liferay.Language.get('new-due-date')}
				</label>

				<DateField
					id={dateFieldId}
					onChange={async (value) => setDueDate(value)}
				/>
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
							disabled={submitDisabled || !dueDate}
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
