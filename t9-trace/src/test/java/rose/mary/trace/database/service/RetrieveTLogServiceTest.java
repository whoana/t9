package rose.mary.trace.database.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rose.mary.trace.data.common.TLog;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RetrieveTLogServiceTest {
    static {
        System.setProperty("rose.mary.home", "/Users/whoana/DEV/workspace-vs/t9/home");
    }
    @Autowired
    RetrieveTLogService service;

    @Test
    public void retrieve() throws Exception {
        Map params = new HashMap();
        params.put("integrationId", "TEST004");
        List<TLog> list = service.retrieve(params);
        assertNotNull(list);
    }
}
