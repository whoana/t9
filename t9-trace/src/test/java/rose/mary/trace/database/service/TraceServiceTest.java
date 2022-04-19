package rose.mary.trace.database.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import pep.per.mint.common.util.Util;
import rose.mary.trace.data.common.Trace;

@RunWith(SpringRunner.class)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)


public class TraceServiceTest {

	static {
		System.setProperty("rose.mary.home", "/Users/whoana/DEV/workspace-t9/t9/home");
	}

	@Autowired
	TraceService traceService;

	//@Test
	public void testLoad() {
		fail("Not yet implemented");
	}

	//@Test
	public void testExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsert() throws Exception{

			String now = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);

			Trace trace = new Trace();
			trace.setIntegrationId("abcde");
			trace.setHostId("a");
			trace.setDate(now);
			trace.setProcessId("1");
			trace.setProcessDate(now);
			trace.setProcessEndDate(now);
			trace.setType("0");
			trace.setIp("11111");
			trace.setOriginHostId("dddd");
			trace.setOs("a");
			trace.setStatus("99");
			trace.setErrorCode("99");
			trace.setErrorMessage("test");
			trace.setRecordCount(0);
			trace.setDataSize(0);
			trace.setCompress("0");
			trace.setData("000".getBytes());
			trace.setTodoNodeCount(1);
			trace.setId(now);
			trace.setSeq(20);

			traceService.insert(trace);



	}

}
