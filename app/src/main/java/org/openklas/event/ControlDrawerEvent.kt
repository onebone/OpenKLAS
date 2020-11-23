package org.openklas.event

data class ControlDrawerEvent(val mode: Mode = Mode.Close)

enum class Mode { Open, Close }