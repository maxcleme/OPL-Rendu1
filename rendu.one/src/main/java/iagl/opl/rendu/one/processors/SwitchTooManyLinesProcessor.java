package iagl.opl.rendu.one.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtBreak;
import spoon.reflect.code.CtCFlowBreak;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtThrow;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.ReturnOrThrowFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class SwitchTooManyLinesProcessor extends AbstractProcessor<CtSwitch<?>> {

	private static final int THRESHOLD_SWITCH_MAXIMUM_LINES = 5;

	@Override
	public void process(CtSwitch<?> element) {
		int endSwitch = element.getPosition().getEndLine();
		int startUpper;
		int startCurrent;

		List<CtCase<?>> casesToBeProcessed = new ArrayList<>();
		List<?> cases = element.getCases();
		CtCase<?>[] arrayCases = cases.toArray(new CtCase<?>[cases.size()]);

		// Case 0 ... N-1
		for (int i = 0; i < arrayCases.length - 1; i++) {
			CtCase<?> upper = arrayCases[i];
			CtCase<?> caseElement = arrayCases[i + 1];
			startUpper = upper.getPosition().getLine();
			startCurrent = caseElement.getPosition().getLine();

			if (startCurrent - startUpper > THRESHOLD_SWITCH_MAXIMUM_LINES) {
				casesToBeProcessed.add(upper);
			}
			upper = caseElement;
		}

		// Case N
		if (endSwitch - arrayCases[arrayCases.length - 1].getPosition().getLine() > THRESHOLD_SWITCH_MAXIMUM_LINES) {
			casesToBeProcessed.add(arrayCases[arrayCases.length - 1]);
		}
		refactorCases(casesToBeProcessed);
	}

	// TODO : Split method in sub method
	private void refactorCases(List<CtCase<?>> casesToBeProcessed) {

		for (CtCase<?> caseElement : casesToBeProcessed) {
			CtClass<?> ctClass = caseElement.getParent(CtClass.class);
			CtMethod<?> ctParentMethod = caseElement.getParent(CtMethod.class);
			CtMethod<?> ctMethod = getFactory().Core().createMethod();

			List<CtCFlowBreak> flowBreaks = caseElement.getElements(new ReturnOrThrowFilter());

			CtReturn<?> ctReturn = (CtReturn<?>) flowBreaks.stream().filter(elem -> elem instanceof CtReturn).findAny()
					.orElse(null);
			CtTypeReference ctReturnType = ctReturn != null ? ctReturn.getReturnedExpression().getType()
					: getFactory().Core().createTypeReference().setSimpleName("void");

			Set<CtThrow> ctThrows = flowBreaks.stream().filter(elem -> elem instanceof CtThrow)
					.map(elem -> (CtThrow) elem).collect(Collectors.toSet());
			Set<CtTypeReference<? extends Throwable>> ctThrowTypes = ctThrows.stream()
					.map(elem -> elem.getThrownExpression().getType()).collect(Collectors.toSet());
			Set<CtVariableReference<Object>> variables = getVariables(caseElement);
			caseElement.setStatements(computeStatements(caseElement.getStatements()));

			ctMethod.setSimpleName(createMethodName(caseElement, ctParentMethod));
			ctMethod.addModifier(ModifierKind.PRIVATE);
			if (ctParentMethod.getModifiers().contains(ModifierKind.STATIC)) {
				ctMethod.addModifier(ModifierKind.STATIC);
			}
			ctMethod.setType(ctReturnType);
			ctMethod.setThrownTypes(ctThrowTypes);
			ctMethod.setBody(getFactory().Core().createBlock().setStatements(caseElement.getStatements().stream()
					.filter(statement -> !(statement instanceof CtBreak)).collect(Collectors.toList())));
			variables.forEach(var -> {
				CtParameter<?> param = getFactory().Core().createParameter();
				param.setSimpleName(var.getSimpleName());
				param.setType((CtTypeReference) var.getType());
				ctMethod.addParameter(param);
			});

			ctClass.addMethod(ctMethod);

			CtExecutableReference<Object> methodReference = (CtExecutableReference<Object>) getFactory().Method()
					.createReference(ctMethod);
			CtInvocation<Object> methodInvocation = getFactory().Core().createInvocation();
			methodInvocation.setExecutable(methodReference);
			variables.forEach(var -> {
				methodInvocation.addArgument(getFactory().Core().createVariableRead().setVariable(var));
			});

			System.out.println(ctMethod);

			caseElement.getStatements().removeIf(statement -> !(statement instanceof CtBreak));
			caseElement.getStatements().add(methodInvocation);
			Collections.reverse(caseElement.getStatements());
		}

	}

	private List<CtStatement> computeStatements(List<CtStatement> statements) {
		List<CtStatement> computedStatements = new ArrayList<>();
		statements.forEach(statement -> {
			if (statement instanceof CtBlock<?>) {
				computedStatements.addAll(((CtBlock) statement).getStatements());
			} else {
				computedStatements.add(statement);
			}
		});
		computedStatements.removeIf(statement -> statement instanceof CtBlock);
		return computedStatements;
	}

	/*
	 * Recupere les variables utiliser à l'interieur du case ( on enleve les
	 * variables local au case)
	 */
	private Set<CtVariableReference<Object>> getVariables(CtCase<?> caseElement) {
		List<CtVariableAccess<Object>> variablesAcccess = caseElement
				.getElements(new TypeFilter<CtVariableAccess<Object>>(CtVariableAccess.class));
		List<CtLocalVariable<Object>> localVariables = caseElement
				.getElements(new TypeFilter<CtLocalVariable<Object>>(CtLocalVariable.class));
		variablesAcccess.removeIf(var -> localVariables.contains(var));

		return variablesAcccess
				.stream().map(
						var -> var.getVariable())
				.filter(var -> !var.getModifiers().contains(ModifierKind.STATIC))
				.filter(var -> !localVariables.stream()
						.filter(localVar -> localVar.getSimpleName().equals(var.getSimpleName())).findAny().isPresent())
				.collect(Collectors.toSet());
	}

	private String createMethodName(CtCase<?> caseElement, CtMethod<?> ctParentMethod) {
		return ctParentMethod.getSimpleName() + "_case_" + Math.abs(caseElement.hashCode());
	}

}
