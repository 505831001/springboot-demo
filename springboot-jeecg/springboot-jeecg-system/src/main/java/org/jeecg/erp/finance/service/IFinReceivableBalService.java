package org.jeecg.erp.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.erp.finance.entity.FinReceivableBal;

/**
 * @Description: fin_receivable_bal
 * @Author: jeecg-boot
 * @Date:   2020-05-25
 * @Version: V1.0
 */
public interface IFinReceivableBalService extends IService<FinReceivableBal> {
    void monthCarryForward(Integer year, Integer month);
}
