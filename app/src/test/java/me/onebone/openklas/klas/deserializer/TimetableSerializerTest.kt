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

package me.onebone.openklas.klas.deserializer

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import me.onebone.openklas.klas.model.Timetable

class TimetableSerializerTest {
	@Test
	fun testTimetable() {
		val json = Json {
			ignoreUnknownKeys = true
		}

		val model = json.decodeFromString<Timetable>(TestJson)

		assertThat(model, `is`(equalTo(TestModel)))
	}
}

private val TestModel = Timetable(listOf(
	Timetable.Entry(
		day = 1, time = 0, classroom = "블리자드", professor = "아론 켈러", length = 2,
		subjectName = "오버워치승률통계학", subjectId = "U2021100000000001", semester = "2021,1",
		printSeq = 1
	),
	Timetable.Entry(
		day = 5, time = 0, classroom = "도쿄", professor = "렌고쿠", length = 1,
		subjectName = "5대기본호흡", subjectId = "U202110000000005", semester = "2021,1",
		printSeq = 2
	),
	Timetable.Entry(
		day = 2, time = 3, classroom = "M87", professor = "아인슈타인", length = 1,
		subjectName = "일반상대성이론실험", subjectId = "U2021100000000003", semester = "2021,1",
		printSeq = 3
	),
	Timetable.Entry(
		day = 6, time = 5, classroom = "어딘가", professor = "세르히오 마르키나", length = 5,
		subjectName = "조폐국인질극실습", subjectId = "U2021100000000004", semester = "2021,1",
		printSeq = 4
	)
))

private const val TestJson = """
[
	{
		"wtTime": 0, 
		"wtSpan_1": 2,
		"wtYearhakgi_1": "2021,1",
		"wtSubj_1": "U2021100000000001",
		"wtSubjNm_1": "오버워치승률통계학",
		"wtLocHname_1": "블리자드",
		"wtProfNm_1": "아론 켈러",
		"wtSubjPrintSeq_1": 1,

		"wtSpan_5": 1,
		"wtYearhakgi_5": "2021,1",
		"wtSubj_5": "U202110000000005",
		"wtSubjNm_5": "5대기본호흡",
		"wtLocHname_5": "도쿄",
		"wtProfNm_5": "렌고쿠",
		"wtSubjPrintSeq_5": 2
	},
	{
		"wtTime": 3,
		"wtSpan_2": 1,
		"wtYearhakgi_2": "2021,1",
		"wtSubj_2": "U2021100000000003",
		"wtSubjNm_2": "일반상대성이론실험",
		"wtLocHname_2": "M87",
		"wtProfNm_2": "아인슈타인",
		"wtSubjPrintSeq_2": 3
	},
	{
		"wtTime": 5,
		"wtSpan_6": 5,
		"wtYearhakgi_6": "2021,1",
		"wtSubj_6": "U2021100000000004",
		"wtSubjNm_6": "조폐국인질극실습",
		"wtLocHname_6": "어딘가",
		"wtProfNm_6": "세르히오 마르키나",
		"wtSubjPrintSeq_6": 4
	}
]
"""
