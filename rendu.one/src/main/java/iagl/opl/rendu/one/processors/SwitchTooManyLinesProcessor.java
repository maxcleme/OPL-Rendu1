package iagl.opl.rendu.one.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCFlowBreak;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtThrow;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.ReturnOrThrowFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class SwitchTooManyLinesProcessor extends AbstractProcessor<CtSwitch<?>>{

	
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
		for ( int i = 0 ; i < arrayCases.length-1 ; i++){
			CtCase<?> upper = arrayCases[i];
			CtCase<?> caseElement = arrayCases[i+1];
			startUpper = upper.getPosition().getLine();
			startCurrent = caseElement.getPosition().getLine();
			
			if ( startCurrent - startUpper > THRESHOLD_SWITCH_MAXIMUM_LINES ){
				casesToBeProcessed.add(upper);
			}
			upper = caseElement;
		}
		
		// Case N
		if ( endSwitch - arrayCases[arrayCases.length-1].getPosition().getLine() > THRESHOLD_SWITCH_MAXIMUM_LINES ){
			casesToBeProcessed.add(arrayCases[arrayCases.length-1]);
		}
		
		refactorCases(casesToBeProcessed);
		
//		System.out.println(element.getParent(CtClass.class));
	}

	private void refactorCases(List<CtCase<?>> casesToBeProcessed) {


		for ( CtCase<?> caseElement : casesToBeProcessed ){
			CtClass<?> ctClass = caseElement.getParent(CtClass.class);
			CtMethod<?> ctParentMethod = caseElement.getParent(CtMethod.class);
			CtMethod<?> ctMethod = getFactory().Core().createMethod();
			
			List<CtCFlowBreak> flowBreaks = caseElement.getElements(new ReturnOrThrowFilter());
			
			CtReturn<?> ctReturn = (CtReturn<?>) flowBreaks.stream().filter( elem -> elem instanceof CtReturn ).findAny().orElse(null);
			CtTypeReference ctReturnType = ctReturn != null ? ctReturn.getReturnedExpression().getType() : getFactory().Core().createTypeReference().setSimpleName("void");
			
			Set<CtThrow> ctThrows = flowBreaks.stream().filter( elem -> elem instanceof CtThrow ).map(elem -> (CtThrow) elem).collect(Collectors.toSet());
			Set<CtTypeReference<? extends Throwable>> ctThrowTypes = 
					ctThrows.stream().map( elem -> elem.getThrownExpression().getType() ).collect(Collectors.toSet()); 
			
			ctMethod.setSimpleName(createMethodName(caseElement, ctParentMethod));
			ctMethod.setModifiers(Sets.newHashSet(ModifierKind.PRIVATE));
			ctMethod.setType(ctReturnType);
			ctMethod.setThrownTypes(ctThrowTypes);
			ctMethod.setBody(getFactory().Core().createBlock().setStatements(caseElement.getStatements()));
			
			ctClass.addMethod(ctMethod);
			// remplacer les statements des cases par les appels de methods
			
			List<CtVariableAccess<?>> variables = caseElement.getElements(new TypeFilter<CtVariableAccess<?>>(CtVariableAccess.class));
			
			System.out.println(sortVariables(variables));
			// Todo ajouter les parametres de methodes en function des variables necessaires
			System.out.println(ctMethod);
			
		}
		
	}

	private Set<CtVariableReference<?>> sortVariables(List<CtVariableAccess<?>> variables) {
		// TODO virer les variables déclaré à l'interieur du case ( ex : le int i du for )
		
		return variables.stream().map(var -> var.getVariable()).filter(var -> !var.getModifiers().contains(ModifierKind.STATIC)).collect(Collectors.toSet());
	}

	private String createMethodName(CtCase<?> caseElement, CtMethod<?> ctParentMethod) {
		return ctParentMethod.getSimpleName()+"_case_"+caseElement.hashCode();
	}
	
	
}
