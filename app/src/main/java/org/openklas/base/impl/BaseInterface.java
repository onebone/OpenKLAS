package org.openklas.base.impl;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import org.openklas.base.BaseViewModel;

@SuppressWarnings("ALL")
public interface BaseInterface extends ActivityInterface {

	default <T extends BaseViewModel> T getViewModel(FragmentActivity owner, ViewModelProvider.Factory factory,
	                                                 Class<T> viewModelClass) {
		return ViewModelProviders.of(owner, factory).get(viewModelClass);
	}

	default <T extends BaseViewModel> T getViewModel(Fragment owner, ViewModelProvider.Factory factory,
	                                                 Class<T> viewModelClass) {
		return ViewModelProviders.of(owner, factory).get(viewModelClass);
	}
}
