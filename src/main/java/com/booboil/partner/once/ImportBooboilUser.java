package com.booboil.partner.once;

import com.alibaba.excel.EasyExcel;
import jodd.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  导入用户数据到数据库
 */
public class ImportBooboilUser {
    public static void main(String[] args) {

        String fileName = "D:\\IdeaProject\\PartnerMatch\\partner-backend\\src\\main\\resources\\testExcel.xlsx";

        // 需要指定class去读，然后读取第一个sheet，同步读取自动完成
        List<BooboilUserInfo> userInfoList = EasyExcel.read(fileName).head(BooboilUserInfo.class).sheet().doReadSync();
        System.out.println("总数 = " + userInfoList.size());
        Map<String, List<BooboilUserInfo>> listMap =
                userInfoList.stream()
                        .filter(userInfo -> StringUtil.isNotEmpty(userInfo.getUsername()))
                        .collect(Collectors.groupingBy(BooboilUserInfo::getUsername));
        for (Map.Entry<String, List<BooboilUserInfo>> stringListEntry : listMap.entrySet()) {
            if (stringListEntry.getValue().size() > 1) {
                System.out.println("username = " + stringListEntry.getKey());
            }
        }
        System.out.println("不重复的昵称 = "+ listMap.keySet().size());

    }
}
