package com.vlashchevskyi.wsmock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by lvm on 2/21/17.
 */
@RestController
public class RESTController {

    private final String response = "{  \"data\": {    \"translations\": [      {        \"translatedText\": \"Hello John, how are you?\"      },      {        \"translatedText\": \"Salut Jean, comment vas tu?\"      }    ]  }}";

    @RequestMapping(method=GET)
    public String doGETMessage() throws InterruptedException{
        return response;
    }

    @RequestMapping(method=POST)
    public String doPOSTMessage() throws InterruptedException{
        return response;
    }
}