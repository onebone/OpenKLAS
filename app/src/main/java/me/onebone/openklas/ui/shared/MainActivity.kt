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

package me.onebone.openklas.ui.shared

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import me.onebone.openklas.R
import me.onebone.openklas.base.PermissionExecutor
import me.onebone.openklas.base.PermissionHolder
import me.onebone.openklas.databinding.MainActivityBinding
import me.onebone.openklas.ui.home.HomeFragmentDirections
import me.onebone.openklas.klas.model.PostType
import me.onebone.openklas.widget.AppbarView

@AndroidEntryPoint
class MainActivity: AppCompatActivity(), AppbarHolder, PermissionHolder {
	private lateinit var binding: MainActivityBinding
	private val viewModel by viewModels<ActivityViewModel>()
	private lateinit var navController: NavController

	private var permissionRequestCode = 0
	private val permissionExecutors = mutableMapOf<Int, Pair<PermissionExecutor, PermissionExecutor>>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = MainActivityBinding.inflate(LayoutInflater.from(this)).apply {
			lifecycleOwner = this@MainActivity
		}
		setContentView(binding.root)

		binding.view = this

		binding.appbar.onClickDrawerListener = AppbarView.OnClickDrawerListener {
			if(binding.drawerRoot.isOpen) {
				binding.drawerRoot.close()
			}else{
				binding.drawerRoot.open()
			}
		}

		binding.navView.setNavigationItemSelectedListener(DrawerNavigationListener())

		val firebaseAnalytics = Firebase.analytics

		navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
		navController.addOnDestinationChangedListener { _, destination, _ ->
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, destination.label.toString())
				param(
					FirebaseAnalytics.Param.SCREEN_CLASS,
					(destination as? FragmentNavigator.Destination)?.className ?: "Not a fragment")
			}

			binding.drawerRoot.close()

			binding.drawerRoot.setDrawerLockMode(
				// lock at non-top level destination
				if(destination.id == R.id.fragment_home) DrawerLayout.LOCK_MODE_UNLOCKED
				else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
			)
		}
	}

	override fun configureAppbar(title: String, headerType: AppbarView.HeaderType, searchType: AppbarView.SearchType) {
		with(binding.appbar) {
			this.title = title
			this.headerType = headerType
			this.searchType = searchType
		}
	}

	override fun setAppbarOnClickSearchListener(listener: AppbarView.OnClickSearchListener?) {
		binding.appbar.onClickSearchListener = listener
	}

	override fun setAppbarSearchState(search: Boolean) {
		binding.appbar.searchType = if(search) AppbarView.SearchType.SEARCH else AppbarView.SearchType.CANCEL
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

				R.id.nav_syllabus_search ->
					HomeFragmentDirections.actionHomeSylsearch()

				R.id.nav_assignment_list ->
					HomeFragmentDirections.actionHomeAssignmentList(viewModel.currentSemester.value?.id, null)

				R.id.nav_grades ->
					HomeFragmentDirections.actionHomeGrades()

				else ->
					throw IllegalStateException("invalid drawer menu selected: ${item.itemId}")
			})

			return true
		}
	}

	override fun askPermissionAndDo(
		permissions: Array<String>,
		executor: PermissionExecutor,
		deniedExecutor: PermissionExecutor
	) {
		if(permissions.all { hasPermission(it) }) {
			executor(this)
			return
		}

		val requestCode = permissionRequestCode++
		ActivityCompat.requestPermissions(this, permissions, requestCode)
		permissionExecutors[requestCode] = Pair(executor, deniedExecutor)
	}

	override fun hasPermission(permission: String): Boolean {
		return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		val executors = permissionExecutors[requestCode]
		if(executors != null) {
			if(grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
				executors.first(this)
			}else{
				executors.second(this)
			}

			permissionExecutors.remove(requestCode)

			return
		}

		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}
}
