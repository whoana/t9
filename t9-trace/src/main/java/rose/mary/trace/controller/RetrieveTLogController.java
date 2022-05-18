package rose.mary.trace.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Autowired
    RetrieveTLogService service;

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
        }
        int totalCount = service.getTotalCount(params);
        List<TLog> list = service.retrieve(params);
        Map response = new HashMap();
        response.put("totalCount", totalCount);
        response.put("list", list);
        comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
        comMessage.setResponseObject(response);

        return comMessage;

    }

}
