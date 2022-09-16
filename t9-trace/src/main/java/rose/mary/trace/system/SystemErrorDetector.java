package rose.mary.trace.system;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
import org.springframework.jdbc.UncategorizedSQLException;

import com.ibm.disthub2.impl.formats.OldEnvelop.payload.error;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.policy.SeriousErrorPolicy;
import rose.mary.trace.core.exception.SystemError;
import rose.mary.trace.manager.CacheManager;
import rose.mary.trace.manager.ChannelManager;
import rose.mary.trace.manager.ConfigurationManager;
import rose.mary.trace.manager.ServerManager;

/**
 * <pre>
 * 2022.08.23 현재 확인결과 미완성 소스 , 아래와 같은 소스 에러 발생시 원인 및 히스토리 파악후 개발 완성 필요함. 
 * ------------------------------------------------------------------
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

	SeriousErrorPolicy policy = SeriousErrorPolicy.SHUTDOWN;

	MessageSource messageResource;

	@Autowired
	ConfigurationManager configurationManager;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	ChannelManager channelManager;

	@Autowired
	ServerManager serverManager;

	@Autowired
	MessageSource messageSource;

	private int policyCount = 1;

	CacheProxy<String, SystemError> systemErrorCache = null;

	SystemErrorHandler systemErrorhandler = null;

	Set<String> seriousErrors = new HashSet<String>();

	public void prepare() {
		policyCount = configurationManager.getPolicyConfig().getPolicyCount();
		policy = new SeriousErrorPolicy(configurationManager.getPolicyConfig().getPolicy());
		systemErrorCache = cacheManager.getSystemErrorCache();

		seriousErrors.add(SystemError.T9E0000001.getId());// JdbcConnectFailError
		seriousErrors.add(SystemError.T9E0100001.getId());// IndexUnusableError
		seriousErrors.add(SystemError.T9E0100002.getId());// TablespaceExpendError
		seriousErrors.add(SystemError.T9E0100003.getId());// BadSqlGrammarError
		seriousErrors.add(SystemError.T9E0200001.getId());// CacheFullError
		seriousErrors.add(SystemError.T9E0200002.getId());// DiskFullError

		seriousErrors.add(SystemError.T9E0199999.getId());//

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
	public synchronized void detectException(JoinPoint jp, Throwable exception) /* throws SystemError */ {

		SystemLogger.error("start to detect exception -------");
		SystemLogger.error(Util.join("class:", jp.getClass()));
		SystemLogger.error(Util.join("kind :", jp.getKind()));
		SystemLogger.error(Util.join("name :", jp.getSignature().getName()));
		SystemLogger.error(Util.join("exception :", exception.getClass()));
		SystemLogger.error(Util.join("cause :", exception.getCause()));
		SystemLogger.error("stacktrace:", exception);

		SystemError error = resolve(jp, exception);
		SystemLogger.error("resolve exception:" + error);
		// DatabasePolicyHandler 에서 에러를 캐시에서 꺼내서 policy 를 수행하도록 한다. ()
		// SystemError 중 정책에 따라 처리해야하는 심각한 건만 처리하도록 isSerious 구현 한다.
		if (seriousErrors.contains(error.getId())) {
			SystemLogger.error("Serious Error occured! PolicyHandler will handle this error.");
			try {
				if (!systemErrorCache.containsKey(error.getId())){
					systemErrorCache.put(error.getId(), error);
				}
			} catch (Exception e) {
				SystemLogger.error("SystemErrorCachePutException:", e);
			}
		} else {
			SystemLogger.error("This error will not be handled by PolicyHandler.");
		}
		/*
		 * // TO-DO
		 * // 미개발 부분
		 * // 제품 출시전 반드시 작성되어야 할 부분
		 * // 임시로 체널 또는 시스템을 종료시키도록 한다.
		 * // Scenario 1 :
		 * // 서버가 종료되면 시스템로그를 분석하여 문제를 해결하고 T9재시동
		 * try {
		 * // 에러 부분은 캐시에 넣지 않도록 하자.
		 * // 에러 대량 발생시 디스크 용량에 부담을 줄수 있다.
		 * // 아마도 이부분은 나중에 분석을 위하여 추가 개발을 고려한 듯 하니 일단.
		 * // system.log 에 남기는 것으로 제한한다. 2022.04.22
		 * // systemErrorCache.put(error.getId(), error);
		 * 
		 * // channelManager.stopChannels();
		 * 
		 * StringBuffer msg = new StringBuffer();
		 * msg.append(
		 * "\n****************************************************************");
		 * msg.append(
		 * "\n****************************************************************");
		 * msg.append(
		 * "\n****************************************************************");
		 * msg.
		 * append("\n* T9 will be shutdown. please restart after fixing the problem *");
		 * msg.append(
		 * "\n****************************************************************");
		 * msg.append(
		 * "\n****************************************************************");
		 * msg.append(
		 * "\n****************************************************************");
		 * SystemLogger.error(msg.toString());
		 * serverManager.shutdownServer();
		 * 
		 * } catch (Exception e) {
		 * // TODO Auto-generated catch block
		 * e.printStackTrace();
		 * }
		 */
		// throw error;

	}

	/**
	 * 
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
	private SystemError resolve(JoinPoint jp, Throwable exception) {
		SystemError error = null;
		long lastThrowTime = System.currentTimeMillis();
		if (exception instanceof BadSqlGrammarException) {
			BadSqlGrammarException e = (BadSqlGrammarException) exception;
			error = SystemError.T9E0100003;
			String sql = e.getSql();
			SQLException se = e.getSQLException();
			String state = se.getSQLState();
			String errorCdoe = se.getErrorCode() + "";
			error.setMsg("state:" + state + ", errorCode:" + errorCdoe + ",sql:" + sql);
		} else if (exception instanceof UncategorizedSQLException) {
			Throwable cause = exception.getCause();
			if (cause instanceof SQLException) {
				// SQL state [72000]; error code [1653]; ORA-01653: unable to extend table space
				SQLException se = (SQLException) cause;
				if (se.getErrorCode() == 1653) {
					error = SystemError.T9E0100002;
					String state = se.getSQLState();
					String errorCdoe = se.getErrorCode() + "";
					error.setMsg("state:" + state + ", errorCode:" + errorCdoe);
				} else {
					error = SystemError.T9E0199999;
					error.setMsg(cause.getMessage());
				}
			} else {
				error = SystemError.T9E0199999;
				error.setMsg(cause.getMessage());
			}

		} else if (exception instanceof InvalidResultSetAccessException) {
			error = SystemError.T9E0100009;
			Throwable cause = exception.getCause();
			if (cause instanceof SQLException) {
				SQLException se = (SQLException) cause;
				String state = se.getSQLState();
				String errorCdoe = se.getErrorCode() + "";
				String msg = se.getMessage();
				error.setMsg("state:" + state + ", errorCode:" + errorCdoe + ", msg:" + msg);
			}
		} else if (exception instanceof DuplicateKeyException) {
			error = SystemError.T9E0110001;
			error.setMsg(exception.getMessage());
		} else if (exception instanceof DataIntegrityViolationException) {
			error = SystemError.T9E0110002;
			error.setMsg(exception.getMessage());
		} else if (exception instanceof DataAccessResourceFailureException) {
			error = SystemError.T9E0100005;
			error.setMsg(exception.getMessage());
		} else if (exception instanceof CannotAcquireLockException) {
			error = SystemError.T9E0100006;
			error.setMsg(exception.getMessage());
		} else if (exception instanceof DeadlockLoserDataAccessException) {
			error = SystemError.T9E0100008;
			error.setMsg(exception.getMessage());
		} else if (exception instanceof PersistenceException) {
			error = SystemError.T9E0199999;
			Throwable cause = exception.getCause();
			if (cause instanceof BatchExecutorException) {
				BatchExecutorException e = (BatchExecutorException) cause;
				String msg = "statement:" + e.getFailingSqlStatement() + ", msg:" + e.getMessage();
				error.setMsg(msg);
			} else if (cause instanceof CannotGetJdbcConnectionException) {
				CannotGetJdbcConnectionException e = (CannotGetJdbcConnectionException) cause;
				String msg = "msg:" + e.getMessage();
				error.setMsg(msg);
			} else {
				error.setMsg(cause.getMessage());
			}
		} else if (exception instanceof MyBatisSystemException) {
			error = SystemError.T9E0199999;
			error.setMsg(exception.getMessage());
		} else {
			error = SystemError.UnknownError;
		}

		if (error == null)
			error = SystemError.UnknownError;

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

	/*
	 * 2022.08.23
	 * 시스템 에러 원인이 org.apache.ibatis.executor.BatchResult 일때 해당 예외는 시리얼라이저블이 아니라서 캐시에
	 * 입력시 예외 발생됨 .
	 * 
	 * NotSerializableException
	 * 
	 * 
	 * 23-08 13:30:59.780 [botLoader1] ERROR
	 * o.i.i.i.InvocationContextInterceptor.rethrowException - ISPN000136: Error
	 * executing command PutKeyValueCommand on Cache 'sec01', writing keys
	 * [UnknownError]
	 * org.infinispan.persistence.spi.PersistenceException:
	 * org.infinispan.persistence.spi.PersistenceException:
	 * org.infinispan.commons.marshall.NotSerializableException:
	 * org.apache.ibatis.executor.BatchResult
	 * at
	 * org.infinispan.persistence.file.SingleFileStore.write(SingleFileStore.java:
	 * 373)
	 * at org.infinispan.persistence.manager.PersistenceManagerImpl.
	 * lambda$writeToAllNonTxStores$14(PersistenceManagerImpl.java:697)
	 * at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
	 * at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:175)
	 * at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:175)
	 * at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:
	 * 1384)
	 * at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	 * at
	 * java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
	 * at
	 * java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	 * at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.
	 * java:174)
	 * at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	 * at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	 * at org.infinispan.persistence.manager.PersistenceManagerImpl.
	 * writeToAllNonTxStores(PersistenceManagerImpl.java:693)
	 * at org.infinispan.interceptors.impl.CacheWriterInterceptor.storeEntry(
	 * CacheWriterInterceptor.java:467)
	 * at org.infinispan.interceptors.impl.CacheWriterInterceptor.
	 * lambda$visitPutKeyValueCommand$1(CacheWriterInterceptor.java:185)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextThenAccept(
	 * BaseAsyncInterceptor.java:105)
	 * at org.infinispan.interceptors.impl.CacheWriterInterceptor.
	 * visitPutKeyValueCommand(CacheWriterInterceptor.java:177)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNext(
	 * BaseAsyncInterceptor.java:54)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextThenAccept(
	 * BaseAsyncInterceptor.java:98)
	 * at org.infinispan.interceptors.impl.EntryWrappingInterceptor.
	 * setSkipRemoteGetsAndInvokeNextForDataCommand(EntryWrappingInterceptor.java:
	 * 671)
	 * at org.infinispan.interceptors.impl.EntryWrappingInterceptor.
	 * visitPutKeyValueCommand(EntryWrappingInterceptor.java:302)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextAndFinally(
	 * BaseAsyncInterceptor.java:150)
	 * 23-08 13:30:59.780 [botLoader1] ERROR
	 * o.i.i.i.InvocationContextInterceptor.rethrowException - ISPN000136: Error
	 * executing command PutKeyValueCommand on Cache 'sec01', writing keys
	 * [UnknownError]
	 * org.infinispan.persistence.spi.PersistenceException:
	 * org.infinispan.persistence.spi.PersistenceException:
	 * org.infinispan.commons.marshall.NotSerializableException:
	 * org.apache.ibatis.executor.BatchResult
	 * at
	 * org.infinispan.persistence.file.SingleFileStore.write(SingleFileStore.java:
	 * 373)
	 * at org.infinispan.persistence.manager.PersistenceManagerImpl.
	 * lambda$writeToAllNonTxStores$14(PersistenceManagerImpl.java:697)
	 * at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
	 * at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:175)
	 * at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:175)
	 * at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:
	 * 1384)
	 * at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	 * at
	 * java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
	 * at
	 * java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	 * at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.
	 * java:174)
	 * at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	 * at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	 * at org.infinispan.persistence.manager.PersistenceManagerImpl.
	 * writeToAllNonTxStores(PersistenceManagerImpl.java:693)
	 * at org.infinispan.interceptors.impl.CacheWriterInterceptor.storeEntry(
	 * CacheWriterInterceptor.java:467)
	 * at org.infinispan.interceptors.impl.CacheWriterInterceptor.
	 * lambda$visitPutKeyValueCommand$1(CacheWriterInterceptor.java:185)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextThenAccept(
	 * BaseAsyncInterceptor.java:105)
	 * at org.infinispan.interceptors.impl.CacheWriterInterceptor.
	 * visitPutKeyValueCommand(CacheWriterInterceptor.java:177)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNext(
	 * BaseAsyncInterceptor.java:54)
	 * at org.infinispan.interceptors.impl.CacheLoaderInterceptor.visitDataCommand(
	 * CacheLoaderInterceptor.java:197)
	 * at org.infinispan.interceptors.impl.CacheLoaderInterceptor.
	 * visitPutKeyValueCommand(CacheLoaderInterceptor.java:124)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextThenAccept(
	 * BaseAsyncInterceptor.java:98)
	 * at org.infinispan.interceptors.impl.EntryWrappingInterceptor.
	 * setSkipRemoteGetsAndInvokeNextForDataCommand(EntryWrappingInterceptor.java:
	 * 671)
	 * at org.infinispan.interceptors.impl.EntryWrappingInterceptor.
	 * visitPutKeyValueCommand(EntryWrappingInterceptor.java:302)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextAndFinally(
	 * BaseAsyncInterceptor.java:150)
	 * at org.infinispan.interceptors.locking.AbstractLockingInterceptor.
	 * lambda$nonTxLockAndInvokeNext$1(AbstractLockingInterceptor.java:297)
	 * at org.infinispan.interceptors.SyncInvocationStage.addCallback(
	 * SyncInvocationStage.java:42)
	 * at
	 * org.infinispan.interceptors.InvocationStage.andHandle(InvocationStage.java:
	 * 65)
	 * at org.infinispan.interceptors.locking.AbstractLockingInterceptor.
	 * nonTxLockAndInvokeNext(AbstractLockingInterceptor.java:292)
	 * at org.infinispan.interceptors.locking.AbstractLockingInterceptor.
	 * visitNonTxDataWriteCommand(AbstractLockingInterceptor.java:128)
	 * at org.infinispan.interceptors.locking.NonTransactionalLockingInterceptor.
	 * visitDataWriteCommand(NonTransactionalLockingInterceptor.java:40)
	 * at org.infinispan.interceptors.locking.AbstractLockingInterceptor.
	 * visitPutKeyValueCommand(AbstractLockingInterceptor.java:82)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextAndFinally(
	 * BaseAsyncInterceptor.java:150)
	 * at
	 * org.infinispan.interceptors.impl.CacheMgmtInterceptor.updateStoreStatistics(
	 * CacheMgmtInterceptor.java:217)
	 * at
	 * org.infinispan.interceptors.impl.CacheMgmtInterceptor.visitPutKeyValueCommand
	 * (CacheMgmtInterceptor.java:179)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at org.infinispan.interceptors.BaseAsyncInterceptor.invokeNext(
	 * BaseAsyncInterceptor.java:54)
	 * at org.infinispan.interceptors.DDAsyncInterceptor.handleDefault(
	 * DDAsyncInterceptor.java:54)
	 * at org.infinispan.interceptors.DDAsyncInterceptor.visitPutKeyValueCommand(
	 * DDAsyncInterceptor.java:60)
	 * at org.infinispan.commands.write.PutKeyValueCommand.acceptVisitor(
	 * PutKeyValueCommand.java:73)
	 * at
	 * org.infinispan.interceptors.BaseAsyncInterceptor.invokeNextAndExceptionally(
	 * BaseAsyncInterceptor.java:123)
	 * at
	 * org.infinispan.interceptors.impl.InvocationContextInterceptor.visitCommand(
	 * InvocationContextInterceptor.java:90)
	 * at org.infinispan.interceptors.impl.AsyncInterceptorChainImpl.invoke(
	 * AsyncInterceptorChainImpl.java:248)
	 * at
	 * org.infinispan.cache.impl.CacheImpl.executeCommandAndCommitIfNeeded(CacheImpl
	 * .java:1918)
	 * at org.infinispan.cache.impl.CacheImpl.put(CacheImpl.java:1433)
	 * at org.infinispan.cache.impl.CacheImpl.put(CacheImpl.java:2043)
	 * at org.infinispan.cache.impl.CacheImpl.put(CacheImpl.java:230)
	 * at
	 * org.infinispan.cache.impl.AbstractDelegatingCache.put(AbstractDelegatingCache
	 * .java:448)
	 * at org.infinispan.cache.impl.EncoderCache.put(EncoderCache.java:675)
	 * at rose.mary.trace.core.cache.infinispan.InfinispanCacheProxy.put(
	 * InfinispanCacheProxy.java:45)
	 * at rose.mary.trace.system.SystemErrorDetector.detectException(
	 * SystemErrorDetector.java:103)
	 * at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	 * at
	 * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	 * at
	 * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.
	 * java:43)
	 * at java.lang.reflect.Method.invoke(Method.java:498)
	 * at org.springframework.aop.aspectj.AbstractAspectJAdvice.
	 * invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:644)
	 * at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(
	 * AbstractAspectJAdvice.java:626)
	 * 
	 */
}
