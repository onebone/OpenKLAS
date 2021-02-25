package org.openklas.ui.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.SemesterViewModelDelegate
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
	semesterViewModelDelegate: SemesterViewModelDelegate
): ViewModel(), SemesterViewModelDelegate by semesterViewModelDelegate
