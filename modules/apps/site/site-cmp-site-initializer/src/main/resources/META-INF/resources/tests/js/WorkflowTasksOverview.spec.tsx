/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';
import {render, waitFor} from '@testing-library/react';
import React from 'react';

import WorkflowTasksOverview from '../../js/components/task/WorkflowTasksOverview';
import {mockFetch} from '../js/__mocks__/frontend-js-web';

function mockWorkflowTasksResponse(totalCount: number) {
	return {
		json: async () => ({totalCount}),
		ok: true,
	} as Response;
}

describe('WorkflowTasksOverview', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	it('shows loading indicator before fetch completes', () => {
		mockFetch.mockImplementation(() => new Promise(() => {}));

		const {container} = render(<WorkflowTasksOverview />);

		expect(
			container.querySelector('.loading-animation')
		).toBeInTheDocument();
	});

	it('returns null when API fetch fails', async () => {
		mockFetch.mockRejectedValueOnce(new Error('network error'));

		const {container} = render(<WorkflowTasksOverview />);

		await waitFor(() => {
			expect(container.firstChild).toBeNull();
		});
	});

	it('returns null when total count is zero', async () => {
		mockFetch
			.mockResolvedValueOnce(mockWorkflowTasksResponse(0))
			.mockResolvedValueOnce(mockWorkflowTasksResponse(0));

		const {container} = render(<WorkflowTasksOverview />);

		await waitFor(() => {
			expect(container.firstChild).toBeNull();
		});
	});

	it('renders pending and completed counts', async () => {
		mockFetch
			.mockResolvedValueOnce(mockWorkflowTasksResponse(20))
			.mockResolvedValueOnce(mockWorkflowTasksResponse(30));

		const {getByText} = render(<WorkflowTasksOverview />);

		await waitFor(() => {
			expect(getByText('30')).toBeInTheDocument();
			expect(getByText('pending')).toBeInTheDocument();
			expect(getByText('20')).toBeInTheDocument();
			expect(getByText('completed')).toBeInTheDocument();
		});
	});
});
