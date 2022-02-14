/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.envs.SqlErrorCode;
import rose.mary.trace.apps.envs.SqlErrorCodeMap;
import rose.mary.trace.apps.manager.config.BotErrorHandlerManagerConfig;
import rose.mary.trace.apps.manager.config.BotLoaderManagerConfig;
import rose.mary.trace.apps.manager.config.BoterManagerConfig;
import rose.mary.trace.apps.manager.config.CacheManagerConfig;
import rose.mary.trace.apps.manager.config.ChannelManagerConfig;
import rose.mary.trace.apps.manager.config.Config;
import rose.mary.trace.apps.manager.config.DatabasePolicyConfig;
import rose.mary.trace.apps.manager.config.FinisherManagerConfig;
import rose.mary.trace.apps.manager.config.InterfaceCacheManagerConfig;
import rose.mary.trace.apps.manager.config.LoaderManagerConfig;
import rose.mary.trace.apps.manager.config.ServerManagerConfig;
import rose.mary.trace.apps.manager.config.TesterManagerConfig;
import rose.mary.trace.apps.manager.config.TraceErrorHandlerManagerConfig;
import rose.mary.trace.apps.manager.config.UnmatchHandlerManagerConfig; 

/**
 * <pre>
 * rose.mary.trace.manager
 * ConfigurationManager.java
 * </pre>
 * @author whoana
 * @date Aug 23, 2019
 */
public class ConfigurationManager {
	
	String configHome = "/Users/whoana/git/projectq/src/main/resources/config";
	String configFile = "config.json";
	Config config;
	

	String sqlerrorFile = "sqlerror.json";
  
	SqlErrorCodeMap sqlErrorCodeMap;
	
	public void prepare() throws Exception {
		//configHome = System.getProperty("rose.mary.config.home");
		configHome = System.getProperty("rose.mary.home") + File.separator + "config";
		if(configHome == null) {
			//configHome = "/Users/whoana/git/projectq/src/main/resources/config";
			throw new Exception("-Drose.mary.config.home={설정홈} 값을 읽을 수 없습니다.(errorcd:ROSE-0001)")  ;
		} 
		config = (Config)readObjectFromJson(new File(configHome, configFile), Config.class, null);
		
		sqlErrorCodeMap = (SqlErrorCodeMap)readObjectFromJson(new File(configHome, sqlerrorFile), SqlErrorCodeMap.class, null);
		
		
		
	}

	public Config getConfig() {
		return config;
	}
	
	public void setConfig(Config config) throws Exception {
		this.config = config;
		save();
	}
	
	/**
	 * @return the channelManagerConfig
	 */
	public ChannelManagerConfig getChannelManagerConfig() {
		return config.getChannelManagerConfig();
	}

	/**
	 * @param channelManagerConfig the channelManagerConfig to set
	 * @throws Exception 
	 */
	public void setChannelManagerConfig(ChannelManagerConfig channelManagerConfig) throws Exception {
		config.setChannelManagerConfig(channelManagerConfig);
		save();
	}
	
	/**
	 * @return the cacheManagerConfig
	 */
	public CacheManagerConfig getCacheManagerConfig() {
		return config.getCacheManagerConfig();
	}

	/**
	 * @param cacheManagerConfig the cacheManagerConfig to set
	 * @throws Exception 
	 */
	public void setCacheManagerConfig(CacheManagerConfig cacheManagerConfig) throws Exception {
		config.setCacheManagerConfig(cacheManagerConfig);
		save();
	}
 
	
	
	
	/**
	 * @return the serverManagerConfig
	 */
	public ServerManagerConfig getServerManagerConfig() {
		return config.getServerManagerConfig();
	}


	/**
	 * @param serverManagerConfig the serverManagerConfig to set
	 * @throws Exception 
	 */
	public void setServerManagerConfig(ServerManagerConfig serverManagerConfig) throws Exception {
		config.setServerManagerConfig(serverManagerConfig);
		save();
	}

	
	

	/**
	 * @return the loaderManagerConfig
	 */
	public LoaderManagerConfig getLoaderManagerConfig() {
		return config.getLoaderManagerConfig();
	}


	/**
	 * @param loaderManagerConfig the loaderManagerConfig to set
	 * @throws Exception 
	 */
	public void setLoaderManagerConfig(LoaderManagerConfig loaderManagerConfig) throws Exception {
		config.setLoaderManagerConfig(loaderManagerConfig);
		save();
	}

	
	

	/**
	 * @return the interfaceCacheManagerConfig
	 */
	public InterfaceCacheManagerConfig getInterfaceCacheManagerConfig() {
		return config.getInterfaceCacheManagerConfig();
	}


	/**
	 * @param interfaceCacheManagerConfig the interfaceCacheManagerConfig to set
	 * @throws Exception 
	 */
	public void setInterfaceCacheManagerConfig(InterfaceCacheManagerConfig interfaceCacheManagerConfig) throws Exception {
		config.setInterfaceCacheManagerConfig(interfaceCacheManagerConfig);
		save();
	}
	
	


	/**
	 * @return the boterManagerConfig
	 */
	public BoterManagerConfig getBoterManagerConfig() {
		return config.getBoterManagerConfig();
	}


	/**
	 * @param boterManagerConfig the boterManagerConfig to set
	 * @throws Exception 
	 */
	public void setBoterManagerConfig(BoterManagerConfig boterManagerConfig) throws Exception {
		config.setBoterManagerConfig(boterManagerConfig);
		save();
	}

	
	

	/**
	 * @return the botLoaderManagerConfig
	 */
	public BotLoaderManagerConfig getBotLoaderManagerConfig() {
		return config.getBotLoaderManagerConfig();
	}


	/**
	 * @param botLoaderManagerConfig the botLoaderManagerConfig to set
	 * @throws Exception 
	 */
	public void setBotLoaderManagerConfig(BotLoaderManagerConfig botLoaderManagerConfig) throws Exception {
		config.setBotLoaderManagerConfig(botLoaderManagerConfig);
		save();
	}	

	/**
	 * @return the traceErrorHandlerManagerConfig
	 */
	public TraceErrorHandlerManagerConfig getTraceErrorHandlerManagerConfig() {
		return config.getTraceErrorHandlerManagerConfig();
	}


	/**
	 * @param traceErrorHandlerManagerConfig the traceErrorHandlerManagerConfig to set
	 * @throws Exception 
	 */
	public void setTraceErrorHandlerManagerConfig(TraceErrorHandlerManagerConfig traceErrorHandlerManagerConfig) throws Exception {
		config.setTraceErrorHandlerManagerConfig(traceErrorHandlerManagerConfig);
		save();
	}

	
	/**
	 * @return the botErrorHandlerManagerConfig
	 */
	public BotErrorHandlerManagerConfig getBotErrorHandlerManagerConfig() {
		return config.getBotErrorHandlerManagerConfig();
	}


	/**
	 * @param botErrorHandlerManagerConfig the botErrorHandlerManagerConfig to set
	 * @throws Exception 
	 */
	public void setBotErrorHandlerManagerConfig(BotErrorHandlerManagerConfig botErrorHandlerManagerConfig) throws Exception {
		config.setBotErrorHandlerManagerConfig(botErrorHandlerManagerConfig);
		save();
	}
	
	
	/**
	 * @return the unmatchHandlerConfig
	 */
	public UnmatchHandlerManagerConfig getUnmatchHandlerManagerConfig() {
		return config.getUnmatchHandlerManagerConfig();
	}

	/**
	 * @param unmatchHandlerManagerConfig the unmatchHandlerConfig to set
	 * @throws Exception 
	 */
	public void setUnmatchHandlerManagerConfig(UnmatchHandlerManagerConfig unmatchHandlerManagerConfig) throws Exception {
		config.setUnmatchHandlerManagerConfig(unmatchHandlerManagerConfig);
		save();
	}

	/**
	 * @return the databasePolicyConfig
	 */
	public DatabasePolicyConfig getDatabasePolicyConfig() {
		return config.getDatabasePolicyConfig();
	}

	/**
	 * @param databasePolicyConfig the databasePolicyConfig to set
	 * @throws Exception 
	 */
	public void setDatabasePolicyConfig(DatabasePolicyConfig databasePolicyConfig) throws Exception {
		config.setDatabasePolicyConfig(databasePolicyConfig);
		save();
	}

	/**
	 * @return the finisherManagerConfig
	 */
	public FinisherManagerConfig getFinisherManagerConfig() {
		return config.getFinisherManagerConfig();
	}


	/**
	 * @param finisherManagerConfig the finisherManagerConfig to set
	 * @throws Exception 
	 */
	public void setFinisherManagerConfig(FinisherManagerConfig finisherManagerConfig) throws Exception {
		config.setFinisherManagerConfig(finisherManagerConfig);
		save();
	}
	
	
	public TesterManagerConfig getTesterManagerConfig() {
		return config.getTesterManagerConfig();
	}
	
	public void setTesterManagerConfig(TesterManagerConfig testerManagerConfig) throws Exception {
		config.setTesterManagerConfig(testerManagerConfig);
		save();
	}
	
	
	Object readObjectFromJson(File dest, Class clazz, String ccsid) throws Exception{
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(dest);
			byte b[] = new byte[(int)dest.length()];
			fis.read(b);
			return jsonMapper.readValue(b, clazz);
		}finally{
			try{if(fis != null) fis.close();}catch(IOException e){}
		}
	}
	
	private void save() throws Exception{
		try{
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
			jsonMapper.writeValue(new File(configHome, configFile), config);
		}finally{
		}
	}
	
	 
	
	public SqlErrorCodeMap getSqlErrorCodeMap() {
		return sqlErrorCodeMap;
	}

	public void setSqlErrorCodeMap(SqlErrorCodeMap sqlErrorCodeMap) {
		this.sqlErrorCodeMap = sqlErrorCodeMap;
	}

	public static void main(String[] args) {
		System.setProperty("rose.mary.home","/Users/whoana/Documents/gitlab/t9/t9-trace/src/main/resources");
		ConfigurationManager cm = new ConfigurationManager();
		try {
			cm.prepare();
			
//			ChannelManagerConfig channelManagerConfig = cm.getChannelManagerConfig();
//			System.out.println(Util.toJSONString(channelManagerConfig));
//			
//			CacheManagerConfig cacheManagerConfig = cm.getCacheManagerConfig();
//			System.out.println(Util.toJSONString(cacheManagerConfig));
//			
//			
//			ServerManagerConfig serverManagerConfig = cm.getServerManagerConfig();
//			System.out.println(Util.toJSONString(serverManagerConfig));
//			
			System.out.println("sqlErrorCodeMap:" + cm.getSqlErrorCodeMap());
			SqlErrorCode sqlErrorCodes = cm.getSqlErrorCodeMap().getMap().get("Oracle");
			System.out.println("oracle:"+ Util.toJSONPrettyString(sqlErrorCodes));
			String [] badSqlGrammarCodes = sqlErrorCodes.getBadSqlGrammarCodes();
			
			//Arrays.sort(badSqlGrammarCodes);
			int res = Arrays.binarySearch(badSqlGrammarCodes, "900");
			System.out.println("res:"+ res);
			res = Arrays.binarySearch(badSqlGrammarCodes, "903");
			System.out.println("res:"+ res);
			res = Arrays.binarySearch(badSqlGrammarCodes, "904");
			System.out.println("res:"+ res);
			res = Arrays.binarySearch(badSqlGrammarCodes, "917");
			System.out.println("res:"+ res);
			res = Arrays.binarySearch(badSqlGrammarCodes, "936");
			System.out.println("res:"+ res);
			res = Arrays.binarySearch(badSqlGrammarCodes, "942");
			System.out.println("res:"+ res);
			res = Arrays.binarySearch(badSqlGrammarCodes, "655000");
			System.out.println("res:"+ res);
			res = Arrays.binarySearch(badSqlGrammarCodes, "1700600000");
			System.out.println("res:"+ res);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
