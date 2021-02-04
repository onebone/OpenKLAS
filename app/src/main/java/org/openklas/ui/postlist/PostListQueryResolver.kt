package org.openklas.ui.postlist

/*
 * OpenKLAS
 * Copyright (C) 2020-2021 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.openklas.klas.model.BriefSubject
import org.openklas.klas.model.Semester
import org.openklas.utils.helper.PostListQuery
import org.openklas.utils.helper.PostListQueryCallback

/**
 * Resolves PostListQuery using data such as current semester, subject and post type
 * given from PostListViewModel. When resolved, it will call the listeners that the
 * query is ready.
 */
class PostListQueryResolver(
	private val defaultSubjectInitializer: (Array<BriefSubject>) -> BriefSubject? = {
		it.firstOrNull()
	}
) {
	private var semester: Semester? = null
	private var subjectId: String? = null
	private var subject: BriefSubject? = null
	private var type: PostType? = null

	var resolvedQuery: PostListQuery? = null
		private set

	private val listeners = mutableListOf<PostListQueryCallback>()

	fun addListener(listener: PostListQueryCallback) {
		listeners += listener
	}

	fun removeListener(listener: PostListQueryCallback) {
		listeners -= listener
	}

	fun setSemester(semester: Semester) {
		this.semester = semester

		tryResolveSubject()
		callListenersIfResolved()
	}

	fun setSubject(subject: String) {
		this.subjectId = subject

		tryResolveSubject()
		callListenersIfResolved()
	}

	fun setType(type: PostType) {
		this.type = type

		callListenersIfResolved()
	}

	private fun tryResolveSubject() {
		val semester = this.semester
		if(semester == null || subjectId == null) return

		val resolvedSubject = semester.subjects.find { subjectId == it.id }

		if(resolvedSubject == null) {
			if(this.subject == null) {
				// if subject is not resolved yet and there is no subject matching given [subjectId],
				// fallback to default initializer
				this.subject = defaultSubjectInitializer(semester.subjects)
			}

			// if subject is already resolved but [subjectId] does not match any of the subject
			// in current semester, ignore it.
		}else{
			this.subject = resolvedSubject
		}
	}

	private fun callListenersIfResolved() {
		val query = resolvedQuery

		if(query != null) {
			listeners.forEach { it.onQueryReady(query) }
		}

		if(semester != null && subject != null && type != null) {
			val resolved = PostListQuery(semester!!, subject!!, type!!)
			listeners.forEach { it.onQueryReady(resolved) }

			resolvedQuery = resolved
		}
	}
}
