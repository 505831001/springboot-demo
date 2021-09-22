package org.jeecg.erp.finance.service;

import org.jeecg.erp.finance.entity.FinPurInvoiceEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 明细
 * @Author: jeecg-boot
 * @Date:   2020-05-21
 * @Version: V1.0
 */
public interface IFinPurInvoiceEntryService extends IService<FinPurInvoiceEntry> {

	public List<FinPurInvoiceEntry> selectByMainId(String mainId);
}
