package de.westnordost.streetcomplete.osm.smoothness

import de.westnordost.streetcomplete.osm.Tags
import de.westnordost.streetcomplete.osm.getLastCheckDateKeys
import de.westnordost.streetcomplete.osm.updateWithCheckDate

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

fun parseSmoothness(smoothness: String?) : Smoothness? {
    if (smoothness == null) return null
    val parsedSmoothness = Smoothness.entries.find { it.osmValue == smoothness }
    return if (parsedSmoothness is Smoothness)
        parsedSmoothness
    else
        Smoothness.UNKNOWN
}

/** Apply the smoothness to the given [tags], with optional [prefix], e.g. "footway" for
 *  "footway:smoothness.
 *  By default the check date is also updated if the smoothness did not change, specified
 *  [updateCheckDate] = false if this should not be done. */
fun Smoothness.applyTo(tags: Tags, prefix: String? = null, updateCheckDate: Boolean = true) {
    requireNotNull(osmValue) { "Smoothness must be valid and not null" }

    val pre = if (prefix != null) "$prefix:" else ""
    val key = "${pre}smoothness"
    val previousOsmValue = tags[key]
    val hasChanged = previousOsmValue != null && previousOsmValue != osmValue

    // on change need to remove keys associated with (old) surface
    if (hasChanged) {
        getKeysAssociatedWithSmoothness(pre).forEach { tags.remove(it) }
    }

    // update surface + check date
    if (updateCheckDate) {
        tags.updateWithCheckDate(key, osmValue)
    } else {
        tags[key] = osmValue
    }

    // always clean up old source tags - source should be in changeset tags
    tags.remove("source:smoothness")
}

fun getKeysAssociatedWithSmoothness(prefix: String = ""): Set<String> =
    setOf(
        "${prefix}smoothness",
        "${prefix}smoothness:date",
        "source:${prefix}smoothness",
    ) +
        getLastCheckDateKeys("${prefix}smoothness")

// smoothness is not asked for steps
val ALL_PATHS_EXCEPT_STEPS = listOf("footway", "cycleway", "path", "bridleway")

// surfaces that are actually used in AddSmoothnessForm
// should only contain values that are in the Surface class
val SURFACES_FOR_SMOOTHNESS = listOf(
    "asphalt", "concrete", "concrete:plates", "sett", "paving_stones", "compacted", "gravel", "fine_gravel"
)

val ROADS_TO_ASK_SMOOTHNESS_FOR = arrayOf(
    // "trunk","trunk_link","motorway","motorway_link", // too much, motorways are almost by definition smooth asphalt (or concrete)
    "primary", "primary_link", "secondary", "secondary_link", "tertiary", "tertiary_link",
    "unclassified", "residential", "living_street", "pedestrian", "track", "busway",
    // "service", // this is too much (e.g. includes driveways), and the information value is very low
)
