package com.zdkk.middleware.sdk.infrastructure.weixin.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zdkk
 * @description
 * @create 2026-11-23 18:09
 */
public class TemplateMessageDTO {
    private final String toUser;
    private final String template_id;
    private String url;
    private Map<String, Map<String, String>> data = new HashMap<>();

    public TemplateMessageDTO(String toUser, String template_id) {
        this.toUser = toUser;
        this.template_id = template_id;
    }

    public void put(String key, String value) {
        data.put(key, new HashMap<String, String>() {
            {
                put("value", value);
            }
        });
    }

    public static void put(Map<String, Map<String, String>> data, TemplateKey key, String value) {
        data.put(key.getCode(), new HashMap<String, String>() {
            {
                put("value", value);
            }
        });
    }

    public enum TemplateKey {
        REPO_NAME("repo_name", "项目名称"),
        BRANCH_NAME("branch_name", "分支名称"),
        COMMIT_AUTHOR("commit_author", "提交者"),
        COMMIT_MESSAGE("commit_message", "提交信息"),
        ;

        private String code;
        private String desc;
        TemplateKey(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public String getToUser() {
        return toUser;
    }

    public String getTemplate_id() {
        return template_id;
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
