package com.booboil.partner.model.dto;

import com.booboil.partner.common.PageRequest;
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
