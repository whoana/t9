package rose.mary.trace.apps.envs;
 
import java.io.Serializable;
import java.util.Arrays;
 

public class SqlErrorCode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	
	private boolean useSqlStateForTranslation = false;

	private String[] badSqlGrammarCodes = new String[0];

	private String[] invalidResultSetAccessCodes = new String[0];

	private String[] duplicateKeyCodes = new String[0];

	private String[] dataIntegrityViolationCodes = new String[0];

	private String[] permissionDeniedCodes = new String[0];

	private String[] dataAccessResourceFailureCodes = new String[0];

	private String[] transientDataAccessResourceCodes = new String[0];

	private String[] cannotAcquireLockCodes = new String[0];

	private String[] deadlockLoserCodes = new String[0];

	private String[] cannotSerializeTransactionCodes = new String[0];
	 
	private String[] tablespaceExpendErrorCodes = new String[0];
	
	private String[] indexUnusableCodes = new String[0]; 	

	public boolean isUseSqlStateForTranslation() {
		return useSqlStateForTranslation;
	}

	public void setUseSqlStateForTranslation(boolean useSqlStateForTranslation) {
		this.useSqlStateForTranslation = useSqlStateForTranslation;
	}

	public String[] getBadSqlGrammarCodes() {
		return badSqlGrammarCodes;
	}

	public void setBadSqlGrammarCodes(String[] badSqlGrammarCodes) {
		this.badSqlGrammarCodes = badSqlGrammarCodes;
		if(this.badSqlGrammarCodes != null) Arrays.sort(this.badSqlGrammarCodes);
	}

	public String[] getInvalidResultSetAccessCodes() {
		return invalidResultSetAccessCodes;
	}

	public void setInvalidResultSetAccessCodes(String[] invalidResultSetAccessCodes) {
		this.invalidResultSetAccessCodes = invalidResultSetAccessCodes;
		if(this.invalidResultSetAccessCodes != null) Arrays.sort(this.invalidResultSetAccessCodes);
	}

	public String[] getDuplicateKeyCodes() {
		return duplicateKeyCodes;
	}

	public void setDuplicateKeyCodes(String[] duplicateKeyCodes) {
		this.duplicateKeyCodes = duplicateKeyCodes;
		if(this.duplicateKeyCodes != null) Arrays.sort(this.duplicateKeyCodes);
	}

	public String[] getDataIntegrityViolationCodes() {
		return dataIntegrityViolationCodes;
	}

	public void setDataIntegrityViolationCodes(String[] dataIntegrityViolationCodes) {
		this.dataIntegrityViolationCodes = dataIntegrityViolationCodes;
		if(this.dataIntegrityViolationCodes != null) Arrays.sort(this.dataIntegrityViolationCodes);
	}

	public String[] getPermissionDeniedCodes() {
		return permissionDeniedCodes;
	}

	public void setPermissionDeniedCodes(String[] permissionDeniedCodes) {
		this.permissionDeniedCodes = permissionDeniedCodes;
		if(this.permissionDeniedCodes != null) Arrays.sort(this.permissionDeniedCodes);
	}

	public String[] getDataAccessResourceFailureCodes() {
		return dataAccessResourceFailureCodes;
	}

	public void setDataAccessResourceFailureCodes(String[] dataAccessResourceFailureCodes) {
		this.dataAccessResourceFailureCodes = dataAccessResourceFailureCodes;
		if(this.dataAccessResourceFailureCodes != null)Arrays.sort(this.dataAccessResourceFailureCodes);
	}

	public String[] getTransientDataAccessResourceCodes() {
		return transientDataAccessResourceCodes;
	}

	public void setTransientDataAccessResourceCodes(String[] transientDataAccessResourceCodes) {
		this.transientDataAccessResourceCodes = transientDataAccessResourceCodes;
		if(this.transientDataAccessResourceCodes != null)Arrays.sort(this.transientDataAccessResourceCodes);
	}

	public String[] getCannotAcquireLockCodes() {
		return cannotAcquireLockCodes;
	}

	public void setCannotAcquireLockCodes(String[] cannotAcquireLockCodes) {
		this.cannotAcquireLockCodes = cannotAcquireLockCodes;
		if(this.cannotAcquireLockCodes != null) Arrays.sort(this.cannotAcquireLockCodes);
	}

	public String[] getDeadlockLoserCodes() {
		return deadlockLoserCodes;
	}

	public void setDeadlockLoserCodes(String[] deadlockLoserCodes) {
		this.deadlockLoserCodes = deadlockLoserCodes;
		if(this.deadlockLoserCodes != null) Arrays.sort(this.deadlockLoserCodes);
	}

	public String[] getCannotSerializeTransactionCodes() {
		return cannotSerializeTransactionCodes;
	}

	public void setCannotSerializeTransactionCodes(String[] cannotSerializeTransactionCodes) {
		this.cannotSerializeTransactionCodes = cannotSerializeTransactionCodes;
		if(this.cannotSerializeTransactionCodes != null) Arrays.sort(this.cannotSerializeTransactionCodes);
	}

	public String[] getTablespaceExpendErrorCodes() {
		return tablespaceExpendErrorCodes;
	}

	public void setTablespaceExpendErrorCodes(String[] tablespaceExpendErrorCodes) {
		this.tablespaceExpendErrorCodes = tablespaceExpendErrorCodes;
		if(this.tablespaceExpendErrorCodes != null) Arrays.sort(this.tablespaceExpendErrorCodes);
	}

	public String[] getIndexUnusableCodes() {
		return indexUnusableCodes;
	}

	public void setIndexUnusableCodes(String[] indexUnusableCodes) {
		this.indexUnusableCodes = indexUnusableCodes;
		if(this.indexUnusableCodes != null) Arrays.sort(this.indexUnusableCodes);
	}
	
	

}
