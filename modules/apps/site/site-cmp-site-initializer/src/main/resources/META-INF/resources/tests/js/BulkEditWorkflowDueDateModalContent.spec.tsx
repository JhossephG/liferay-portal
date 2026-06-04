/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';
import {IBulkActionFDSData} from '@liferay/site-cms-site-initializer';
import {fireEvent, render, waitFor} from '@testing-library/react';
import React from 'react';

import BulkEditWorkflowDueDateModalContent from '../../js/components/modal/BulkEditWorkflowDueDateModalContent';
import {mockFetch} from '../js/__mocks__/frontend-js-web';

jest.mock('../../js/components/DateField', () => ({
	__esModule: true,
	dateConfig: {momentFormat: 'MM/DD/YYYY'},
	default: ({
		id,
		onChange,
	}: {
		id: string;
		onChange: (value: string) => Promise<void>;
	}) => (
		<input
			data-testid="mock-date-field"
			id={id}
			onChange={(event) => onChange(event.target.value)}
			type="text"
		/>
	),
}));

const mockCloseModal = jest.fn();
const mockSelectedData = {
	items: [{embedded: {id: 1}}, {embedded: {id: 2}}],
	selectAll: false,
} as unknown as IBulkActionFDSData;

describe('BulkEditWorkflowDueDateModalContent', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	it('renders the modal with save button disabled when no date is selected', () => {
		const {getByText} = render(
			<BulkEditWorkflowDueDateModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		expect(getByText('save')).toBeDisabled();
	});

	it('does not submit when no date is selected', async () => {
		const {getByText} = render(
			<BulkEditWorkflowDueDateModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		fireEvent.submit(getByText('save').closest('form')!);

		await waitFor(() => {
			expect(mockFetch).not.toHaveBeenCalled();
		});
	});

	it('keeps modal open and re-enables save button when PATCH fails', async () => {
		mockFetch.mockResolvedValueOnce({ok: false} as Response);

		const {getByTestId, getByText} = render(
			<BulkEditWorkflowDueDateModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		fireEvent.change(getByTestId('mock-date-field'), {
			target: {value: '06/05/2026'},
		});

		fireEvent.submit(getByText('save').closest('form')!);

		await waitFor(() => {
			expect(mockCloseModal).not.toHaveBeenCalled();
			expect(getByText('save')).not.toBeDisabled();
		});
	});

	it('calls close modal on cancel click', () => {
		const {getByText} = render(
			<BulkEditWorkflowDueDateModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		fireEvent.click(getByText('cancel'));

		expect(mockCloseModal).toHaveBeenCalled();
	});

	it('PATCHes update-due-date endpoint and closes modal on success', async () => {
		mockFetch.mockResolvedValueOnce({ok: true} as Response);

		const {getByTestId, getByText} = render(
			<BulkEditWorkflowDueDateModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		fireEvent.change(getByTestId('mock-date-field'), {
			target: {value: '06/05/2026'},
		});

		fireEvent.submit(getByText('save').closest('form')!);

		await waitFor(() => {
			expect(mockFetch).toHaveBeenCalledWith(
				'/o/headless-admin-workflow/v1.0/workflow-tasks/update-due-date',
				expect.objectContaining({method: 'PATCH'})
			);
			expect(mockCloseModal).toHaveBeenCalled();
		});
	});
});
