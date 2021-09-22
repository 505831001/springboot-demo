package org.jeecg.erp.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.erp.finance.entity.FinPayableCheckEntry;
import org.jeecg.erp.finance.mapper.FinPayableCheckEntryMapper;
import org.jeecg.erp.finance.service.IFinPayableService;
import org.jeecg.erp.finance.service.IFinPaymentService;
import org.jeecg.erp.finance.service.IFinPayableCheckEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: fin_rp_check_entry
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Service
public class FinPayableCheckEntryServiceImpl extends ServiceImpl<FinPayableCheckEntryMapper, FinPayableCheckEntry> implements IFinPayableCheckEntryService {

    @Autowired
    private FinPayableCheckEntryMapper finPayableCheckEntryMapper;
    @Autowired
    private IFinPayableService finPayableService;
    @Autowired
    private IFinPaymentService finPaymentService;


    @Override
    public List<FinPayableCheckEntry> selectByMainId(String mainId) {
        List<FinPayableCheckEntry> finPayableCheckEntryList = finPayableCheckEntryMapper.selectByMainId(mainId);
        for(FinPayableCheckEntry entry:finPayableCheckEntryList) {
            //20200428 cfm: 待完善（不同源单类型）
            entry.setUncheckedAmt(entry.getCheckSide().equals("1") ?
                    finPayableService.getUncheckedAmt(entry.getSourceId()) :
                    finPaymentService.getUncheckedAmt(entry.getSourceId()));
        }
        return finPayableCheckEntryList;
    }
}
