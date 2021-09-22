package org.jeecg.erp.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.erp.base.entity.BasSequence;
import org.jeecg.erp.base.mapper.BasSequenceMapper;
import org.jeecg.erp.base.service.IBasSequenceService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Description: bas_sequence
 * @Author: jeecg-boot
 * @Date: 2020-03-20
 * @Version: V1.0
 */
@Service
@Component
public class BasSequenceServiceImpl extends ServiceImpl<BasSequenceMapper, BasSequence> implements IBasSequenceService {

}
