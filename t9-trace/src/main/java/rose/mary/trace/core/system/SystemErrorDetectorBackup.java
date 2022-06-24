  package rose.mary.trace.core.system;

import java.sql.SQLException;

import org.apache.ibatis.executor.BatchExecutorException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import rose.mary.trace.core.data.policy.DatabasePolicy;
import rose.mary.trace.handler.DatabasePolicyHandler;
import rose.mary.trace.manager.ChannelManager;
import rose.mary.trace.manager.ConfigurationManager;
import rose.mary.trace.manager.ServerManager;

/**
 * <pre>
 * 데이터베이스 Error 발생시 DatabasePolicy 에 따른 처리 절차를 구현한다.
 * DatabaseErrorHandler.java
 * 
 * </pre>
 * @author whoana
 * @since 20200515
 */
@Aspect
public class SystemErrorDetectorBackup {
	  
	DatabasePolicy policy = DatabasePolicy.SHUTDOWN;
	 
	MessageSource messageResource;
 
	@Autowired 
	ConfigurationManager configurationManager;
	
	@Autowired 
	ServerManager serverManager;
	
	@Autowired 
	ChannelManager channelManager;
	
	@Autowired 
	MessageSource messageSource;
  
	private int policyCount = 1;
	
	int continuousCheckCount1 = 0 ;
	int continuousCheckCount2 = 0 ;
	int continuousCheckCount3 = 0 ;

	
	
	public void prepare() {
		policyCount = configurationManager.getDatabasePolicyConfig().getPolicyCount();
		policy = new DatabasePolicy(configurationManager.getDatabasePolicyConfig().getPolicy());
	}
	
	
	@AfterThrowing(value = "execution(public * rose.mary.trace.database.service.BotService.*())", throwing = "exception")
	public void handleBotException(JoinPoint jp, Throwable exception) {
		 
		
		if(exception instanceof org.apache.ibatis.exceptions.PersistenceException) {
			 
			continuousCheckCount2 ++;

			SystemLogger.info("DatabaseErrorHandler 예외처리:" + jp.toLongString());
			SQLException e = (SQLException)exception.getCause();
			if(e != null) {
				SystemLogger.info("sqlState:" + e.getSQLState());
				SystemLogger.info("errorCode:" + e.getErrorCode());
				SystemLogger.info("message:" + e.getMessage(), e);
				
			}
			SystemLogger.info("continuousCheckCount2:" + continuousCheckCount2);
			SystemLogger.info("policyCount:" + policyCount); 
			if(continuousCheckCount2 >= policyCount) {
				try {
					SystemLogger.info("데이터베이스 예외가 발생하여 정책을 실행합니다.", exception);
					switch(policy.getPolicy()) {
						case DatabasePolicy.stopChannel :
							SystemLogger.info("DatabasePolicy.stopChannel 정책을 실행합니다.");
							serverManager.stopChannel();					
							continuousCheckCount2 = 0;
							SystemLogger.info("DatabasePolicy.stopChannel 정책을 실행을 처리하였습니다.");
							break;
						case DatabasePolicy.stopServer :
							SystemLogger.info("DatabasePolicy.stopServer 정책을 실행합니다.");
							serverManager.stopServer();
							continuousCheckCount2 = 0;
							SystemLogger.info("DatabasePolicy.stopServer 정책을 실행을 처리하였습니다.");
							break;
						case DatabasePolicy.shutdownServer :
							SystemLogger.info("DatabasePolicy.shutdownServer 정책을 실행합니다.");
							serverManager.shutdownServer();
							continuousCheckCount2 = 0;
							SystemLogger.info("DatabasePolicy.shutdownServer 정책을 실행을 처리하였습니다.");
							break;
						default :
							SystemLogger.info("DatabasePolicy정보가 존재하지 않습니다.(세팅값:" + policy.getPolicy() + ")");
							continuousCheckCount2 = 0;
							break;
					}			
					
				} catch (Exception ex) { 
					SystemLogger.info("DatabasePolicy.stopChannel 정책 실행 도중 예외가 발생되었습니다.",ex);
				}
			}
			
			
		}
	}
  
	
	boolean alreadyRunPolicy = false;
	@AfterThrowing(value = "execution(public * rose.mary.trace.database.service.TraceService.*(..))", throwing = "exception")
	public void handleTraceException(JoinPoint jp, Throwable exception) {
		//SystemLogger.info("DatabaseErrorHandler.handleTraceException:" + jp.toLongString());
		
		if(exception instanceof org.apache.ibatis.exceptions.PersistenceException) {
			 
			continuousCheckCount1 ++;

			SystemLogger.info("DatabaseErrorHandler 예외처리:" + jp.toLongString());
			 
			if(exception.getCause() instanceof SQLException && exception.getCause() != null) {
				SQLException e = (SQLException)exception.getCause();
				SystemLogger.info("sqlState:" + e.getSQLState());
				SystemLogger.info("errorCode:" + e.getErrorCode());
				SystemLogger.info("message:" + e.getMessage(), e);
			}else if(exception.getCause() instanceof BatchExecutorException && exception.getCause() != null) {
				BatchExecutorException e = (BatchExecutorException)exception.getCause();
				SystemLogger.info("message:" + e.getMessage(), e);				
			}
			
			SystemLogger.info("continuousCheckCount1:" + continuousCheckCount1);
			SystemLogger.info("policyCount:" + policyCount);
			
			if(continuousCheckCount1 >= policyCount) {
				DatabasePolicyHandler.setDatabaseError(true);
				SystemLogger.info("DatabasePolicy 실행 세팅 완료");
			}
			
//			if(continuousCheckCount1 >= policyCount) {
//				try {
//					if(alreadyRunPolicy) {
//						return;
//					}else{ 
//						alreadyRunPolicy = true;
//					
//						SystemLogger.info("데이터베이스 예외가 발생하여 정책을 실행합니다.", exception);
//						switch(policy.getPolicy()) {
//							case DatabasePolicy.stopChannel :
//								SystemLogger.info("DatabasePolicy.stopChannel 정책을 실행합니다.");
//								serverManager.stopChannel();					
//								continuousCheckCount1 = 0;
//								SystemLogger.info("DatabasePolicy.stopChannel 정책을 실행을 처리하였습니다.");
//								break;
//							case DatabasePolicy.stopServer :
//								SystemLogger.info("DatabasePolicy.stopServer 정책을 실행합니다.");
//								serverManager.stopServer();
//								continuousCheckCount1 = 0;
//								SystemLogger.info("DatabasePolicy.stopServer 정책을 실행을 처리하였습니다.");
//								break;
//							case DatabasePolicy.shutdownServer :
//								SystemLogger.info("DatabasePolicy.shutdownServer 정책을 실행합니다.");
//								serverManager.shutdownServer();
//								continuousCheckCount1 = 0;
//								SystemLogger.info("DatabasePolicy.shutdownServer 정책을 실행을 처리하였습니다.");
//								break;
//							default :
//								SystemLogger.info("DatabasePolicy정보가 존재하지 않습니다.(세팅값:" + policy.getPolicy() + ")");
//								continuousCheckCount1 = 0;
//								break;
//						}			
//					}
//				} catch (Exception ex) { 
//					SystemLogger.info("DatabasePolicy.stopChannel 정책 실행 도중 예외가 발생되었습니다.",ex);
//				}
//			}
			
			
		}
	}
	
	//@AfterThrowing(value = "execution(public * rose.mary.trace.database.service.InterfaceService.*())", throwing = "exception")
	
//	public void handleInterfaceException(JoinPoint jp, Throwable exception) {
//		 
//		
//		if(exception instanceof org.apache.ibatis.exceptions.PersistenceException) {
//			 
//			continuousCheckCount2 ++;
//
//			SystemLogger.info("DatabaseErrorHandler 예외처리:" + jp.toLongString());
//			SQLException e = (SQLException)exception.getCause();
//			if(e != null) {
//				SystemLogger.info("sqlState:" + e.getSQLState());
//				SystemLogger.info("errorCode:" + e.getErrorCode());
//				SystemLogger.info("message:" + e.getMessage(), e);
//			}
//			SystemLogger.info("continuousCheckCount2:" + continuousCheckCount2);
//			SystemLogger.info("policyCount:" + policyCount); 
//			if(continuousCheckCount2 >= policyCount) {
//				try {
//					SystemLogger.info("데이터베이스 예외가 발생하여 정책을 실행합니다.", exception);
//					switch(policy.getPolicy()) {
//						case DatabasePolicy.stopChannel :
//							SystemLogger.info("DatabasePolicy.stopChannel 정책을 실행합니다.");
//							serverManager.stopChannel();					
//							continuousCheckCount2 = 0;
//							SystemLogger.info("DatabasePolicy.stopChannel 정책을 실행을 처리하였습니다.");
//							break;
//						case DatabasePolicy.stopServer :
//							SystemLogger.info("DatabasePolicy.stopServer 정책을 실행합니다.");
//							serverManager.stopServer();
//							continuousCheckCount2 = 0;
//							SystemLogger.info("DatabasePolicy.stopServer 정책을 실행을 처리하였습니다.");
//							break;
//						case DatabasePolicy.shutdownServer :
//							SystemLogger.info("DatabasePolicy.shutdownServer 정책을 실행합니다.");
//							serverManager.shutdownServer();
//							continuousCheckCount2 = 0;
//							SystemLogger.info("DatabasePolicy.shutdownServer 정책을 실행을 처리하였습니다.");
//							break;
//						default :
//							SystemLogger.info("DatabasePolicy정보가 존재하지 않습니다.(세팅값:" + policy.getPolicy() + ")");
//							continuousCheckCount2 = 0;
//							break;
//					}			
//					
//				} catch (Exception ex) { 
//					SystemLogger.info("DatabasePolicy.stopChannel 정책 실행 도중 예외가 발생되었습니다.",ex);
//				}
//			}
//			
//			
//		}
//	}
	
	
}
