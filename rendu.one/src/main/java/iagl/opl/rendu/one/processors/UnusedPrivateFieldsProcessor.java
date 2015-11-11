package iagl.opl.rendu.one.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.reference.CtFieldReference;

public class UnusedPrivateFieldsProcessor extends AbstractProcessor<CtFieldAccess<?>> {

  // Map de <nomDeLaClasse, ListeDeVariablePrivateAccedees>
  Map<String, List<CtFieldReference>> map;

  public void init() {
    map = new HashMap<String, List<CtFieldReference>>();
  }

  @Override
  public boolean isToBeProcessed(CtFieldAccess<?> candidate) {
    if (null != candidate.getVariable().getDeclaration().getVisibility())
      return (candidate.getVariable().getDeclaration().getVisibility().name().equals("PRIVATE"));
    return false;
  };

  @Override
  public void process(CtFieldAccess<?> candidate) {
    // init de la map
    if (null == map.get(candidate.getVariable().getDeclaration().getParent().getSignature())) {
      List<CtFieldReference> tmp = new ArrayList<>();
      map.put(candidate.getVariable().getDeclaration().getParent().getSignature(), tmp);
    }

    // ajout dans la map < classe , variableAccedee >
    List<CtFieldReference> tmp = map.get(candidate.getVariable().getDeclaration().getParent().getSignature());
    if (!tmp.contains(candidate.getVariable())) {
      tmp.add(candidate.getVariable());
    }
    map.put(candidate.getVariable().getDeclaration().getParent().getSignature(), tmp);
  }

  public void processingDone() {
    System.out.println(map);

  }

}
