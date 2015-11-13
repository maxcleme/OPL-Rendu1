package iagl.opl.rendu.one.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.FieldAccessFilter;

public class TestProcessor extends AbstractProcessor<CtField<?>> {

	@Override
	public boolean isToBeProcessed(CtField<?> candidate) {
		return candidate.getModifiers().contains(ModifierKind.PRIVATE)
				&& (candidate.getAnnotation(SuppressWarnings.class) != null
						&& !candidate.getAnnotation(SuppressWarnings.class).value()[0].equals("unused"))
				&& !candidate.getSimpleName().contains("serialVersionUID");
	}

	@Override
	public void process(CtField<?> ctField) {
		CtFieldReference<?> ctFieldReference = ctField.getReference();

		CtPackage rootPackage = getFactory().Package().getRootPackage();

		// System.out.println(rootPackage.getElements(new
		// SameFieldAccessFilter(ctFieldReference)));

		boolean legitAccess = rootPackage.getElements(new SameFieldAccessFilter(ctFieldReference)).stream()
				.filter(ref -> {
					return ref.getParent(CtReturn.class) != null || ref.getParent(CtIf.class) != null
							|| ref.getParent(CtInvocation.class) != null;
				}).findAny().isPresent();

		if (!legitAccess) {
			System.out.println("Need to remove :" + ctField);
			rootPackage.getElements(new SameFieldAccessFilter(ctFieldReference)).forEach(ref -> {
				ref.getParent(CtStatement.class).replace(getFactory().Code()
						.createCodeSnippetStatement("// Remove unused private field " + ctField.getSimpleName()));
				;
			});

			ctField.getParent(CtClass.class).removeField(ctField);
		}

	}

	private class SameFieldAccessFilter extends FieldAccessFilter {

		private CtFieldReference<?> field;

		public SameFieldAccessFilter(CtFieldReference<?> field) {
			super(field);
			this.field = field;
		}

		@Override
		public boolean matches(CtFieldAccess<?> variableAccess) {
			return field.getQualifiedName().equals(variableAccess.getVariable().getQualifiedName());
		}

	}

}
