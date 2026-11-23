package com.zdkk.middleware.sdk.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zdkk
 * @description
 * @create 2026-11-23 18:09
 */
public class Message {
    private String touser = "oAzFj3IzCRzBliR9R-YXV4iGWU2k";
    private String template_id = "mS3fxrSrT7RplPKMjqpCtaWc4InxVszhchQFN28uHns";
    private String url = "https://github.com/zdkk619/openai-code-review-log/blob/master/2026-11-23/2be37c4b-bed5-44f9-aba1-b171d30f4e24-log.txt";
    private Map<String, Map<String, String>> data = new HashMap<>();

    public void put(String key, String value) {
        data.put(key, new HashMap<String, String>() {
            {
                put("value", value);
            }
        });
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }
}
