/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';

import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import React from 'react';

import FolderService from '../../../../src/main/resources/META-INF/resources/js/common/services/FolderService';
import FolderItemSelectorModalContent from '../../../../src/main/resources/META-INF/resources/js/main_view/modal/FolderItemSelectorModalContent';
import {OBJECT_ENTRY_FOLDER_CLASS_NAME} from '../../../../src/main/resources/META-INF/resources/js/common/utils/constants';

const mockOpenToast = jest.fn();
const mockItemSelectorModal = jest.fn();
let mockSelectedItem: any = {};

jest.mock('@clayui/modal', () => ({
	useModal: () => ({
		observer: {},
		onOpenChange: jest.fn(),
		open: true,
	}),
}));

jest.mock('@liferay/frontend-js-item-selector-web', () => ({
	ItemSelectorModal: (props: any) => {
		mockItemSelectorModal(props);

		return (
			<button onClick={() => props.onItemsChange([mockSelectedItem])}>
				select-item
			</button>
		);
	},
}));

jest.mock('frontend-js-components-web', () => ({
	openToast: (props: any) => mockOpenToast(props),
}));

jest.mock('frontend-js-web', () => ({
	sub: (message: string, ...args: string[]) => `${message} ${args.join(' ')}`,
}));

jest.mock(
	'../../../../src/main/resources/META-INF/resources/js/common/services/FolderService',
	() => ({
		__esModule: true,
		default: {
			copyFolder: jest.fn(),
			copyReplaceFolder: jest.fn(),
			moveFolder: jest.fn(),
			moveReplaceFolder: jest.fn(),
			searchFolder: jest.fn(),
		},
	})
);

const DEFAULT_PROPS = {
	action: 'move' as const,
	assetLibraries: [
		{externalReferenceCode: 'default', groupId: 101, name: 'Space A'},
	],
	itemData: {
		actions: {},
		embedded: {
			id: 2001,
			scopeId: 101,
			title: 'Source Folder',
		},
		entryClassName: OBJECT_ENTRY_FOLDER_CLASS_NAME,
		id: 2001,
		title: 'Source Folder',
	},
	loadData: jest.fn(),
	objectEntryFolderExternalReferenceCode: 'folder-erc',
	rootObjectEntryFolderExternalReferenceCode: 'L_CONTENTS',
};

describe('FolderItemSelectorModalContent', () => {
	beforeEach(() => {
		jest.clearAllMocks();
		mockSelectedItem = {};

		(FolderService.searchFolder as jest.Mock).mockResolvedValue({
			data: {items: []},
		});
		(FolderService.moveFolder as jest.Mock).mockResolvedValue({
			error: null,
		});
	});

	it('uses embedded.title for folder card/table labels and locator', () => {
		render(<FolderItemSelectorModalContent {...DEFAULT_PROPS} />);

		const props = mockItemSelectorModal.mock.calls[0][0];
		const [cardsView, tableView] = props.fdsProps.views;

		expect(cardsView.schema.title).toBe('embedded.title');
		expect(tableView.schema.fields[0].fieldName).toBe('embedded.title');
		expect(props.locator.label).toBe('embedded.title');
	});

	it('preserves folder capitalization from embedded.title when selecting a folder', async () => {
		mockSelectedItem = {
			embedded: {
				id: 3001,
				parentObjectEntryFolderId: 999,
				title: 'My Mixed Case Folder',
			},
			title: 'my mixed case folder',
		};

		render(<FolderItemSelectorModalContent {...DEFAULT_PROPS} />);

		fireEvent.click(screen.getByRole('button', {name: 'select-item'}));

		await waitFor(() => {
			expect(FolderService.moveFolder).toHaveBeenCalledWith(2001, 3001);
		});

		expect(mockOpenToast).toHaveBeenCalledWith(
			expect.objectContaining({
				message: expect.stringContaining(
					'<strong>My Mixed Case Folder</strong>'
				),
			})
		);
	});
});
