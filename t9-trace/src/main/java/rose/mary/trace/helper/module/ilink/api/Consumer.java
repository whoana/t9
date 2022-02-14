/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.ilink.api;
 

/**
 * <pre>
 * rose.mary.trace.ilink.api
 * Consumer.java
 * </pre>
 * @author whoana
 * @date Aug 21, 2019
 */
public interface Consumer<T> {
	public void consume(T msg) throws Exception;
}
