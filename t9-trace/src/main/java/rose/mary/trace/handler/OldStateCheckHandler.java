package rose.mary.trace.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.core.config.OldStateCheckHandlerConfig;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.StateChecker;
import rose.mary.trace.core.data.common.Trace;

public class OldStateCheckHandler implements StateChecker {

	Logger logger = LoggerFactory.getLogger(getClass());

	OldStateCheckHandlerConfig config;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int KEY_SNDR = 10;
	public static final int KEY_BRKR = 20;
	public static final int KEY_REPL = 30;
	public static final int KEY_RBRK = 40;
	public static final int KEY_RCVR = 50;

	public String statusSuccess = "00";
	public String statusIng = "01";
	public String statusFail = "99";

	boolean usePreviousProcessInfo = false;

	public OldStateCheckHandler(OldStateCheckHandlerConfig config) {
		this.config = config;
	}

	@Override
	public void checkAndSet(boolean first, Trace trace, State state) {

		String trackingDate = trace.getDate();
		String orgHostId = trace.getOriginHostId();
		String status = trace.getStatus();
		String type = trace.getType();
		int typeSeq = config.getNodeMap().putIfAbsent(type, 0);
		int recordCount = trace.getRecordCount();
		int dataAmount = trace.getDataSize();
		String compress = trace.getCompress();
		int cost = 0;
		try {
			cost = Integer.parseInt(trace.getElapsedTime());
		} catch (Exception e) {
			cost = 0;
		}
		int todoNodeCount = trace.getTodoNodeCount();
		String errorCode = trace.getErrorCode();
		String errorMessage = trace.getErrorMessage();
		String integrationId = trace.getIntegrationId();

		// ---------------------------------------
		// TOP0501 order by 정보 세팅을 위한 순서값 정렬
		// ---------------------------------------
		// 개발 보류 : 스펙이 정확하지 않음.
		/*
		 * {
		 * if(config.isUsePreviousProcessInfo()) {
		 * 
		 * LinkedList context = (LinkedList)state.getContext();
		 * 
		 * if(context == null) {
		 * context = new LinkedList<String>();
		 * state.setContext(context);
		 * }
		 * 
		 * if(KEY_SNDR == typeSeq) {
		 * //시작이면 이전 노드 없음
		 * context.add(trace.getId());
		 * }else {
		 * 
		 * 
		 * }
		 * 
		 * }
		 * }
		 */

		state.setSkip(false);
		if (first) {
			// ------------------------------------------
			// 최초로 도착한 헤더정보를 기준으로 세팅되는 값
			// ------------------------------------------
			state.setIntegrationId(integrationId);
			state.setTrackingDate(trackingDate);
			state.setOrgHostId(orgHostId);
			state.setCompress(compress);
			state.setCost(cost);// 처리시간값(단위는 자유)
			state.setDataAmount(dataAmount);
			state.setRecordCount(recordCount);
			state.setTodoNodeCount(todoNodeCount);
			state.setFinishSender(false);

			switch (typeSeq) {
				case KEY_SNDR:
					state.setFinishSender(true);
					// ----------------------------------------------
					// type 이 SNDR 일 경우
					// ----------------------------------------------
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(1);
					} else {

					}
					break;
				case KEY_BRKR:
					KEY_RBRK:
					// ----------------------------------------------
					// type 이 BRKR RBRK 일 경우
					// ----------------------------------------------
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(1);

					} else {
						state.setSkip(true);
					}
					break;
				case KEY_REPL:
					// ----------------------------------------------
					// type 이 REPL 일 경우
					// ----------------------------------------------
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(1);
					} else {
						state.setSkip(true);
					}
					break;

				case KEY_RCVR:
					// ----------------------------------------------
					// type 이 RCVR 일 경우
					// ----------------------------------------------
					state.setFinishNodeCount(1);
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(1);
					} else {

					}
					break;
				default:
					// ----------------------------------------------
					// type 이 정의되지 않은 값일 경우, 에러 또는 처리중으로 할까?
					// ----------------------------------------------
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(1);
					} else {

					}
					break;
			}

		} else {

			switch (typeSeq) {
				case KEY_SNDR:
					state.setFinishSender(true);
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(state.getErrorNodeCount() + 1);
					} else {

					}
					break;
				case KEY_BRKR:
					KEY_RBRK: if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(state.getErrorNodeCount() + 1);
					} else {
						state.setSkip(true);
					}
					break;
				case KEY_REPL:
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(state.getErrorNodeCount() + 1);
					} else {
						state.setSkip(true);
					}
					break;

				case KEY_RCVR:
					state.setFinishNodeCount(state.getFinishNodeCount() + 1);
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(state.getErrorNodeCount() + 1);
					} else {
					}
					break;
				default:
					if (statusFail.equals(status)) {
						state.setErrorCode(errorCode);
						state.setErrorMessage(errorMessage);
						state.setErrorNodeCount(state.getErrorNodeCount() + 1);
					} else {
					}
					break;
			}

		}

		// --------------------------------------------------
		// 완료여부 세팅
		// --------------------------------------------------
		{
			int finishNodeCount = state.getFinishNodeCount();
			if (finishNodeCount >= todoNodeCount) {// 처리해야할 노드 숫자와 발생된 트래킹 노드 수가 일치하면
				if (state.isFinishSender()) {// 첫번째 트래킹을 받았을 경우
					state.setFinish(true); // 완료처리
				} else {
					state.setFinish(false);// 미완료, 첫번째 건이 오지 않음.(이런 경우가 없을 듯 한데. 흠...)
				}
			} else {
				if (state.isFinishSender() && statusFail.equals(status)) { // 첫번째 트래킹이 발생하고 에러일 경우
					state.setFinish(true); // 완료(에러)로 봄
				} else {
					state.setFinish(false); // 첫번째 트래킹이
				}
			}
		}

		// --------------------------------------------------
		// 상태값 세팅
		// --------------------------------------------------
		{
			String oldStatus = state.getStatus();
			if (state.isFinish()) {
				if (statusFail.equals(status)) {
					state.setStatus(statusFail);
				} else {
					if (statusFail.equals(oldStatus)) {
						state.setStatus(statusFail);
					} else {
						state.setStatus(statusSuccess);
					}
				}
			} else {
				if (statusFail.equals(status)) {
					state.setStatus(statusFail);
				} else {
					if (statusFail.equals(oldStatus)) {
						state.setStatus(statusFail);
					} else {
						state.setStatus(statusIng);
					}
				}
			}
		}

	}

}
