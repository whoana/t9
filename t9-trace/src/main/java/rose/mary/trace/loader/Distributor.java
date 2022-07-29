package rose.mary.trace.loader;
 
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import rose.mary.trace.core.data.common.Trace;

public class Distributor {
    
    Logger logger = LoggerFactory.getLogger(Distributor.class);

    RestTemplate restTemplate = new RestTemplate();
    String url = "http://127.0.0.1:8090/traces/mergers";
    public void distribute(Map<String, Trace> traces) throws Exception {
        // TODO
        Map res = restTemplate.postForObject(url, traces, Map.class);
        
    }

}
