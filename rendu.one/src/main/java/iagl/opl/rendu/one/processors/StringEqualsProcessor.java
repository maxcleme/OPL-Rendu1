package iagl.opl.rendu.one.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;

public class StringEqualsProcessor extends AbstractProcessor<CtInvocation<?>>{

	@Override
	public boolean isToBeProcessed(CtInvocation<?> candidate) {
		return ("equals".equals(candidate.getExecutable().getSimpleName()) || "equalsIgnoreCase".equals(candidate.getExecutable().getSimpleName()) )
				&& candidate.getTarget() != null
				&& candidate.getExecutable().getParameters().size() == 1 
				&& candidate.getArguments().get(0).getSignature().matches("\"(.*?)\"")
				&& !(""+candidate.getTarget()).matches("\"(.*?)\"");
	}
	
	@Override
	public void process(CtInvocation<?> invocation) {
		System.out.println(invocation);
		invocation.replace(getFactory().Code().createCodeSnippetExpression(invocation.getArguments().get(0).getSignature()+".equals("+invocation.getTarget()+")"));
	}

}
