package iagl.opl.rendu.one.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.reference.CtVariableReference;

public class UnusedPrivateFieldsProcessor extends AbstractProcessor<CtVariableWrite<?>> {

  // Map de <nomDeLaClasse, ListeDeVariablePrivateAccedees>
  Map<String, List<CtVariableReference>> map;

  public void init() {
    map = new HashMap<String, List<CtVariableReference>>();
  }

  @Override
  public boolean isToBeProcessed(CtVariableWrite<?> candidate) {

    // System.out.println(candidate.getVariable().getDeclaration().getParent().getSignature());
    if (null != candidate.getVariable().getDeclaration() && null != candidate.getVariable().getDeclaration().getVisibility()) {
      // System.out.println(candidate.getVariable().getDeclaration());
      // System.out.println("visu : " + candidate.getVariable().getDeclaration().getVisibility());
      return (candidate.getVariable().getDeclaration().getVisibility().name().equals("PRIVATE"));
    }
    return false;
  };

  @Override
  public void process(CtVariableWrite<?> candidate) {
    // init de la map
    if (null == map.get(candidate.getVariable().getDeclaration().getParent().getSignature())) {
      List<CtVariableReference> tmp = new ArrayList<>();
      map.put(candidate.getVariable().getDeclaration().getParent().getSignature(), tmp);
    }

    // ajout dans la map < classe , variableAccedee >
    List<CtVariableReference> tmp = map.get(candidate.getVariable().getDeclaration().getParent().getSignature());
    if (!tmp.contains(candidate.getVariable())) {
      tmp.add(candidate.getVariable());
    }
    map.put(candidate.getVariable().getDeclaration().getParent().getSignature(), tmp);
  }

  public void processingDone() {
    System.out.println(map);

  }

}
