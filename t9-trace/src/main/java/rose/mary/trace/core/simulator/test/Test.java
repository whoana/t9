/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.simulator.test;

/**
 * <pre>
 * rose.mary.trace.test.ilink
 * Test.java
 * </pre>
 * @author whoana
 * @date Jul 30, 2019
 */
public abstract class Test {
	
	public abstract boolean test(String [] args) throws Throwable;
	public abstract String getSuccessMsg(String [] args);
	public abstract String getFailMsg(String [] args);
	
	public String run(String [] args) throws Throwable{
		return test(args) ? getSuccessMsg(args) : getFailMsg(args);
	}
 
}
