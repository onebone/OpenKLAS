package org.openklas.ui.shared

import dagger.hilt.android.lifecycle.HiltViewModel
import org.openklas.base.BaseViewModel
import org.openklas.base.SemesterViewModelDelegate
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
	semesterViewModelDelegate: SemesterViewModelDelegate
): BaseViewModel(), SemesterViewModelDelegate by semesterViewModelDelegate
