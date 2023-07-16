package com.niuma.langbei.model.dto;

import com.niuma.langbei.common.PageRequest;
import lombok.Data;

import java.util.List;

/**
 * @author niuma
 * @create 2023-02-10 22:22
 */
@Data
public class UserQuery extends PageRequest {
    String searchText;
    List<Long> ids;
}
