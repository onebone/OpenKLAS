package org.openklas.ui.common

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.databinding.MainActivityBinding
import org.openklas.ui.home.HomeFragmentDirections
import org.openklas.ui.postlist.PostType
import org.openklas.widget.TitleView

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
	private val viewModel by viewModels<ActivityViewModel>()
	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val binding = MainActivityBinding.inflate(LayoutInflater.from(this)).apply {
			lifecycleOwner = this@MainActivity
		}
		setContentView(binding.root)

		binding.viewModel = viewModel
		binding.view = this

		binding.mainTitle.onClickDrawerListener = TitleView.OnClickDrawerListener {
			if(binding.drawerRoot.isOpen) {
				binding.drawerRoot.close()
			}else{
				binding.drawerRoot.open()
			}
		}

		binding.navView.setNavigationItemSelectedListener(DrawerNavigationListener())

		navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
		navController.addOnDestinationChangedListener { _, destination, _ ->
			binding.drawerRoot.close()

			binding.drawerRoot.setDrawerLockMode(
				// lock at non-top level destination
				if(destination.id == R.id.fragment_home) DrawerLayout.LOCK_MODE_UNLOCKED
				else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
			)
		}
	}

	fun onTitleClickBack(view: View) {
		this.onBackPressed()
	}

	internal inner class DrawerNavigationListener: NavigationView.OnNavigationItemSelectedListener {
		override fun onNavigationItemSelected(item: MenuItem): Boolean {
			navController.navigate(when(item.itemId) {
				R.id.nav_notice_list ->
					HomeFragmentDirections.actionHomePostList(viewModel.currentSemester.value?.id, PostType.NOTICE)

				R.id.nav_material_list ->
					HomeFragmentDirections.actionHomePostList(viewModel.currentSemester.value?.id, PostType.LECTURE_MATERIAL)

				R.id.nav_qna_list ->
					HomeFragmentDirections.actionHomePostList(viewModel.currentSemester.value?.id, PostType.QNA)

				else ->
					throw IllegalStateException("invalid drawer menu selected: ${item.itemId}")
			})

			return true
		}
	}
}
