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

package me.onebone.openklas.ui.shared.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import me.onebone.openklas.R
import me.onebone.openklas.klas.model.BriefSubject
import me.onebone.openklas.klas.model.Semester

@Composable
fun SubjectSelectionDialog(
	semesters: List<Semester>?,
	currentSemester: Semester?,
	currentSubject: BriefSubject?,
	onChange: (Semester, BriefSubject) -> Unit,
	onDismissRequest: () -> Unit
) {
	var state by remember { mutableStateOf(DialogType.Subject) }

	var semesterSelection by remember { mutableStateOf(currentSemester) }
	var subjectSelection by remember { mutableStateOf(currentSubject) }

	Dialog(
		onDismissRequest = onDismissRequest
	) {
		// hack: Dialog composable constrains its maximum height by that of the first
		// composed content, so we have to fill height at the first time
		Box(
			modifier = Modifier
				.fillMaxHeight()
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onDismissRequest
				),
			contentAlignment = Alignment.Center
		) {
			Surface(
				modifier = Modifier
					.background(MaterialTheme.colors.surface)
					.pointerInput(Unit) {
						awaitPointerEventScope {
							while(true) {
								awaitPointerEvent().changes.forEach { it.consumeDownChange() }
							}
						}
					}
			) {
				@OptIn(ExperimentalAnimationApi::class)
				AnimatedContent(
					targetState = if(semesterSelection == null || state == DialogType.Semester) {
						DialogType.Semester
					}else{
						DialogType.Subject
					},
					transitionSpec = {
						when(targetState) {
							DialogType.Semester -> {
								slideInHorizontally({ -it }) with
										slideOutHorizontally({ it })
							}
							DialogType.Subject -> {
								slideInHorizontally({ it }) with
										slideOutHorizontally({ -it })
							}
						}
					}
				) { target ->
					when(target) {
						DialogType.Semester -> SelectionDialogContent(
							type = DialogType.Semester,
							contents = semesters,
							selectedEntry = semesterSelection,
							displayName = { it.label },
							onClickChangeType = { state = DialogType.Subject },
							onClickEntry = {
								semesterSelection = it
								state = DialogType.Subject
							}
						)
						DialogType.Subject -> SelectionDialogContent(
							type = DialogType.Subject,
							contents = semesterSelection?.subjects?.toList(),
							selectedEntry = subjectSelection,
							displayName = { it.name },
							onClickChangeType = { state = DialogType.Semester },
							onClickEntry = {
								subjectSelection = it
								onChange(semesterSelection!!, it)
							}
						)
					}
				}
			}
		}
	}
}

@Composable
internal fun <T> SelectionDialogContent(
	type: DialogType,
	contents: List<T>?,
	selectedEntry: T?,
	displayName: (T) -> String,
	onClickChangeType: (current: DialogType) -> Unit,
	onClickEntry: (T) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {
		DialogHeader(
			title = when(type) {
				DialogType.Semester -> stringResource(id = R.string.common_semester_dialog_title)
				DialogType.Subject -> stringResource(id = R.string.common_subject_dialog_title)
			},
			buttonAlignment = when(type) {
				DialogType.Semester -> Alignment.CenterEnd
				DialogType.Subject -> Alignment.CenterStart
			},
			onClickButton = {
				onClickChangeType(type)
			}
		) {
			NavigationButton(type)
		}

		if(contents != null) {
			SelectionItems(
				items = contents,
				selectedItem = { it === selectedEntry },
				displayName = displayName,
				onClick = {
					onClickEntry(it)
				}
			)
		}
	}
}

internal enum class DialogType {
	Semester, Subject
}

@Composable
internal fun NavigationButton(type: DialogType) {
	when(type) {
		DialogType.Semester -> {
			NavigationText(type)
			NavigationIcon(type)
		}
		DialogType.Subject -> {
			NavigationIcon(type)
			NavigationText(type)
		}
	}
}

@Composable
internal fun NavigationIcon(type: DialogType) {
	Icon(
		painterResource(when(type) {
			DialogType.Subject -> R.drawable.ic_arrow_back
			DialogType.Semester -> R.drawable.ic_arrow
		}),
		contentDescription = null,
		modifier = Modifier.padding(4.dp, 0.dp, 4.dp, 0.dp),
		tint = colorResource(R.color.primary)
	)
}

@Composable
internal fun NavigationText(type: DialogType) {
	Text(
		text = stringResource(when(type) {
			DialogType.Semester -> R.string.common_semester_dialog_title
			DialogType.Subject -> R.string.common_subject_dialog_title
		}),
		color = colorResource(R.color.primary),
		fontSize = 12.sp
	)
}

@Composable
fun DialogHeader(
	title: String,
	buttonAlignment: Alignment,
	onClickButton: () -> Unit,
	button: @Composable () -> Unit
) {
	Box(modifier = Modifier
		.fillMaxWidth()
		.padding(4.dp)
	) {
		Text(
			text = title,
			modifier = Modifier.align(Alignment.Center),
			fontWeight = Bold,
			fontSize = 18.sp
		)

		Row(modifier = Modifier
			.align(buttonAlignment)
			.padding(0.dp, 0.dp, 4.dp, 0.dp)
			.clickable(onClick = onClickButton)
			.padding(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			button()
		}
	}
}

@Composable
internal fun<T> SelectionItems(
	items: List<T>,
	selectedItem: (T) -> Boolean,
	displayName: (T) -> String,
	onClick: (T) -> Unit
) {
	LazyColumn(
		modifier = Modifier
			.fillMaxWidth()
	) {
		items(items) {
			Text(
				text = displayName(it),
				modifier = Modifier
					.fillMaxWidth()
					.clickable(onClick = { onClick(it) })
					.padding(24.dp, 12.dp),
				color =
					if(selectedItem(it)) colorResource(R.color.selected)
					else Color.Unspecified,
				fontSize = 15.sp
			)
		}
	}
}
