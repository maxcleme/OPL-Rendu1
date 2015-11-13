package iagl.opl.rendu.one.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.FieldAccessFilter;

public class TestProcessor extends AbstractProcessor<CtField<?>> {

	@Override
	public boolean isToBeProcessed(CtField<?> candidate) {
		return candidate.getModifiers().contains(ModifierKind.PRIVATE);
	}

	@Override
	public void process(CtField<?> ctField) {
		System.out.println(ctField.getReference().getQualifiedName() + " -> ");

		CtFieldReference<?> ctFieldReference = ctField.getReference();

		CtPackage rootPackage = getFactory().Package().getRootPackage();

		System.out.println(rootPackage.getElements(new YoloFilter(ctFieldReference)));
	}

	private class YoloFilter extends FieldAccessFilter {

		private CtFieldReference<?> field;

		public YoloFilter(CtFieldReference<?> field) {
			super(field);
			this.field = field;
		}

		@Override
		public boolean matches(CtFieldAccess<?> variableAccess) {
			System.out.println("1:" + field.getQualifiedName());
			System.out.println("2:" + variableAccess.getVariable().getQualifiedName());
			return field.getQualifiedName().equals(variableAccess.getVariable().getQualifiedName());
		}

	}

}
