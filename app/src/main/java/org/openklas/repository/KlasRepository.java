package org.openklas.repository;

import androidx.annotation.NonNull;

import org.openklas.klas.model.Home;
import org.openklas.klas.model.Semester;

import io.reactivex.Single;

public interface KlasRepository {
	Single<String> performLogin(@NonNull String username, @NonNull String password);
	Single<Home> getHome(@NonNull String semester);
	Single<Semester[]> getSemesters();
}
