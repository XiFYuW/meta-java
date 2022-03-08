package com.meta.nat.app.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.nacos.common.utils.StringUtils;
import com.meta.nat.app.config.NatConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReadFileService {

    private final NatConfig natConfig;
    private final AtomicReference<String> urlAtomicReference = new AtomicReference<>("");
    private final AtomicBoolean urlAtomicReferenceIsOk = new AtomicBoolean(false);

    public ReadFileService(NatConfig natConfig) {
        this.natConfig = natConfig;
    }

    private void heartbeat(){
        try {
            if (!StringUtils.isBlank(urlAtomicReference.get())) {
                urlAtomicReferenceIsOk.set(HttpRequest.get(urlAtomicReference.get()).execute().isOk());
                log.warn("服务{}，心跳检测返回：{}", urlAtomicReference.get(), urlAtomicReferenceIsOk.get());
            }
            if (!urlAtomicReferenceIsOk.get()) {
                boolean localIsOk = HttpRequest.get(natConfig.getLocal()).execute().isOk();
                log.warn("服务{}，心跳检测返回：{}", natConfig.getLocal(), localIsOk);

                if (localIsOk) {
                    Process proc = null;
                    try {
                        proc = Runtime.getRuntime().exec(natConfig.getPerformStart());
                        proc.waitFor();
                    }catch (Throwable t) {
                        log.warn("心跳检测执行脚本: {} 失败: {}", natConfig.getPerformStart(), t.getMessage());
                    }finally {
                        if (proc != null) {
                            proc.destroy();
                            send();
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.warn("心跳检测失败: {}", e.getMessage());
        }

    }

    private void send(){
        final String urls = redsFile();
        log.warn("当前服务访问地址: " + urls);

        if (!StringUtils.isEmpty(urls) && !urlAtomicReference.get().equals(urls)) {
            urlAtomicReference.set(urls);
            //mailService.sendHtmlMail(urls);
        }
    }

    private synchronized String redsFile(){
        try {
            final List<String> lineData = FileUtil.readLines(natConfig.getLog(), CharsetUtil.CHARSET_UTF_8, new LinkedList<>());
            log.warn("lineData size: {}", lineData.size());
            final List<String> panList = lineData.stream().filter(x -> x.contains(natConfig.getPan())).collect(Collectors.toList());
            final AtomicReference<List<String>> atomicReference = new AtomicReference<>();
            panList
                    .stream()
                    .map(x -> {
                        final String strDate = x.substring(1, 20);
                        return DateUtil.parse(strDate, natConfig.getFormat());
                    })
                    .max(Date::compareTo)
                    .ifPresent(y -> {
                        String dateTime = DateUtil.format(y, natConfig.getFormat());
                        log.warn("最早日期：{}", dateTime);
                        atomicReference.set(
                                panList
                                        .stream()
                                        .filter(x -> x.contains(dateTime))
                                        .map(x -> {
                                            int index = x.indexOf(natConfig.getPan());
                                            int len = x.length();
                                            return x.substring(index, len);
                                        }).collect(Collectors.toList())
                        );

                    });

            List<String> result = atomicReference.get();
            if (CollectionUtils.isEmpty(result)) {
                return "";
            }
            return result.get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
