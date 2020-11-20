package org.openklas.main

import android.os.Bundle
import com.github.windsekirun.daggerautoinject.InjectActivity
import org.openklas.R
import org.openklas.base.BaseActivity
import org.openklas.databinding.HomeActivityBinding

@InjectActivity
class HomeActivity : BaseActivity<HomeActivityBinding>() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.home_activity)

		val viewModel = getViewModel(HomeViewModel::class.java)
		mBinding.viewModel = viewModel

		// TODO dynamically assign semester id
		viewModel.semester.value = "2020,2"
	}
}
