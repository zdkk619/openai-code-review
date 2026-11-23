package com.zdkk.middleware.sdk.infrastructure.weixin;

import com.alibaba.fastjson2.JSON;
import com.zdkk.middleware.sdk.infrastructure.weixin.dto.TemplateMessageDTO;
import com.zdkk.middleware.sdk.types.util.WXAccessTokenUtils;
import com.zdkk.middleware.sdk.types.util.WXTemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 1:15
 */
public class WeiXin {
    private final Logger logger = LoggerFactory.getLogger(WeiXin.class);

    private final String appid;
    private final String appSecret;
    private final String toUser;
    private final String templateId;

    public WeiXin(String appid, String appSecret, String toUser, String templateId) {
        this.appid = appid;
        this.appSecret = appSecret;
        this.toUser = toUser;
        this.templateId = templateId;
    }

    public void sendTemplateMessage(String logUrl, Map<String, Map<String, String>> data) {
        String accessToken = WXAccessTokenUtils.getAccessToken(appid, appSecret);
        TemplateMessageDTO templateMessageDTO = new TemplateMessageDTO(toUser, templateId);
        templateMessageDTO.setData(data);
        templateMessageDTO.setUrl(logUrl);
        WXTemplateUtils.sendTemplateMessage(accessToken, JSON.toJSONString(templateMessageDTO));
    }
}
