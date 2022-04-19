package rose.mary.trace.controller;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@TestPropertySource(properties = {"rose.mary.home=/Users/whoana/Documents/gitlab/t9/t9-trace/bin/main"})
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//	,properties = {
//		"rose.mary.home=/Users/whoana/Documents/gitlab/t9/t9-trace/bin/main"
//	}
)
public class RestChannelControllerTest {

	static {
        System.setProperty("rose.mary.home", "/Users/whoana/Documents/gitlab/t9/t9-trace/bin/main");
    }


	@Autowired
    TestRestTemplate testRestTemplate;


//	@Test
	public void testTrace() {
 		Map<String, Object> trace = new HashMap<String, Object>();


 		trace.put("integrationId"	, "EG_SCMEGS_BD_003");
 		trace.put("originHostId" 	, "host1");
 		trace.put("hostId"       	, "host1");
 		trace.put("date"         	, "20200318133153645645");
 		trace.put("processId"    	, "node1");
 		trace.put("processDate"  	, "20200318122553645645");
 		trace.put("processEndDate"  , "20200318122553645645");
 		trace.put("type"			, "RCVR");
 		trace.put("todoNodeCount"	, 1);
 		trace.put("status"			, "00");

		Map result = testRestTemplate.postForObject("/traces", trace, Map.class);
        assertThat(result).containsKeys("cd","msg");

	}

}
