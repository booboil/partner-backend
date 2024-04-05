package com.booboil.partner.once;


import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 *  导入excel数据
 */
public class ImportExcel {
    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器
     * <p>
     * 3. 直接读即可
     */

    public static void main(String[] args) {

        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        String fileName = "D:\\IdeaProject\\PartnerMatch\\partner-backend\\src\\main\\resources\\testExcel.xlsx";
//        readByListener(fileName);
        synchronizedRead(fileName);
    }

    /**
     * 方式一：监听器读取
     * @param fileName
     */
    public static void readByListener(String fileName) {
        EasyExcel.read(fileName, BooboilUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 方式二：同步读取
     * @param fileName
     */
    public static void synchronizedRead(String fileName) {
        // 需要指定class去读，然后读取第一个sheet，同步读取自动完成
        List<BooboilUserInfo> totalList = EasyExcel.read(fileName).head(BooboilUserInfo.class).sheet().doReadSync();
        for (BooboilUserInfo booboilUserInfo : totalList) {
            System.out.println(booboilUserInfo);

        }

    }

}
