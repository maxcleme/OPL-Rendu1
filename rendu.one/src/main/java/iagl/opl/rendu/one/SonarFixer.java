package iagl.opl.rendu.one;

import spoon.Launcher;

/**
 * Hello world!
 *
 */
public class SonarFixer {
	
	/*
	 * 
	 * [FIND/DONE] "@Override" annotation should be used on any method overriding (since Java 5) or implementing (since Java 6) another one

	 * TODO
	 * [FIND] "switch case" clauses should not have too many lines
	 * [FIND] Unused private fields should be removed
	 * [FIND] Strings literals should be placed on the left side when checking for equality
	 * [FIND] Exception types should not be tested using "instanceof" in catch blocks
	 */
	public static void main(String[] args) throws Exception {
		String[] spoonArgs = { 
				"-i", "/home/m2iagl/clement/Documents/workspace-sts-3.7.0.RELEASE/OPL-Rendu1/rendu.one/src/test/java/iagl/opl/rendu/one/samples", 
				"-p", "iagl.opl.rendu.one.processors.SwitchTooManyLinesProcessor",
				"-v",
				"-x"
		};

		Launcher.main(spoonArgs);
	}
}
