package com.booboil.partner.model.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author booboil
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 1950536713854398674L;
    /**
     * id
     */
    private Long teamId;

}
