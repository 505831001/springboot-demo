package org.jeecg.erp.finance.service;

import org.jeecg.erp.finance.entity.FinReceivable;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.erp.stock.entity.StkIoBill;
import org.jeecg.erp.stock.entity.StkIoBillEntry;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 应收单
 * @Author: jeecg-boot
 * @Date:   2020-04-30
 * @Version: V1.0
 */
public interface IFinReceivableService extends IService<FinReceivable> {
    void createReceivable(StkIoBill stkIoBill, List<StkIoBillEntry> stkIoBillEntryList);
    void approve(String id);
    boolean isApproved(Integer year, Integer month);
    BigDecimal getUncheckedAmt(String id);
    void checkAmt(String id, BigDecimal amt);
}
