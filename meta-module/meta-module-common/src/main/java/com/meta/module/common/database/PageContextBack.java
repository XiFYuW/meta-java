package com.meta.module.common.database;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author https://github.com/XiFYuW
 * @since  2020/08/31 9:01
 */

public interface PageContextBack<T> {

    Object buildDataBack(Page<T> page);
}
