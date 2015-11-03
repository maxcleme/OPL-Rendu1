package iagl.opl.rendu.one;

import spoon.Launcher;

/**
 * Hello world!
 *
 */
public class SonarFixer {
	
	/*
	 * TODO
	 * 
	 * "compareTo" results should not be checked for specific values
	 * [DONE] "@Override" annotation should be used on any method overriding (since Java 5) or implementing (since Java 6) another one
	 * "entrySet()" should be iterated when both the key and value are needed
	 * Case insensitive string comparisons should be made without intermediate upper or lower casing
	 * "switch case" clauses should not have too many lines
	 * Unused private fields should be removed
	 * Classes and enums with private members should have a constructor
	 */
	public static void main(String[] args) throws Exception {
		String[] spoonArgs = { 
				"-i", "/home/m2iagl/clement/Documents/workspace-sts-3.7.0.RELEASE/OPL-Rendu1/rendu.one/src/test/java/iagl/opl/rendu/one/samples", 
				"-p", "iagl.opl.rendu.one.processors.OverrideProcessor",
				"--with-imports",
				"-v",
				"-x"
		};

		Launcher.main(spoonArgs);
	}
}
