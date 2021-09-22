package org.jeecg.erp.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.erp.finance.entity.FinSalInvoiceEntry;

import java.util.List;

/**
 * @Description: 明细
 * @Author: jeecg-boot
 * @Date:   2020-05-21
 * @Version: V1.0
 */
public interface IFinSalInvoiceEntryService extends IService<FinSalInvoiceEntry> {

	public List<FinSalInvoiceEntry> selectByMainId(String mainId);
}
