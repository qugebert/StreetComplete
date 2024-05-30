package de.westnordost.streetcomplete.quests.powerpoles_material

import androidx.appcompat.app.AlertDialog
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AImageListQuestForm
import de.westnordost.streetcomplete.quests.AnswerItem

class AddPowerPolesMaterialForm : AImageListQuestForm<PowerPolesMaterial, PowerPolesMaterial>() {

    override val items = PowerPolesMaterial.values().map { it.asItem() }
    override val itemsPerRow = 3

    override val otherAnswers = listOf(
        AnswerItem(R.string.quest_powerPolesMaterial_terminal) { confirmIsTerminal() }
    )

    override fun onClickOk(selectedItems: List<PowerPolesMaterial>) {
        applyAnswer(selectedItems.single())
    }

    private fun confirmIsTerminal() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.quest_powerPolesMaterial_terminal_dialog_title)
            .setMessage(R.string.quest_powerPolesMaterial_terminal_dialog_description)
            .setPositiveButton(R.string.quest_generic_hasFeature_yes) { _, _ -> applyAnswer(
                PowerPolesMaterial.TERMINAL
            ) }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
