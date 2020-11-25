package org.openklas.base.impl;

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
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

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.StringRes;


import org.openklas.base.impl.port.AlertInterfacePort;

import java.util.List;

import pyxis.uzuki.live.richutilskt.impl.F1;
import pyxis.uzuki.live.richutilskt.module.reference.ActivityReference;

@SuppressWarnings("SameParameterValue")
public interface AlertInterface {

	default void showToast(String message) {
		AlertInterfacePort.showToast(message);
	}

	default void showAlertDialog(String message) {
		AlertInterfacePort.showAlertDialog("", message, null);
	}

	default void showAlertDialog(String title, String message) {
		AlertInterfacePort.showAlertDialog(title, message, null);
	}

	default void showAlertDialog(int messageResId) {
		AlertInterfacePort.showAlertDialog("", getString(messageResId), null);
	}

	default void showAlertDialog(int titleResId, int messageResId) {
		AlertInterfacePort.showAlertDialog(getString(titleResId), getString(messageResId), null);
	}

	default void showAlertDialog(String message, DialogInterface.OnClickListener positiveListener) {
		AlertInterfacePort.showAlertDialog("", message, positiveListener);
	}

	default void showAlertDialog(String title, String message, DialogInterface.OnClickListener positiveListener) {
		AlertInterfacePort.showAlertDialog(title, message, positiveListener);
	}

	default void showSelectorDialog(List<String> items, F1<Integer> callback) {
		AlertInterfacePort.showSelectorDialog(items, callback);
	}

	default void showConfirmDialog(String message) {
		AlertInterfacePort.showConfirmDialog("", message, null, null);
	}

	default void showConfirmDialog(String title, String message) {
		AlertInterfacePort.showConfirmDialog(title, message, null, null);
	}

	default void showConfirmDialog(int messageResId) {
		showConfirmDialog("", getString(messageResId), null, null);
	}

	default void showConfirmDialog(int titleResId, int messageResId) {
		AlertInterfacePort.showConfirmDialog(getString(titleResId), getString(messageResId), null, null);
	}

	default void showConfirmDialog(String message, DialogInterface.OnClickListener positiveListener) {
		AlertInterfacePort.showConfirmDialog("", message, positiveListener, null);
	}

	default void showConfirmDialog(String title, String message, DialogInterface.OnClickListener positiveListener) {
		showConfirmDialog(title, message, positiveListener, null);
	}

	default void showConfirmDialog(int messageResId, DialogInterface.OnClickListener positiveListener) {
		AlertInterfacePort.showConfirmDialog("", getString(messageResId), positiveListener, null);
	}

	default void showConfirmDialog(int titleResId, int messageResId, DialogInterface.OnClickListener positiveListener) {
		AlertInterfacePort.showConfirmDialog(getString(titleResId), getString(messageResId), positiveListener, null);
	}

	default void showConfirmDialog(String message, DialogInterface.OnClickListener positiveListener,
	                               DialogInterface.OnClickListener negativeListener) {
		AlertInterfacePort.showConfirmDialog("", message, positiveListener, negativeListener);
	}

	default void showConfirmDialog(String title, String message, DialogInterface.OnClickListener positiveListener,
	                               DialogInterface.OnClickListener negativeListener) {
		AlertInterfacePort.showConfirmDialog(title, message, positiveListener, negativeListener);
	}

	default DialogInterface showProgressDialog(String message) {
		return AlertInterfacePort.showProgressDialog("", message);
	}

	default DialogInterface showProgressDialog(String title, String message) {
		return AlertInterfacePort.showProgressDialog(title, message);
	}

	static String getString(@StringRes int resource) {
		Context context = ActivityReference.getContext();
		return context.getString(resource);
	}
}
