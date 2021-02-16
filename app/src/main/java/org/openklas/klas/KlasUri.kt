package org.openklas.klas

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

object KlasUri {
	const val ROOT_URI = "https://klas.kw.ac.kr"
	const val LOGIN_SECURITY = "$ROOT_URI/usr/cmn/login/LoginSecurity.do"
	const val LOGIN_CONFIRM = "$ROOT_URI/usr/cmn/login/LoginConfirm.do"
	const val STD_HOME = "$ROOT_URI/std/cmn/frame/StdHome.do"
	const val STD_SEMESTERS = "$ROOT_URI/std/cmn/frame/YearhakgiAtnlcSbjectList.do"

	const val STD_NOTICE_LIST = "$ROOT_URI/std/lis/sport/d052b8f845784c639f036b102fdc3023/BoardStdList.do"
	const val STD_MATERIAL_LIST = "$ROOT_URI/std/lis/sport/6972896bfe72408eb72926780e85d041/BoardStdList.do"
	const val STD_QNA_LIST = "$ROOT_URI/std/lis/sport/573f918c23984ae8a88c398051bb1263/BoardStdList.do"

	const val STD_SYLLABUS_LIST = "$ROOT_URI/std/cps/atnlc/LectrePlanStdList.do"
	const val STD_SYLLABUS = "$ROOT_URI/std/cps/atnlc/LectrePlanData.do"
	const val STD_TEACHING_ASSISTANT = "$ROOT_URI/std/cps/atnlc/LectreAstnt.do"

	const val STD_ONLINE_CONTENT_LIST = "$ROOT_URI/std/lis/evltn/SelectOnlineCntntsStdList.do"
}
