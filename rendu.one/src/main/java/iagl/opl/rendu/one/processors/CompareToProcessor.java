package iagl.opl.rendu.one.processors;


import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;

public class CompareToProcessor extends AbstractProcessor<CtInvocation<?>> {
	
	@Override
	public boolean isToBeProcessed(CtInvocation<?> candidate) {
		if ( !candidate.getExecutable().getSimpleName().equalsIgnoreCase("compareTo") ){
			return false;
		}
		
		
		
		return true;
	}
	
	@Override
	public void process(CtInvocation<?> candidate) {
		System.out.println(candidate.getParent(CtExpression.class));
	}
	
//	public class CompareToProcessorTest {
//
//		@Test
//		public void test() {
//
//		}
//		void compareFoo(Integer a, Integer b){
//			if(a.compareTo(b) == -1){
//				assertTrue(1==1);
//			}
//			assertTrue(1==2);
//		}
//	}


}
