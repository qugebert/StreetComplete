package de.westnordost.streetcomplete.overlays.smoothness

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.mapdata.Element
import de.westnordost.streetcomplete.data.osm.mapdata.MapDataWithGeometry
import de.westnordost.streetcomplete.data.osm.mapdata.filter
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.BICYCLIST
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.CAR
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.WHEELCHAIR
import de.westnordost.streetcomplete.osm.ALL_PATHS
import de.westnordost.streetcomplete.osm.ALL_ROADS
import de.westnordost.streetcomplete.osm.smoothness.Smoothness
import de.westnordost.streetcomplete.osm.smoothness.parseSmoothness
import de.westnordost.streetcomplete.overlays.Color
import de.westnordost.streetcomplete.overlays.Overlay
import de.westnordost.streetcomplete.overlays.PolygonStyle
import de.westnordost.streetcomplete.overlays.PolylineStyle
import de.westnordost.streetcomplete.overlays.StrokeStyle
import de.westnordost.streetcomplete.overlays.Style
import de.westnordost.streetcomplete.quests.smoothness.AddPathSmoothness
import de.westnordost.streetcomplete.quests.smoothness.AddRoadSmoothness

class SmoothnessOverlay : Overlay {

    override val changesetComment = "Specify smoothness"
    override val icon = R.drawable.ic_quest_street_surface_detail
    override val title = R.string.quest_smoothness_road_title
    override val wikiLink: String = "Key:smoothness"
    override val achievements = listOf(CAR, BICYCLIST, WHEELCHAIR)
    override val hidesQuestTypes = setOf(
        AddRoadSmoothness::class.simpleName!!,
        AddPathSmoothness::class.simpleName!!,
    )
    //TODO: Muss das auch noch im AchievementsModule als Alias gelistet werden?

    /* Von surface-overlay übernommen.
    * TODO: Gleiche Auswahl ok?? Nochmal nachsehen, was die beiden Smoothness-Quests abfragen */
    override fun getStyledElements(mapData: MapDataWithGeometry) =
        mapData.filter("""
            ways, relations with
                highway ~ ${(ALL_PATHS + ALL_ROADS).joinToString("|")}
        """).map { it to getStyle(it) }

// TODO: Von Surface-Overlay übernommen. Nochmal drüber nachdenken.
    private fun getStyle(element: Element): Style {
        val tags = element.tags
        val isArea = tags["area"] == "yes"
        val isSegregated = tags["segregated"] == "yes"
        val isPath = tags["highway"] in ALL_PATHS

        val color = if (isPath && isSegregated) {
            val footwayColor = parseSmoothness(tags["footway:smoothness"]).getColor(element)
            val cyclewayColor = parseSmoothness(tags["cycleway:smoothness"]).getColor(element)
            // take worst case for showing
            listOf(footwayColor, cyclewayColor).minBy { color ->
                when (color) {
                    Color.DATA_REQUESTED -> 0
                    Color.INVISIBLE -> 1
                    Color.BLACK -> 2
                    else -> 3
                }
            }
        } else {
            parseSmoothness(tags["smoothness"]).getColor(element)
        }
        return if (isArea) PolygonStyle(color) else PolylineStyle(StrokeStyle(color))
    }

    private fun Smoothness?.getColor(element: Element): String =
        this?.color ?: Color.DATA_REQUESTED

    /* TODO: Reconsider Colors */
    private val Smoothness.color get() = when (this) {
        Smoothness.EXCELLENT -> Color.BLUE
        Smoothness.GOOD -> Color.SKY
        Smoothness.INTERMEDIATE -> Color.CYAN
        Smoothness.BAD -> Color.AQUAMARINE
        Smoothness.VERY_BAD -> Color.TEAL
        Smoothness.HORRIBLE -> Color.ORANGE
        Smoothness.VERY_HORRIBLE -> Color.LIME
        Smoothness.IMPASSABLE -> Color.GRAY
        Smoothness.UNKNOWN -> Color.BLACK
    }

    override fun createForm(element: Element?) = SmoothnessOverlayForm()
    }


