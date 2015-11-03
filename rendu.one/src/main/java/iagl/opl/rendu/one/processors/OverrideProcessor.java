package iagl.opl.rendu.one.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

public class OverrideProcessor extends AbstractProcessor<CtMethod<?>> {

	@Override
	public boolean isToBeProcessed(CtMethod<?> candidate) {
		CtTypeReference<?> superClass = candidate.getParent(CtClass.class).getSuperclass();
		
		if ( superClass == null ) return false;
		
		if ( candidate.getAnnotation(Override.class) != null ) return false;
		
		return superClass.getDeclaredExecutables().stream().filter(executable  -> {
			return executable.getSimpleName().equalsIgnoreCase(candidate.getSimpleName());
		}).findAny().isPresent();
		
	}
	
	
	@Override
	public void process(CtMethod<?> element) {
		getFactory().Annotation().annotate(element, Override.class);
	}

	



}
