package com.booboil.partner.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Excel表格用户信息
 */
@Data
@EqualsAndHashCode
public class BooboilUserInfo {

    /**
     * 成员编号
     */
    @ExcelProperty("成员编号")
    private String planetCode;
    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String username;

}