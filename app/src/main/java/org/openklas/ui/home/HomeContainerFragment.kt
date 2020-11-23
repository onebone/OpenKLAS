package org.openklas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.openklas.R


class HomeContainerFragment : Fragment() {
	private var controller: NavController? = null
	private val navListener: OnDestinationChangedListener? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.home_container_fragment, container, false)

		val navHostFragment =
			childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		controller = navHostFragment.navController

		NavigationUI.setupWithNavController(
			view.findViewById<BottomNavigationView>(R.id.bottom_nav)!!,
			controller!!
		)

		return view
	}

	override fun onResume() {
		super.onResume()

		navListener?.let {
			controller?.addOnDestinationChangedListener(navListener)
		}
	}

	override fun onPause() {
		super.onPause()

		navListener?.let {
			controller?.removeOnDestinationChangedListener(navListener)
		}
	}
}
