package com.booboil.partner.model.dto;

import com.booboil.partner.common.PageRequest;
import lombok.Data;

import java.util.List;

/**
 * @author booboil
 */
@Data
public class UserQuery extends PageRequest {
    String searchText;
    List<Long> ids;
}
