/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.delivery.dto.v1_0.util;

import com.liferay.headless.delivery.dto.v1_0.Comment;
import com.liferay.message.boards.exception.DiscussionMaxCommentsException;
import com.liferay.message.boards.exception.MessageSubjectException;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.comment.DuplicateCommentException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import jakarta.ws.rs.ClientErrorException;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javier Gamarra
 */
public class CommentUtil {

	public static Comment toComment(
			com.liferay.portal.kernel.comment.Comment comment,
			CommentManager commentManager, Portal portal)
		throws Exception {

		return _toComment(comment, commentManager, portal, null);
	}

	public static Comment toComment(
			com.liferay.portal.kernel.comment.Comment comment,
			CommentManager commentManager, Portal portal,
			Map<Long, String> parentCommentExternalReferenceCodes)
		throws Exception {

		return _toComment(
			comment, commentManager, portal,
			parentCommentExternalReferenceCodes);
	}

	public static Comment toComment(
			UnsafeSupplier<com.liferay.portal.kernel.comment.Comment, Exception>
				addCommentUnsafeSupplier,
			CommentManager commentManager, Portal portal)
		throws Exception {

		try {
			return _toComment(
				addCommentUnsafeSupplier.get(), commentManager, portal, null);
		}
		catch (DiscussionMaxCommentsException discussionMaxCommentsException) {
			throw new ClientErrorException(
				"Maximum number of comments has been reached", 422,
				discussionMaxCommentsException);
		}
		catch (DuplicateCommentException duplicateCommentException) {
			throw new ClientErrorException(
				"A comment with the same text already exists", 409,
				duplicateCommentException);
		}
		catch (MessageSubjectException messageSubjectException) {
			throw new ClientErrorException(
				"Comment text is null", 422, messageSubjectException);
		}
	}

	public static CommentBatch toComments(
			String className, long classPK, CommentManager commentManager,
			Comment[] comments, long companyId, long groupId, long userId)
		throws PortalException {

		if (groupId == 0) {
			Company company = CompanyLocalServiceUtil.getCompany(companyId);

			groupId = company.getGroupId();
		}

		long finalGroupId = groupId;

                List<CommentBatch.Comment> serviceBuilderComments = new ArrayList<>();
                Map<String, String> parentCommentExternalReferenceCodes =
                        new HashMap<>();
                Map<String, Long> resolvedParentCommentIds = new HashMap<>();

		for (Comment comment : comments) {
			String parentCommentExternalReferenceCode =
				comment.getParentCommentExternalReferenceCode();

			if (Validator.isNotNull(parentCommentExternalReferenceCode)) {
				parentCommentExternalReferenceCodes.put(
					comment.getExternalReferenceCode(),
					parentCommentExternalReferenceCode);

				com.liferay.portal.kernel.comment.Comment parentComment =
					commentManager.fetchComment(
						finalGroupId, parentCommentExternalReferenceCode);

				if (parentComment != null) {
					resolvedParentCommentIds.put(
						parentCommentExternalReferenceCode,
						parentComment.getCommentId());
				}
			}

                        serviceBuilderComments.add(
                                new CommentBatch.Comment(
                                        comment.getExternalReferenceCode(),
                                        comment.getText()));
                }

                return new CommentBatch(
                        serviceBuilderComments, parentCommentExternalReferenceCodes,
                        resolvedParentCommentIds);
	}

	public static class CommentBatch {

                public CommentBatch(
                        List<Comment> comments,
                        Map<String, String> parentExternalReferenceCodes,
                        Map<String, Long> resolvedParentCommentIds) {

                        _comments = comments;
                        _parentExternalReferenceCodes = parentExternalReferenceCodes;
                        _resolvedParentCommentIds = resolvedParentCommentIds;
                }

                public List<Comment> getComments() {
                        return _comments;
                }

                public Map<String, String> getParentExternalReferenceCodes() {
                        return _parentExternalReferenceCodes;
                }

		public Map<String, Long> getResolvedParentCommentIds() {
			return _resolvedParentCommentIds;
		}

                public static class Comment implements Serializable {

                        public Comment(String externalReferenceCode, String text) {
                                _externalReferenceCode = externalReferenceCode;
                                _text = text;
                        }

                        public String getExternalReferenceCode() {
                                return _externalReferenceCode;
                        }

                        public String getText() {
                                return _text;
                        }

                        private static final long serialVersionUID = 1L;

                        private final String _externalReferenceCode;
                        private final String _text;

                }

                private final List<Comment> _comments;
                private final Map<String, String> _parentExternalReferenceCodes;
                private final Map<String, Long> _resolvedParentCommentIds;

        }

	private static Comment _toComment(
		com.liferay.portal.kernel.comment.Comment comment,
		CommentManager commentManager, Portal portal,
		Map<Long, String> parentCommentExternalReferenceCodes) {

		if (comment == null) {
			return null;
		}

		return new Comment() {
			{
				setCreator(
					() -> CreatorUtil.toCreator(
						null, portal, comment.getUser()));
				setDateCreated(comment::getCreateDate);
				setDateModified(comment::getModifiedDate);
				setExternalReferenceCode(comment::getExternalReferenceCode);
				setId(comment::getCommentId);
			setNumberOfComments(
				() -> commentManager.getChildCommentsCount(
						comment.getCommentId(),
						WorkflowConstants.STATUS_APPROVED));
			setParentCommentExternalReferenceCode(
				() -> {
					long parentCommentId = comment.getParentCommentId();

				                if (parentCommentId == 0) {
				                        return null;
				                }

				                if (parentCommentExternalReferenceCodes != null) {
				                        if (parentCommentExternalReferenceCodes.containsKey(
				                                        parentCommentId)) {

				                                return parentCommentExternalReferenceCodes.get(
				                                        parentCommentId);
				                        }
				                }

				                com.liferay.portal.kernel.comment.Comment
				                        parentComment = commentManager.fetchComment(
				                                parentCommentId);

				                String parentCommentExternalReferenceCode = null;

				                if (parentComment != null) {
				                        parentCommentExternalReferenceCode =
				                                parentComment.getExternalReferenceCode();
				                }

				                if (parentCommentExternalReferenceCodes != null) {
				                        parentCommentExternalReferenceCodes.put(
				                                parentCommentId, parentCommentExternalReferenceCode);
				                }

				                return parentCommentExternalReferenceCode;
				        });
				setParentCommentId(comment::getParentCommentId);
				setText(comment::getBody);
			}
		};
	}

}
