package com.booboil.partner.model.vo;

import com.booboil.partner.model.domain.Im;
import lombok.Data;

/**
 * @author booboil
 */
@Data
public class ImMessageVo {

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息
     */
    private Im im;

}
