package de.westnordost.streetcomplete.quests.tactile_paving

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AbstractQuestAnswerFragment
import de.westnordost.streetcomplete.quests.AnswerItem
import de.westnordost.streetcomplete.quests.tactile_paving.TactilePavingCrosswalkAnswer.YES
import de.westnordost.streetcomplete.quests.tactile_paving.TactilePavingCrosswalkAnswer.NO
import de.westnordost.streetcomplete.quests.tactile_paving.TactilePavingCrosswalkAnswer.INCORRECT

class TactilePavingCrosswalkForm : AbstractQuestAnswerFragment<TactilePavingCrosswalkAnswer>() {

    override val contentLayoutResId = R.layout.quest_tactile_paving

    override val otherAnswers get() = listOf(
        AnswerItem(R.string.quest_tactilePaving_incorrect) { applyAnswer(INCORRECT) }
    )

    override val buttonPanelAnswers = listOf(
        AnswerItem(R.string.quest_generic_hasFeature_no) { applyAnswer(NO) },
        AnswerItem(R.string.quest_generic_hasFeature_yes) { applyAnswer(YES) }
    )
}
