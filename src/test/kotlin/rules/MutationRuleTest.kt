package rules

import io.gitlab.arturbosch.detekt.test.KtTestCompiler
import io.gitlab.arturbosch.detekt.test.compileAndLint
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class MutationRuleTest : Spek({
    val wrapper by memoized(
            factory = { KtTestCompiler.createEnvironment() },
            destructor = { it.dispose() }
    )

    describe("Mutation testing") {
        it("should report code smell") {
            val code = """var a = 1"""

            val findings = MutationRule().compileAndLint(code)
            Assertions.assertThat(findings).hasSize(1)
        }
        it("should not report code smell") {
            val code = """val a = 1"""

            val findings = MutationRule().compileAndLint(code)
            Assertions.assertThat(findings).hasSize(0)
        }
        it("should report code smell") {
            val code = """
                    class Foo(var a: Int) 
                    """
            val findings = MutationRule().compileAndLint(code)
            Assertions.assertThat(findings).hasSize(1)
        }
        it("should detect calls of mutable collections") {
            val code = """          
                        val list = mutableListOf(1, 2)
                        val set = mutableSetOf(1, 2)
                        val map = mutableMapOf(Pair(1, 2))
                        """
            val findings = MutationRule().compileAndLintWithContext(wrapper.env, code)
            Assertions.assertThat(findings).hasSize(3)
        }
    }
})

