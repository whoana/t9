package rose.mary.trace.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;
import rose.mary.trace.data.common.TLog;
import rose.mary.trace.database.service.RetrieveTLogService;

@RestController
@CrossOrigin
public class RetrieveTLogController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RetrieveTLogService service;

    @PostConstruct
    public void testRetrieve() {
        logger.info("Retrieving test for");

        
        String toDate = Util.getFormatedDate("yyyyMMddHH");
        String fromDate = Util.getDateAdd("yyyyMMddHH", toDate, -7);

        Map params = new HashMap();
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("offset", 10);
        params.put("rowCount", 10);

        try {
            int count = service.getTotalCount(params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            List<TLog> list = service.retrieve(params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/trace/services/tlogs", params = "method=GET", method = RequestMethod.POST, headers = "content-type=application/json")
    public @ResponseBody ComMessage<Map, Map> retrieve(
            HttpSession httpSession,
            @RequestBody ComMessage<Map, Map> comMessage,
            Locale locale,
            HttpServletRequest request) throws Throwable {

        Map params = comMessage.getRequestObject();
        if (Util.isEmpty(params)) {

            String toDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT);
            String fromDate = Util.getDateAdd(Util.DEFAULT_DATE_FORMAT, toDate, -3);
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
            params.put("offset", 0);
            params.put("rowCount", 10);
            params.put("firstSearch", true);
        }

        boolean firstSearch = params.get("firstSearch") == null ? false : (Boolean) params.get("firstSearch");
        int totalCount = firstSearch ? service.getTotalCount(params) : 0; // 최초 조회시에만 전체 카운트 조회
        List<TLog> list = service.retrieve(params);
        Map response = new HashMap();
        response.put("totalCount", totalCount);
        response.put("list", list);
        comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
        comMessage.setResponseObject(response);

        return comMessage;

    }

}
