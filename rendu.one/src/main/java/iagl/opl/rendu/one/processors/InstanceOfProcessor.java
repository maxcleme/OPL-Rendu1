package iagl.opl.rendu.one.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtTry;

public class InstanceOfProcessor extends AbstractProcessor<CtBinaryOperator<?>> {

	@Override
	public boolean isToBeProcessed(CtBinaryOperator<?> candidate) {
		return candidate.getKind().equals(BinaryOperatorKind.INSTANCEOF)
				&& candidate.getParent(CtCatch.class) != null
				&& candidate.getReferencedTypes().stream().map( refType -> getFactory().Class().createReference("java.lang.Exception").isAssignableFrom(refType)).count() >= 2 
				&& candidate.getParent(CtCatch.class).getParameter().getSimpleName().equals(candidate.getLeftHandOperand().toString());
	}
	@Override
	public void process(CtBinaryOperator<?> operator) {
		CtCatch ctCatchOriginal = operator.getParent(CtCatch.class);
		ctCatchOriginal.getBody().removeStatement(operator.getParent(CtStatement.class));
		
		CtTry ctTry = operator.getParent(CtTry.class);
		CtCatch ctCatch = getFactory().Core().createCatch();
		CtBlock block = getFactory().Core().createBlock();
		ctCatch.setParameter(getFactory().Core().createCatchVariable().setSimpleName(operator.getRightHandOperand().toString()+" "+operator.getLeftHandOperand().toString()));
		
		
		
		operator.replace(getFactory().Code().createCodeSnippetExpression("true"));
		block.addStatement(operator.getParent(CtStatement.class));
		ctCatch.setBody(block);
		ctTry.getCatchers().add(0, ctCatch);
		System.out.println(ctTry);
	}

}
