package com.meta.module.common.req;

import com.meta.module.common.exception.HintRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * 获取 HttpServletRequest
 */
@Slf4j
public class RequestHolder {

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getNotifyBaseUrl(final String path, final Boolean isLog){
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/").path(path).toUriString();
        try {
            String url = URLDecoder.decode(fileDownloadUri.replaceAll("%5C", "%2F"), "utf-8");
            if (isLog) {
                log.warn("通知地址：{}", url);
            }
            return url;
        } catch (UnsupportedEncodingException e) {
            log.warn("", e);
            throw new HintRuntimeException(e.getMessage());
        }
    }
}
