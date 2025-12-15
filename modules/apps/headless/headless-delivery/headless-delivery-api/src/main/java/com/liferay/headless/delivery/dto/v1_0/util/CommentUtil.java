/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.delivery.dto.v1_0.util;

import com.liferay.headless.delivery.dto.v1_0.Comment;
import com.liferay.message.boards.exception.DiscussionMaxCommentsException;
import com.liferay.message.boards.exception.MessageSubjectException;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.comment.DuplicateCommentException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import jakarta.ws.rs.ClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Javier Gamarra
 */
public class CommentUtil {

	public static Comment toComment(
			com.liferay.portal.kernel.comment.Comment comment,
			CommentManager commentManager, Portal portal)
		throws Exception {

		return _toComment(comment, commentManager, portal);
	}

	public static Comment toComment(
			UnsafeSupplier<com.liferay.portal.kernel.comment.Comment, Exception>
				addCommentUnsafeSupplier,
			CommentManager commentManager, Portal portal)
		throws Exception {

		try {
			return _toComment(
				addCommentUnsafeSupplier.get(), commentManager, portal);
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

	public static List<com.liferay.portal.kernel.comment.Comment> toComments(
			String className, long classPK, CommentManager commentManager,
			Comment[] comments, long companyId, long groupId, long userId)
		throws PortalException {

		if (groupId == 0) {
			Company company = CompanyLocalServiceUtil.getCompany(companyId);

			groupId = company.getGroupId();
		}

		long finalGroupId = groupId;

		Map<String, Long> toIdMap = new HashMap<>();

		List<Comment> sortedComments = _topologicallySortComments(comments);

		return TransformUtil.transformToList(
			sortedComments,
			comment -> {
				long parentCommentId = 0;

				String parentCommentExternalReferenceCode =
					comment.getParentCommentExternalReferenceCode();

				if (Validator.isNotNull(parentCommentExternalReferenceCode)) {
					Long parentExternalReferenceCodeId = toIdMap.get(
						parentCommentExternalReferenceCode);

					if (parentExternalReferenceCodeId != null) {
						parentCommentId = parentExternalReferenceCodeId;
					}
					else {
						com.liferay.portal.kernel.comment.Comment parentComment =
							commentManager.fetchComment(
								finalGroupId, parentCommentExternalReferenceCode);

						if (parentComment != null) {
							parentCommentId = parentComment.getCommentId();

							toIdMap.put(
								parentCommentExternalReferenceCode,
								parentCommentId);
						}
					}
				}

				com.liferay.portal.kernel.comment.Comment liferayComment =
					commentManager.createComment(
						0L, comment.getExternalReferenceCode(), userId,
						finalGroupId, className, classPK, parentCommentId,
						StringPool.BLANK, comment.getText());

				toIdMap.put(
					comment.getExternalReferenceCode(),
					liferayComment.getCommentId());

				return liferayComment;
			});

	}

	private static List<Comment> _topologicallySortComments(Comment[] comments)
		throws PortalException {

		Map<String, Comment> commentMap = new LinkedHashMap<>();

		for (Comment comment : comments) {
			commentMap.put(comment.getExternalReferenceCode(), comment);
		}

		List<Comment> sortedComments = new ArrayList<>();
		Set<String> visitingExternalReferenceCodes = new HashSet<>();
		Set<String> visitedExternalReferenceCodes = new HashSet<>();

		for (Comment comment : commentMap.values()) {
			if (visitedExternalReferenceCodes.contains(
					comment.getExternalReferenceCode())) {
				continue;
			}

			_sortComments(
				comment, commentMap, sortedComments,
				visitingExternalReferenceCodes, visitedExternalReferenceCodes);
		}

		return sortedComments;
	}

	private static void _sortComments(
		Comment comment, Map<String, Comment> commentMap,
		List<Comment> sortedComments, Set<String> visitingExternalReferenceCodes,
		Set<String> visitedExternalReferenceCodes)
	throws PortalException {

		String externalReferenceCode = comment.getExternalReferenceCode();

		if (visitedExternalReferenceCodes.contains(externalReferenceCode)) {
			return;
		}

		if (!visitingExternalReferenceCodes.add(externalReferenceCode)) {
			throw new PortalException(
					"Circular parent comment reference detected for external " +
						externalReferenceCode);
		}

		String parentCommentExternalReferenceCode =
			comment.getParentCommentExternalReferenceCode();

		if (Validator.isNotNull(parentCommentExternalReferenceCode)) {
			Comment parentComment = commentMap.get(
				parentCommentExternalReferenceCode);

			if (parentComment != null) {
				_sortComments(
					parentComment, commentMap, sortedComments,
					visitingExternalReferenceCodes, visitedExternalReferenceCodes);
			}
		}

		visitingExternalReferenceCodes.remove(externalReferenceCode);
		visitedExternalReferenceCodes.add(externalReferenceCode);

		sortedComments.add(comment);
	}

	private static Comment _toComment(
		com.liferay.portal.kernel.comment.Comment comment,
		CommentManager commentManager, Portal portal) {

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
						com.liferay.portal.kernel.comment.Comment
							parentComment = commentManager.fetchComment(
								comment.getParentCommentId());

						if (parentComment != null) {
							return parentComment.getExternalReferenceCode();
						}

						return null;
					});
				setParentCommentId(comment::getParentCommentId);
				setText(comment::getBody);
			}
		};
	}

}