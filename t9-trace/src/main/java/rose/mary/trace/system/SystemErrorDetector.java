package rose.mary.trace.system;

import java.sql.BatchUpdateException;
import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchExecutorException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.InvalidResultSetAccessException;

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.manager.CacheManager;
import rose.mary.trace.apps.manager.ConfigurationManager;
import rose.mary.trace.data.policy.DatabasePolicy;

/**
 * <pre>
 * 데이터베이스 Error 발생시 예외를 캐치하여 systemErrorCache 에 전달한다.
 * SystemErrorDetector.java
 * ------------------------------------------------------------------
 * </pre>
 * 
 * @author whoana
 * @since 20200515
 */
@Aspect
public class SystemErrorDetector {

	DatabasePolicy policy = DatabasePolicy.SHUTDOWN;

	MessageSource messageResource;

	@Autowired
	ConfigurationManager configurationManager;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	MessageSource messageSource;

	private int policyCount = 1;

	CacheProxy<String, SystemError> systemErrorCache = null;

	SystemErrorHandler systemErrorhandler = null;

	public void prepare() {
		policyCount = configurationManager.getDatabasePolicyConfig().getPolicyCount();
		policy = new DatabasePolicy(configurationManager.getDatabasePolicyConfig().getPolicy());
		systemErrorCache = cacheManager.getSystemErrorCache();
	}

	/**
	 * <pre>
	 * 에러처리부 
	 * 이벤트 방식 처리를 할 것인지 캐시 전달 모델로 처리할 것인지 고민해 볼것 (20205029) 
	 * 이벤트 방식이 좋을 듯 싶네만. 조금 더 고민해보자고요.
	 * 
	 * cache full 은 어떻게 체크할 것인가?
	 * 
	 * </pre>
	 * 
	 * @param jp
	 * @param exception
	 */
	// @AfterThrowing(pointcut = "execution(public *
	// rose.mary.trace.database.service.*Service.*())", throwing = "exception")
	// @AfterThrowing(pointcut = "execution(public *
	// rose.mary.trace.database.service.*Service.*())", throwing = "exception")
	@AfterThrowing(pointcut = "execution(public * rose..*(..))", throwing = "exception")
	public void detectException(JoinPoint jp, Throwable exception) /* throws SystemError */ {

		SystemLogger.info("start to detect database exception -------");
		SystemLogger.info(Util.join("class:", jp.getClass()));
		SystemLogger.info(Util.join("kind :", jp.getKind()));
		SystemLogger.info(Util.join("name :", jp.getSignature().getName()));
		SystemLogger.info(Util.join("exception :", exception.getClass()));
		SystemLogger.info(Util.join("cause :", exception.getCause()));
		SystemLogger.info("stacktrace:", exception);

		SystemError error = databaseErrorDetect(jp, exception);

		try {
			// 에러 부분은 캐시에 넣지 않도록 하자.
			// 에러 대량 발생시 디스크 용량에 부담을 줄수 있다.
			// 아마도 이부분은 나중에 분석을 위하여 추가 개발을 고려한 듯 하니 일단.
			// system.log 에 남기는 것으로 제한한다. 2022.04.22
			systemErrorCache.put(error.getId(), error);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// throw error;

	}

	/**
	 * database 에러의 경우 스프링프레임워크는 DB 종류에 상관없이 내부적으로 매핑된 정의에 맞는 예외를 발생시킨다.
	 * 예를 들어 DB가 오라클일 경우 에러코드가 ["900","903","904","917","936","942", "6550",
	 * "17006"] 일경우
	 * org.springframework.jdbc.BadSqlGrammarException 을 발생시킨다.
	 * 이외에도 아래와 같은 예외가 발생될 수 있으며 아래 이외의 예외에 대해서는 원본 예외를 그대로 전달한다.
	 * 
	 * InvalidResultSetAccessException ["17003"]
	 * DuplicateKeyException ["1"]
	 * DataIntegrityViolationException ["1400","1722","2291","2292"],
	 * DataAccessResourceFailureException ["17002","17447"]
	 * CannotAcquireLockException ["54","3000"]
	 * CannotSerializeTransactionException ["8177"]
	 * DeadlockLoserDataAccessException ["60"]
	 * 
	 * @param jp
	 * @param exception
	 */
	private SystemError databaseErrorDetect(JoinPoint jp, Throwable exception) {
		SystemError error = null;
		long lastThrowTime = System.currentTimeMillis();
		if (exception instanceof BadSqlGrammarException) {
			BadSqlGrammarException e = (BadSqlGrammarException) exception;
			String sql = e.getSql();
			SQLException se = e.getSQLException();
			String state = se.getSQLState();
			String errorCdoe = se.getErrorCode() + "";
			error = SystemError.T9E0100003;
			// error.setMsg(msg);

		} else if (exception instanceof InvalidResultSetAccessException) {

		} else if (exception instanceof DuplicateKeyException) {

		} else if (exception instanceof DataIntegrityViolationException) {

		} else if (exception instanceof DataAccessResourceFailureException) {

		} else if (exception instanceof CannotAcquireLockException) {

		} else if (exception instanceof DeadlockLoserDataAccessException) {

		} else if (exception instanceof PersistenceException) {

			Throwable t = exception.getCause();

			if (t instanceof BatchExecutorException) {
				BatchExecutorException e = (BatchExecutorException) t;
				Throwable cause = e.getCause();
				if (cause instanceof BatchUpdateException) {

				} else {

				}

			} else if (t instanceof CannotGetJdbcConnectionException) {
				CannotGetJdbcConnectionException e = (CannotGetJdbcConnectionException) t;

			} else {

			}

		} else if (exception instanceof MyBatisSystemException) {
			MyBatisSystemException mse = (MyBatisSystemException) exception;

		} else {
			error = SystemError.UnknownError;
		}

		if (error == null)
			error = SystemError.UnknownError;

		error.setCause(exception);
		error.setLastThrowTime(lastThrowTime);

		return error;

	}

	// retry-trace.log:org.infinispan.commons.CacheException:
	// java.lang.OutOfMemoryError: Java heap space
	//
	// rosemary_19-05-2020_0.log
	//
	//
	// rosemary_15-05-2020_0.log:org.apache.ibatis.exceptions.PersistenceException:
	// rosemary_15-05-2020_0.log:### Error flushing statements. Cause:
	// org.apache.ibatis.executor.BatchExecutorException:
	// rose.mary.trace.database.mapper.m01.TraceMapper.insert (batch index #1)
	// failed. Cause: java.sql.BatchUpdateException: ORA-01653: IIPDMC.TOP0501 테이블을
	// 8192(으)로 IIPDMC_DAT_TBS 테이블스페이스에서 확장할 수 없습니다
	// rosemary_15-05-2020_0.log:### Cause:
	// org.apache.ibatis.executor.BatchExecutorException:
	// rose.mary.trace.database.mapper.m01.TraceMapper
	//
	// rosemary_19-05-2020_0.log:org.apache.ibatis.exceptions.PersistenceException:
	// rosemary_19-05-2020_0.log:### Error flushing statements. Cause:
	// org.apache.ibatis.executor.BatchExecutorException:
	// rose.mary.trace.database.mapper.m01.TraceMapper.insert (batch index #1)
	// failed. Cause: java.sql.BatchUpdateException: ORA-01654: IIPDMC.PK_TOP0501
	// 인덱스를 8192(으)로 IIPDMC_DAT_TBS 테이블스페이스에서 확장할 수 없습니다
	//
	//
	// rosemary_30-04-2020_0.log:org.apache.ibatis.exceptions.PersistenceException:
	// rosemary_30-04-2020_0.log:### Error querying database. Cause:
	// org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain
	// JDBC Connection; nested exception is
	// java.sql.SQLTransientConnectionException: HikariPool-2 - Connection is not
	// available, request timed out after 30000ms.
	//

	// 1)
	// org.springframework.jdbc.BadSqlGrammarException:
	// ### Error querying database. Cause: java.sql.SQLSyntaxErrorException:
	// ORA-00942: 테이블 또는 뷰가 존재하지 않습니다
	// 2)
	// org.mybatis.spring.MyBatisSystemException
	// org.mybatis.spring.MyBatisSystemException: nested exception is
	// org.apache.ibatis.exceptions.PersistenceException:
	// ### Error querying database. Cause:
	// org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain
	// JDBC Connection; nested exception is java.sql.SQLException: HikariDataSource
	// HikariDat

}
