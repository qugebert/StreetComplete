package de.westnordost.streetcomplete.overlays.smoothness
import android.os.Bundle
import android.view.View
import de.westnordost.streetcomplete.data.osm.edits.update_tags.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.edits.update_tags.UpdateElementTagsAction
import de.westnordost.streetcomplete.data.preferences.Preferences
import de.westnordost.streetcomplete.osm.smoothness.asItem
import de.westnordost.streetcomplete.osm.smoothness.Smoothness
import de.westnordost.streetcomplete.osm.smoothness.parseSmoothness
import de.westnordost.streetcomplete.overlays.AImageSelectOverlayForm
import de.westnordost.streetcomplete.util.ktx.valueOfOrNull
import de.westnordost.streetcomplete.view.image_select.DisplayItem
import org.koin.android.ext.android.inject

class SmoothnessOverlayForm() : AImageSelectOverlayForm<Smoothness>()  {

    private var originalSmoothness: Smoothness? = null
    private val prefs: Preferences by inject()
    override val itemsPerRow = 1

    override val lastPickedItem: DisplayItem<Smoothness>? get() =
        prefs.getLastPicked(this::class.simpleName!!)
            .map { valueOfOrNull<Smoothness>(it)?.asItem() }
            .firstOrNull()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        originalSmoothness = parseSmoothness(element!!.tags["smoothness"])
        selectedItem = originalSmoothness?.asItem()
    }

    override fun hasChanges(): Boolean = selectedItem?.value != originalSmoothness

    override fun onClickOk() {
        prefs.addLastPicked(this::class.simpleName!!, selectedItem!!.value!!.name)
        val tagChanges = StringMapChangesBuilder(element!!.tags)
        //TODO: HÄÄÄH???
        tagChanges["smoothness"]=selectedItem!!.value?.osmValue.toString()
        applyEdit(UpdateElementTagsAction(element!!, tagChanges.create()))
    }

    override val items = Smoothness.entries.filter { it.osmValue != null }.map { it.asItem() }
    override val selectableItems = Smoothness.entries.filter { it.osmValue != null }.map { it.asItem() }


}
