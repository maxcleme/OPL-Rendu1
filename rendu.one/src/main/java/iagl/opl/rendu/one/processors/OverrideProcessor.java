package iagl.opl.rendu.one.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtExecutableReference;

public class OverrideProcessor extends AbstractProcessor<CtMethod<?>> {

	@Override
	public boolean isToBeProcessed(CtMethod<?> candidate) {
		if (candidate.hasModifier(ModifierKind.STATIC))
			return false;
		if (candidate.getParent(CtClass.class) == null)
			return false;
		if (candidate.getAnnotation(Override.class) != null)
			return false;
		
		if (getFactory().Class().OBJECT.getDeclaredExecutables().stream().filter(exec -> isOverriding(candidate, exec))
				.findAny().isPresent()) {
			return true;
		}

		if (candidate.getParent(CtNewClass.class) != null) {
			try {
				return candidate.getParent(CtNewClass.class).getType().getDeclaredExecutables().stream()
						.filter(exec -> isOverriding(candidate, exec)).findAny().isPresent();
			} catch (NullPointerException ex) {
				return false;
			}
		}

		if (candidate.getParent(CtClass.class).getSuperclass() != null) {
			return candidate.getParent(CtClass.class).getSuperclass().getDeclaredExecutables().stream()
					.filter(executable -> {
						return isOverriding(candidate, executable);
					}).findAny().isPresent();
		}
		if (candidate.getParent(CtClass.class).getSuperInterfaces() != null) {
			try {
				return candidate.getParent(CtClass.class).getSuperInterfaces().stream()
						.filter(inter -> inter.getDeclaredExecutables().stream().filter(executable -> {
							return isOverriding(candidate, executable);
						}).findAny().isPresent()).findAny().isPresent();
			} catch (NullPointerException ex) {
				return false;
			}
		}
		return false;

	}

	private boolean isOverriding(CtMethod<?> candidate, CtExecutableReference<?> exec) {
		if (!candidate.getSimpleName().equals(exec.getSimpleName()))
			return false;
		if (candidate.getParameters().size() != exec.getParameters().size())
			return false;

		for (int i = 0; i < candidate.getParameters().size(); i++) {
			if (candidate.getParent(CtNewClass.class) != null) {
				return exec.getParameters().get(i).isAssignableFrom(candidate.getParameters().get(i).getType());
			} else if (!exec.getParameters().get(i).equals(candidate.getParameters().get(i).getType())) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void process(CtMethod<?> element) {
		System.out.println(element);
		getFactory().Annotation().annotate(element, Override.class);
	}

}
