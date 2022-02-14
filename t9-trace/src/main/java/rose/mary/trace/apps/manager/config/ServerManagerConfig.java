/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager.config;

/**
 * <pre>
 * rose.mary.trace.manager
 * ServerManagerConfig.java
 * </pre>
 * @author whoana
 * @date Aug 26, 2019
 */
public class ServerManagerConfig {

	String name;
	
	String version;
	
	String site;

	boolean startOnBoot = true;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	public boolean isStartOnBoot() {
		return startOnBoot;
	}

	public void setStartOnBoot(boolean startOnBoot) {
		this.startOnBoot = startOnBoot;
	}
	
	
}
