package rose.mary.trace.core.system;

/**
 * 
 * @author whoana
 *
 */
public class SystemError extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static int TYPE_SYSTEM_ERROR     = 0;
	public final static int TYPE_NON_SYSTEM_ERROR = 1; 
	
	public final static int KIND_NET 	= 0;
	public final static int KIND_DB     = 1;
	public final static int KIND_DISK	= 2;
	public final static int KIND_CPU    = 3;
	public final static int KIND_MEM    = 4;
	
	
	//------------------------------------------------------------	
	//SystemError naming rule: T9E + KIND[2] + TYPE[1] + SEQ[4]
	//------------------------------------------------------------
	/** JdbcConnectFailError */
	public static SystemError T9E0000001 = new SystemError("T9E0000001", "JdbcConnectFailError", 			TYPE_SYSTEM_ERROR, KIND_NET );
	/** NetworkConnectFailError */
	public static SystemError T9E0000002 = new SystemError("T9E0000002", "NetworkConnectFailError", 		TYPE_SYSTEM_ERROR, KIND_NET );
	/** ChannelConnectFailError */
	public static SystemError T9E0010001 = new SystemError("T9E0010001", "ChannelConnectFailError", 		TYPE_NON_SYSTEM_ERROR, KIND_NET );
	/** IndexUnusableError */
	public static SystemError T9E0100001 = new SystemError("T9E0100001", "IndexUnusableError", 				TYPE_SYSTEM_ERROR, KIND_DB );
	/** TablespaceExpendError */
	public static SystemError T9E0100002 = new SystemError("T9E0100002", "TablespaceExpendError", 			TYPE_SYSTEM_ERROR, KIND_DB );
	/** BadSqlGrammarError */
	public static SystemError T9E0100003 = new SystemError("T9E0100003", "BadSqlGrammarError", 				TYPE_SYSTEM_ERROR, KIND_DB );
	/** InvalidResultSetAccessError */
	public static SystemError T9E0100004 = new SystemError("T9E0100004", "InvalidResultSetAccessError", 	TYPE_SYSTEM_ERROR, KIND_DB );
	/** DataAccessResourceFailureError */
	public static SystemError T9E0100005 = new SystemError("T9E0100005", "DataAccessResourceFailureError", 	TYPE_SYSTEM_ERROR, KIND_DB );//The Network Adapter could not establish the connection 업체코드17002, 오라클 리스터 문제
	/** CannotAcquireLockError */
	public static SystemError T9E0100006 = new SystemError("T9E0100006", "CannotAcquireLockError", 			TYPE_SYSTEM_ERROR, KIND_DB );//ORA-00054: 자원이 사용중이고, NOWAIT가 지정되어 있습니다. (table lock)
	/** CannotSerializeTransactionError */
	public static SystemError T9E0100007 = new SystemError("T9E0100007", "CannotSerializeTransactionError", TYPE_SYSTEM_ERROR, KIND_DB );
	/** DeadlockLoserError */
	public static SystemError T9E0100008 = new SystemError("T9E0100008", "DeadlockLoserError", 				TYPE_SYSTEM_ERROR, KIND_DB );//deadlock detected while waiting for resource
	/** DuplicateKeyError */ 
	public static SystemError T9E0110001 = new SystemError("T9E0110001", "DuplicateKeyError", 				TYPE_NON_SYSTEM_ERROR, KIND_DB );
	/** DataIntegrityViolationError */
	public static SystemError T9E0110002 = new SystemError("T9E0110002", "DataIntegrityViolationError", 	TYPE_SYSTEM_ERROR, KIND_DB ); //Null 이 입력되지 않는 필드에 Null를 입력하려 하기때문에 생기는 에러
	/** CacheFullError */
	public static SystemError T9E0200001 = new SystemError("T9E0200001", "CacheFullError", 					TYPE_SYSTEM_ERROR, KIND_DISK );
	/** DiskFullError */
	public static SystemError T9E0200002 = new SystemError("T9E0200002", "DiskFullError", 					TYPE_SYSTEM_ERROR, KIND_DISK );
	
	/** UnknownError */
	public static SystemError UnknownError = new SystemError("UnknownError", "UnknownError", 					TYPE_SYSTEM_ERROR, -1);
	
	
	String id;
	
	int type;
	
	int kind;
	
	String name;
	
	String msg;
	 
	Throwable cause;
	
	long lastThrowTime;
		
	public SystemError() {
		super();
	}

	public SystemError(String id, String name, int type, int kind) {
		this();
		this.id = id;
		this.name = name;
		this.type = type;
		this.kind = kind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public long getLastThrowTime() {
		return lastThrowTime;
	}

	public void setLastThrowTime(long lastThrowTime) {
		this.lastThrowTime = lastThrowTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
