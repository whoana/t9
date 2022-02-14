package rose.mary.trace.apps.manager;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rose.mary.trace.apps.manager.config.TesterManagerConfig;
import rose.mary.trace.apps.tester.GenerateMsgTester;
import rose.mary.trace.service.GenerateTraceMsgService;
import rose.mary.trace.system.SystemLogger;

@Component
public class TesterManager {

	
	TesterManagerConfig config;
	
	GenerateMsgTester generateMsgTester; 
	
	
	@Autowired 
	GenerateTraceMsgService gtms;
	
	public TesterManager(@Autowired ConfigurationManager cm) {
		this.config = cm.getTesterManagerConfig();
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