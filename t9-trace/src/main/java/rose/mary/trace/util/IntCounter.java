/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.util;

 
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
/**
 * <pre>
 * IntCounter
 * maxValue 값까지 카운팅 후 initValue 값으로 리셋되는 아나로그 카운터 기능     
 * </pre> 
 * @author whoana
 * @since 2020.03.20
 */
public class IntCounter {
 
	private AtomicInteger counter;
	
	private IntUnaryOperator operator;
	
	/**
	 * 
	 * @param initValue
	 * @param maxValue
	 * @param increaseValue
	 */
	public IntCounter(int initValue, int maxValue, int increaseValue) {
		this.counter = new AtomicInteger(initValue);
		operator = new IntUnaryOperator() {			
			@Override
			public int applyAsInt(int operand) {
				if(operand >= maxValue) return initValue;
				else return operand + increaseValue > maxValue ? initValue : operand + increaseValue ;
			}
		};
	}
	
	/**
	 * 현재 값을 리턴한다. 명심해라! 카운터를 증가시키지는 낳는다.
	 * @return
	 */
	public int current() {
		return counter.get();
	}
	
	/**
	 * 현재 값을 리턴한다. 카운터는 증가된다.
	 * @return
	 */
	public int getAndIncrease() {
		return counter.getAndUpdate(operator);			
	}
}
