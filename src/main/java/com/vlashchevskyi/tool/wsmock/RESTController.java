package com.vlashchevskyi.tool.wsmock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by lvm on 2/21/17.
 */
@RestController
public class RESTController {
    private final String SOURCE_MESSAGE = "Hello John, how are you?";
    private final String TARGET_MESSAGE = "Salut Jean, comment vas tu?";

    @RequestMapping(method=GET
            , params = {"q", "source", "target"}
            , headers="host=api.google.com:8080"
            , produces = "application/json; charset=UTF-8")
    public Map doGETMessage(WebRequest request) throws InterruptedException{
        String[] values = request.getParameterValues("q");

        if (values != null) {
            int amount = values.length;
            Map response = buildResponse(amount, TARGET_MESSAGE);
            return response;
        }

        return new HashMap();
    }

    @RequestMapping(method=POST
            , params = {"q", "source", "target"}
            , headers="host=api.google.com:8080"
            , produces = "application/json; charset=UTF-8")
    public Map doPOSTMessage(WebRequest request) throws InterruptedException{
        return doGETMessage(request);
    }

        private Map buildResponse(int amount, String tgtMessage) {

        Map targetTranslated = new HashMap();
        targetTranslated.put("translatedText", tgtMessage);

        List tBlock = new ArrayList();
        for (int i = 0; i < amount; i++) {
            tBlock.add(targetTranslated);
        }

        Map translations = new LinkedHashMap();
        translations.put("translations", tBlock);

        Map data = new LinkedHashMap();
        data.put("data", translations);

        return data;
    }
}