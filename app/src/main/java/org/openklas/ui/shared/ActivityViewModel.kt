package org.openklas.ui.shared

import androidx.hilt.lifecycle.ViewModelInject
import org.openklas.base.BaseViewModel
import org.openklas.base.SemesterViewModelDelegate

class ActivityViewModel @ViewModelInject constructor(
	semesterViewModelDelegate: SemesterViewModelDelegate
): BaseViewModel(), SemesterViewModelDelegate by semesterViewModelDelegate
