package com.booboil.partner.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.booboil.partner.mapper.ImMapper;
import com.booboil.partner.model.domain.Im;
import com.booboil.partner.service.ImService;
import org.springframework.stereotype.Service;

/**
* @author booboil
* @description 针对表【im】的数据库操作Service实现
*/
@Service
public class ImServiceImpl extends ServiceImpl<ImMapper, Im>
    implements ImService {

}




