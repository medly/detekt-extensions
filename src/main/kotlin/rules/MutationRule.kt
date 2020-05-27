package rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameOrNull

class MutationRule : Rule() {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Mutation is not allowed in codebase",
        Debt(mins = 5)
    )

    override fun visitDeclaration(dcl: KtDeclaration) {
        super.visitDeclaration(dcl)
        if (dcl is KtProperty && dcl.isVar) {
            report(CodeSmell(issue, Entity.from(dcl), "Usage of var is not allowed in codebase"))
        }
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (bindingContext == BindingContext.EMPTY) return

        val resolvedCall = expression.getResolvedCall(bindingContext) ?: return
        val fqName = resolvedCall.resultingDescriptor.fqNameOrNull()?.asString()

        if (fqName != null && fqName in listOf(
                "kotlin.collections.mutableListOf",
                "kotlin.collections.mutableSetOf",
                "kotlin.collections.mutableMapOf"
            )
        ) {
            report(CodeSmell(issue, Entity.from(expression), "Usage of mutable collections is not allowed"))
        }
    }

    override fun visitParameter(parameter: KtParameter) {
        super.visitParameter(parameter)

        if (parameter.isMutable) {
            report(CodeSmell(issue, Entity.from(parameter), "Usage of mutable parameter is not allowed"))
        }
    }
}
