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

import android.app.Activity;
import android.os.Bundle;

import org.openklas.base.impl.port.ActivityInterfacePort;


@SuppressWarnings("SameParameterValue")
public interface ActivityInterface {

	default void startActivity(Class<?> cls) {
		ActivityInterfacePort.startActivity(cls, 0, null);
	}

	default void startActivity(Class<?> cls, int flags) {
		ActivityInterfacePort.startActivity(cls, flags, null);
	}

	default void startActivity(Class<?> cls, Bundle extras) {
		ActivityInterfacePort.startActivity(cls, 0, extras);
	}

	default void startActivity(Class<?> cls, int flags, Bundle extras) {
		ActivityInterfacePort.startActivity(cls, flags, extras);
	}

	default void finishActivity(Activity activity) {
		ActivityInterfacePort.finishActivity(activity);
	}

	default void finishActivity(Activity activity, boolean isLoadAnim) {
		ActivityInterfacePort.finishActivity(activity, isLoadAnim ? -1 : 0, isLoadAnim ? -1 : 0);
	}

	default void finishActivity(Activity activity, int enterAnim, int exitAnim) {
		ActivityInterfacePort.finishActivity(activity, enterAnim, exitAnim);
	}

	default void finishActivity(Class<?> cls) {
		ActivityInterfacePort.finishActivity(cls);
	}

	default void finishActivity(Class<?> cls, boolean isLoadAnim) {
		ActivityInterfacePort.finishActivity(cls, isLoadAnim ? -1 : 0, isLoadAnim ? -1 : 0);
	}

	default void finishActivity(Class<?> cls, int enterAnim, int exitAnim) {
		ActivityInterfacePort.finishActivity(cls, enterAnim, exitAnim);
	}

	default void finishAllActivities() {
		ActivityInterfacePort.finishAllActivities();
	}

	default void finishAllActivities(boolean isLoadAnim) {
		ActivityInterfacePort.finishAllActivities(isLoadAnim ? -1 : 0, isLoadAnim ? -1 : 0);
	}

	default void finishAllActivities(int enterAnim, int exitAnim) {
		ActivityInterfacePort.finishAllActivities(enterAnim, exitAnim);
	}
}
