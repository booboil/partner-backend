package com.booboil.partner.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页参数
 *
 * @author niumazlb
 * @create 2022-08-22 11:04
 */
@Data
public class PageRequest implements Serializable {


    private static final long serialVersionUID = 4325971266559386383L;
    /**
     * 页面大小
     */
    protected int pageSize;
    /**
     * 页码
     */
    protected int pageNum;
}
