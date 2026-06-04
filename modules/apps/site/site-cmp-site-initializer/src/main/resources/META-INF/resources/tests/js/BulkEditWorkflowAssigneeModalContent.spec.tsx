/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';
import {IBulkActionFDSData} from '@liferay/site-cms-site-initializer';
import {fireEvent, render, waitFor} from '@testing-library/react';
import React from 'react';

import BulkEditWorkflowAssigneeModalContent from '../../js/components/modal/BulkEditWorkflowAssigneeModalContent';
import {mockFetch} from '../js/__mocks__/frontend-js-web';

const mockOpenToast = jest.fn();

jest.mock('frontend-js-components-web', () => ({
	openToast: (...args: any[]) => mockOpenToast(...args),
}));

const mockCloseModal = jest.fn();
const mockSelectedData = {
	items: [{embedded: {id: 1}}, {embedded: {id: 2}}],
	selectAll: false,
} as unknown as IBulkActionFDSData;

const mockAssignableUsersResponse = {
	workflowTaskAssignableUsers: [
		{
			assignableUsers: [
				{id: 10, name: 'User A'},
				{id: 20, name: 'User B'},
			],
			workflowTaskId: 1,
		},
		{
			assignableUsers: [{id: 10, name: 'User A'}],
			workflowTaskId: 2,
		},
	],
};

describe('BulkEditWorkflowAssigneeModalContent', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	it('renders disabled combobox when no assignable users are found', async () => {
		mockFetch.mockResolvedValueOnce({
			json: async () => ({workflowTaskAssignableUsers: []}),
			ok: true,
		} as Response);

		const {getByRole} = render(
			<BulkEditWorkflowAssigneeModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		await waitFor(() => {
			expect(getByRole('combobox')).toBeDisabled();
		});
	});

	it('shows error toast when fetching assignable users fails', async () => {
		mockFetch.mockRejectedValueOnce(new Error('network error'));

		render(
			<BulkEditWorkflowAssigneeModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		await waitFor(() => {
			expect(mockOpenToast).toHaveBeenCalledWith(
				expect.objectContaining({type: 'danger'})
			);
		});
	});

	it('calls close modal on cancel click', async () => {
		mockFetch.mockResolvedValueOnce({
			json: async () => mockAssignableUsersResponse,
			ok: true,
		} as Response);

		const {getByText} = render(
			<BulkEditWorkflowAssigneeModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		fireEvent.click(getByText('cancel'));

		expect(mockCloseModal).toHaveBeenCalled();
	});

	it('intersects assignable users across all selected tasks', async () => {
		mockFetch.mockResolvedValueOnce({
			json: async () => mockAssignableUsersResponse,
			ok: true,
		} as Response);

		const {queryByText} = render(
			<BulkEditWorkflowAssigneeModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		await waitFor(() => {
			expect(queryByText('User B')).not.toBeInTheDocument();
		});
	});

	it('renders the modal and fetches assignable users', async () => {
		mockFetch.mockResolvedValueOnce({
			json: async () => mockAssignableUsersResponse,
			ok: true,
		} as Response);

		const {getByText} = render(
			<BulkEditWorkflowAssigneeModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		await waitFor(() => {
			expect(getByText('User A')).toBeInTheDocument();
		});
	});

	it('PATCHes assign-to-user endpoint and closes modal on success', async () => {
		mockFetch
			.mockResolvedValueOnce({
				json: async () => mockAssignableUsersResponse,
				ok: true,
			} as Response)
			.mockResolvedValueOnce({ok: true} as Response);

		const {getByText} = render(
			<BulkEditWorkflowAssigneeModalContent
				closeModal={mockCloseModal}
				dataSetId="test-fds"
				selectedData={mockSelectedData}
			/>
		);

		await waitFor(() => getByText('User A'));

		fireEvent.submit(getByText('save').closest('form')!);

		await waitFor(() => {
			expect(mockFetch).toHaveBeenCalledWith(
				'/o/headless-admin-workflow/v1.0/workflow-tasks/assign-to-user',
				expect.objectContaining({method: 'PATCH'})
			);
			expect(mockCloseModal).toHaveBeenCalled();
		});
	});
});
