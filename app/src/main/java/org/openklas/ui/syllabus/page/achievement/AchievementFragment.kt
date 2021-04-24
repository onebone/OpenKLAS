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

package org.openklas.ui.syllabus.page.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.openklas.R
import org.openklas.databinding.SyllabusPageAchievementFragmentBinding
import org.openklas.ui.syllabus.SyllabusViewModel
import org.openklas.widget.PieChartView

class AchievementFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = SyllabusPageAchievementFragmentBinding.inflate(inflater, container, false)

		val viewModel by viewModels<SyllabusViewModel>(ownerProducer = { requireParentFragment() })
		viewModel.syllabus.observe(viewLifecycleOwner) {
			val weights = it.scoreWeights
			binding.pieScoreWeights.entries = arrayOf(
				PieChartView.Entry(getString(R.string.syllabus_score_attendance), weights.attendance, getColor(R.color.score_weight_attendance)),
				PieChartView.Entry(getString(R.string.syllabus_score_midterm), weights.midterm, getColor(R.color.score_weight_midterm)),
				PieChartView.Entry(getString(R.string.syllabus_score_final_term), weights.finalTerm, getColor(R.color.score_weight_final_term)),
				PieChartView.Entry(getString(R.string.syllabus_score_report), weights.report, getColor(R.color.score_weight_report)),
				PieChartView.Entry(getString(R.string.syllabus_score_attitude), weights.attitude, getColor(R.color.score_weight_attitude)),
				PieChartView.Entry(getString(R.string.syllabus_score_quiz), weights.quiz, getColor(R.color.score_weight_quiz)),
				PieChartView.Entry(getString(R.string.syllabus_score_others), weights.others, getColor(R.color.score_weight_others))
			)

			val vl = it.vlCompetence
			binding.pieVlCompetence.entries = arrayOf(
				PieChartView.Entry(getString(R.string.syllabus_vl_profession), vl.profession, getColor(R.color.vl_profession)),
				PieChartView.Entry(getString(R.string.syllabus_vl_convergence_thinking), vl.convergenceThinking, getColor(R.color.vl_convergence_thinking)),
				PieChartView.Entry(getString(R.string.syllabus_vl_global_competence), vl.globalCompetence, getColor(R.color.vl_global_competence)),
				PieChartView.Entry(getString(R.string.syllabus_vl_social_competence), vl.socialCompetence, getColor(R.color.vl_social_competence)),
				PieChartView.Entry(getString(R.string.syllabus_vl_future_oriented), vl.futureOriented, getColor(R.color.vl_future_oriented)),
				PieChartView.Entry(getString(R.string.syllabus_vl_challenger_minded), vl.challengerMinded, getColor(R.color.vl_challenger_minded)),
				PieChartView.Entry(getString(R.string.syllabus_vl_sympathy), vl.sympathy, getColor(R.color.vl_sympathy))
			)
		}

		return binding.root
	}

	private fun getColor(@ColorRes resId: Int): Int {
		return ResourcesCompat.getColor(resources, resId, null)
	}
}
