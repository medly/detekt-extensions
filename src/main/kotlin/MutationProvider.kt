import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import rules.MutationRule

class MutationProvider : RuleSetProvider {

    override val ruleSetId: String = "mutation"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            MutationRule()
        )
    )
}
