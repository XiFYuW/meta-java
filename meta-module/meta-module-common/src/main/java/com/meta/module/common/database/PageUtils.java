package com.meta.module.common.database;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author https://github.com/XiFYuW
 * @since  2020/08/29 17:11
 */
public class PageUtils {

    public static <T> Page<T> getPage(long current, long size){
        Page<T> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        return page;
    }

    public static <T> IPage<T> getIPage(IPage<T> page, long current, long size){
        page.setCurrent(current);
        page.setSize(size);
        return page;
    }


    public static <T> Map<String, Object> getDateMap(PageContext<T> pageContext){
        Page<T> page = pageContext.buildData();
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return data;
    }

    public static <T> Map<String, Object> getDateMapBack(PageContext<T> pageContext, PageContextBack<T> pageContextBack){
        Page<T> page = pageContext.buildData();
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", pageContextBack.buildDataBack(page));
        return data;
    }

    public static <T> List<T> getDateMapByRecords(PageContext<T> pageContext){
        Page<T> page = pageContext.buildData();
        return page.getRecords();
    }

    public static Map<String, Object> getMap(long total, final Object o){
        final Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", o);
        return map;
    }
}
