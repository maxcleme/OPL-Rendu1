package iagl.opl.rendu.one.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;

public class CompareToProcessor extends AbstractProcessor<CtInvocation<?>> {

  @Override
  public boolean isToBeProcessed(CtInvocation<?> candidate) {
    if (!candidate.getExecutable().getSimpleName().equalsIgnoreCase("compareTo")) {
      return false;
    }

    return true;
  }

  @Override
  public void process(CtInvocation<?> candidate) {
    System.out.println("Affichage ==> " + candidate.getParent(CtExpression.class));
  }

}
