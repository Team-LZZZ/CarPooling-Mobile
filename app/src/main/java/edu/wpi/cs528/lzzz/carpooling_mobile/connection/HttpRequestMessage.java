package edu.wpi.cs528.lzzz.carpooling_mobile.connection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QQZhao on 10/28/17.
 */

public class HttpRequestMessage {

    private String url;
    private String method;
    private Map<String, String> params = new HashMap<>();
    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void addParam(String key, String value){
        this.params.put(key, value);
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
