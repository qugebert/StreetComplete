package de.westnordost.streetcomplete.osm.smoothness

data class SmoothnessAndNote(val smoothness: Smoothness?, val note: String? = null)

enum class Smoothness(val osmValue: String?) {
    EXCELLENT("excellent"),
    GOOD("good"),
    INTERMEDIATE("intermediate"),
    BAD("bad"),
    VERY_BAD("very_bad"),
    HORRIBLE("horrible"),
    VERY_HORRIBLE("very_horrible"),
    IMPASSABLE("impassable"),
    UNKNOWN(null),
}

fun parseSmoothness(smoothness: String?) : Smoothness {
    if (smoothness == null) return Smoothness.UNKNOWN
    val parsedSmoothness = Smoothness.entries.find { it.osmValue == smoothness }
    return if (parsedSmoothness is Smoothness)
        parsedSmoothness
    else
        Smoothness.UNKNOWN
}
