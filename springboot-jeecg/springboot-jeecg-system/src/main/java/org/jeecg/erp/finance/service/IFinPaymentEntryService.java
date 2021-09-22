package org.jeecg.erp.finance.service;

import org.jeecg.erp.finance.entity.FinPaymentEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 付款明细
 * @Author: jeecg-boot
 * @Date:   2020-04-14
 * @Version: V1.0
 */
public interface IFinPaymentEntryService extends IService<FinPaymentEntry> {

	public List<FinPaymentEntry> selectByMainId(String mainId);
}
