package org.openklas.klas.model

@Suppress("ArrayInDataClass")
data class Timetable (
	val entries: Array<Entry>
) {
	data class Entry (
		val day: Int,
		// wtLocHname_%d
		val classroom: String,
		// wtProfNm_%d
		val professor: String,
		// wtSpan_%d
		val length: Int,
		// wtSubjNm_%d
		val subjectName: String,
		// wtSubj_%d
		val subjectId: String,
		// wtYearhakgi_%d
		val semester: String,
		// wtSubjPrintSeq_%d
		// cannot figure out what exactly this is
		// but looks like it is only used to select
		// item color in timetable
		val printSeq: Int
	)
}
