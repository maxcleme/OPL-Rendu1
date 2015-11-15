package iagl.opl.rendu.one.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtBreak;
import spoon.reflect.code.CtCFlowBreak;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtOperatorAssignment;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtThrow;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.ReturnOrThrowFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.visitor.filter.VariableAccessFilter;

public class SwitchTooManyLinesProcessor extends AbstractProcessor<CtSwitch<?>> {

	private static final int THRESHOLD_SWITCH_MAXIMUM_LINES = 5;

	private CtClass reference;

	@Override
	public void process(CtSwitch<?> element) {
		createReferenceClass();
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

	private void createReferenceClass() {
		if (reference == null) {
			reference = getFactory().Class().create(getFactory().Package().getOrCreate("iagl.iopl.generated"),
					"Reference");

			CtTypeReference genericType = getFactory().Core().createTypeReference().setSimpleName("T");

			reference.addFormalTypeParameter(genericType);
			CtConstructor<Object> constructor = getFactory().Core().createConstructor();

			CtField referenceField = getFactory().Core().createField();
			referenceField.addModifier(ModifierKind.PRIVATE);
			referenceField.setSimpleName("ref");
			referenceField.setType(genericType);
			reference.addField(referenceField);

			CtParameter<?> param = getFactory().Core().createParameter();
			param.setSimpleName("ref");
			param.setType(genericType);

			CtMethod<?> ctGetter = getFactory().Core().createMethod();
			ctGetter.setType(genericType);
			ctGetter.addModifier(ModifierKind.PUBLIC);
			ctGetter.setSimpleName("getRef");
			ctGetter.setBody(getFactory().Core().createBlock()
					.addStatement(getFactory().Code().createCodeSnippetStatement("return this.ref")));

			CtMethod<?> ctSetter = getFactory().Core().createMethod();
			ctSetter.setType(getFactory().Core().createTypeReference().setSimpleName("void"));
			ctSetter.addModifier(ModifierKind.PUBLIC);
			ctSetter.setSimpleName("setRef");
			ctSetter.addParameter(param);
			ctSetter.setBody(getFactory().Core().createBlock()
					.addStatement(getFactory().Code().createCodeSnippetStatement("this.ref = ref")));

			constructor.addModifier(ModifierKind.PUBLIC);
			constructor.addParameter(param);
			constructor.setBody(getFactory().Code()
					.createCtBlock(getFactory().Code().createCodeSnippetStatement("this.ref = ref")));

//			System.out.println(reference.getPackage());
			reference.addModifier(ModifierKind.PUBLIC);
			reference.addConstructor(constructor);
			reference.addMethod(ctGetter);
			reference.addMethod(ctSetter);
//			System.out.println(reference);
		}
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
			CtTypeReference ctReturnType = ctReturn != null ? ctParentMethod.getType()
					: getFactory().Core().createTypeReference().setSimpleName("void");

			Set<CtThrow> ctThrows = flowBreaks.stream().filter(elem -> elem instanceof CtThrow)
					.map(elem -> (CtThrow) elem).collect(Collectors.toSet());
			Set<CtTypeReference<? extends Throwable>> ctThrowTypes = ctThrows.stream()
					.map(elem -> elem.getThrownExpression().getType()).collect(Collectors.toSet());
			workaroundVariableInit(getVariables(caseElement), caseElement);
			System.out.println("///////");
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
			getVariables(caseElement).stream().map(variableReference -> variableReference.getDeclaration()).collect(Collectors.toSet()).stream().filter(var -> var != null).forEach(var -> {
					CtParameter<Object> param = getFactory().Core().createParameter();
					param.setSimpleName(var.getSimpleName());
					param.setType( var.getType());
					ctMethod.addParameter(param);
			});
			if (ctReturnType != null && !ctReturnType.getSimpleName().equals("void")
					&& !(ctMethod.getBody().getLastStatement() instanceof CtReturn)
					&& !(ctMethod.getBody().getLastStatement() instanceof CtThrow)) {
				ctMethod.getBody().addStatement(getFactory().Core().createReturn()
						.setReturnedExpression(getFactory().Code().createCodeSnippetExpression("null")));
			}

			ctClass.addMethod(ctMethod);

			CtExecutableReference<Object> methodReference = (CtExecutableReference<Object>) getFactory().Method()
					.createReference(ctMethod);
			CtInvocation<Object> methodInvocation = getFactory().Core().createInvocation();
			methodInvocation.setExecutable(methodReference);
			getVariables(caseElement).stream().map(variableReference -> variableReference.getDeclaration()).collect(Collectors.toSet()).stream().filter(var -> var != null).forEach(var -> {
				methodInvocation.addArgument(getFactory().Core().createVariableRead().setVariable(var.getReference()));
			});

//			System.out.println(ctMethod);
			
			if ( caseElement.getStatements().size() == 1 && caseElement.getStatements().get(0) instanceof CtBlock ){
				caseElement.setStatements(((CtBlock)(caseElement.getStatements().get(0))).getStatements());
			}

			if (ctReturnType != null && !ctReturnType.getSimpleName().equals("void")) {
				if ( caseElement.getStatements().get(caseElement.getStatements().size()-1) instanceof CtReturn ){
					caseElement.getStatements().clear();
					caseElement.getStatements()
							.add(getFactory().Core().createReturn().setReturnedExpression(methodInvocation));
				}else{
					caseElement.getStatements().removeIf(statement -> !(statement instanceof CtBreak));
					CtAssignment<Object, Object> assignment = getFactory().Core().createAssignment();
					String varName = "var_"+ctMethod.getSimpleName();
					assignment.setAssigned(getFactory().Code().createCodeSnippetExpression(ctReturnType.getSimpleName()+" "+varName));
					assignment.setAssignment(methodInvocation);
					CtIf returnIf = getFactory().Core().createIf();
					returnIf.setCondition(getFactory().Code().createCodeSnippetExpression(varName +" != null"));
					returnIf.setThenStatement(getFactory().Code().createCodeSnippetStatement("return "+varName));
					caseElement.getStatements().add(returnIf);
					caseElement.getStatements().add(assignment);
					Collections.reverse(caseElement.getStatements());
				}
				
			} else {
				caseElement.getStatements().removeIf(statement -> !(statement instanceof CtBreak));
				caseElement.getStatements().add(methodInvocation);
				Collections.reverse(caseElement.getStatements());
			}
		}

	}


	private void workaroundVariableInit(Set<CtVariableReference<Object>> variables, CtCase<?> caseElement) {
		variables.stream()
				.filter(var -> var.getDeclaration() instanceof CtLocalVariable)
				.filter(var -> !(var.getDeclaration().getParent() instanceof CtForEach))
				.forEach(var -> {					
					if ( caseElement.getElements(new VariableAccessFilter<>(var)).stream().filter(access -> access instanceof CtVariableWrite).findAny().isPresent() ){
						CtTypeReference<Object> ref = reference.getReference();
						ref.addActualTypeArgument(var.getType().box());
						if ( !var.getDeclaration().getType().equals(ref)){
							caseElement.getParent(CtMethod.class).getElements(new VariableAccessFilter<>(var)).stream().filter(access -> access instanceof CtVariableRead).forEach(accessRead -> {
//								if ( accessRead.getParent(CtAssignment.class) != null){
								
									CtInvocation<Object> methodInvocation = getFactory().Core().createInvocation();
									CtExecutableReference executableRef = getFactory().Core().createExecutableReference().setSimpleName("getRef");
									
									methodInvocation.setExecutable(executableRef);
									methodInvocation.setTarget(getFactory().Code().createCodeSnippetExpression(accessRead.getVariable().getSimpleName()));
									methodInvocation.setParent(accessRead);
									
									
									accessRead.replace(getFactory().Code().createCodeSnippetExpression(methodInvocation.toString()));
									
									
//									accessWrite.getParent(CtAssignment.class).replace((CtStatement)methodInvocation);
//									accessWrite.getParent(CtAssignment.class).replace(getFactory().Code().createCodeSnippetStatement(assignment.getAssigned()+".setRef("+assignment.getAssignment()+")"));
//								}
							});

							caseElement.getParent(CtMethod.class).getElements(new VariableAccessFilter<>(var)).stream().filter(access -> access instanceof CtVariableWrite).forEach(accessWrite -> {
								if ( accessWrite.getParent(CtOperatorAssignment.class) != null){
									CtOperatorAssignment assignment = accessWrite.getParent(CtOperatorAssignment.class);
									
									CtInvocation<Object> methodInvocation = getFactory().Core().createInvocation();
									CtExecutableReference executableRef = getFactory().Core().createExecutableReference().setSimpleName("setRef");
									
									methodInvocation.setExecutable(executableRef);
									methodInvocation.addArgument(getFactory().Code().createCodeSnippetExpression(assignment.getAssigned()+".getRef() "+convertToSymbol(assignment.getKind())+" "+assignment.getAssignment()));
									methodInvocation.setTarget(assignment.getAssigned());
									methodInvocation.setParent(assignment.getParent());
									
									
									assignment.replace((CtStatement)methodInvocation);
									System.out.println(methodInvocation);
								}else if ( accessWrite.getParent(CtAssignment.class) != null){
									CtAssignment assignment = accessWrite.getParent(CtAssignment.class);
									
									CtInvocation<Object> methodInvocation = getFactory().Core().createInvocation();
									CtExecutableReference executableRef = getFactory().Core().createExecutableReference().setSimpleName("setRef");
									
									methodInvocation.setExecutable(executableRef);
									methodInvocation.addArgument(assignment.getAssignment());
									methodInvocation.setTarget(assignment.getAssigned());
									methodInvocation.setParent(assignment.getParent());
									
									
									assignment.replace((CtStatement)methodInvocation);
//									accessWrite.getParent(CtAssignment.class).replace((CtStatement)methodInvocation);
//									accessWrite.getParent(CtAssignment.class).replace(getFactory().Code().createCodeSnippetStatement(assignment.getAssigned()+".setRef("+assignment.getAssignment()+")"));
								}
							});
							
							var.setType(ref);
							var.getDeclaration().setType(ref);
							if (var.getDeclaration().getDefaultExpression() == null) {
								var.getDeclaration().setDefaultExpression(
										getFactory().Code().createCodeSnippetExpression("new " + ref + "(null)"));
							} else {
								var.getDeclaration().setDefaultExpression(getFactory().Code().createCodeSnippetExpression(
										"new " + ref + "(" + var.getDeclaration().getDefaultExpression() + ")"));
							}
							
							
							
						}
						
					}

				});
	}

	private String convertToSymbol(BinaryOperatorKind kind) {
		switch(kind.toString()){
		case "OR":
			return "||";
		case "AND":
			return "&&";
		case "BITOR":
			return "|";
		case "BITXOR":
			return "^";
		case "BITAND":
			return "&";
		case "EQ":
			return "==";
		case "NE":
			return "!=";
		case "LT":
			return "<";
		case "GT":
			return ">";
		case "LE":
			return "<=";
		case "GE":
			return ">=";
		case "SL":
			return "<<";
		case "SR":
			return ">>";
		case "USR":
			return "<";
		case "PLUS":
			return "+";
		case "MINUS":
			return "-";
		case "MUL":
			return "*";
		case "DIV":
			return "/";
		case "MOD":
			return "%";
		case "INSTANCEOF":
			return "instanceof";
		}
		return null;
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
				.collect(Collectors.toCollection(HashSet::new));
	}

	private String createMethodName(CtCase<?> caseElement, CtMethod<?> ctParentMethod) {
		return ctParentMethod.getSimpleName() + "_case_" + Math.abs(caseElement.hashCode());
	}

}
