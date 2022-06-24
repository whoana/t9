package rose.mary.trace.manager;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rose.mary.trace.core.config.TesterManagerConfig;
import rose.mary.trace.core.system.SystemLogger;
import rose.mary.trace.service.GenerateTraceMsgService;
import rose.mary.trace.tester.GenerateMsgTester;

//@Component
public class TesterManager {

	
	TesterManagerConfig config;
	
	GenerateMsgTester generateMsgTester; 
	
	
	//@Autowired 
	GenerateTraceMsgService gtms;
	
	public TesterManager(ConfigurationManager cm, GenerateTraceMsgService gtms) {
		this.config = cm.getTesterManagerConfig();
		this.gtms = gtms;
	}
	
	public void start() throws Exception{
		String name = config.getName();
		int exceptionDelay = config.getExceptionDelay();
		int repeatDelaySec = config.getRepeatDelaySec();
		List<Map> paramsList = config.getParamsList();
		SystemLogger.info("GenerateMsgTester starting");
		generateMsgTester = new GenerateMsgTester(name, exceptionDelay, repeatDelaySec, gtms, paramsList);
		generateMsgTester.start();
		SystemLogger.info("GenerateMsgTester started");
	}
	
	public void stop()  {
		SystemLogger.info("GenerateMsgTester stopping");			
		if(generateMsgTester != null) generateMsgTester.stop();
		SystemLogger.info("GenerateMsgTester stopped");		
	}
	
}
