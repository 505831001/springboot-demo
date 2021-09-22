package org.jeecg.erp.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.erp.finance.entity.FinSalInvoiceEntry;
import org.jeecg.erp.finance.mapper.FinSalInvoiceEntryMapper;
import org.jeecg.erp.finance.service.IFinSalInvoiceEntryService;
import org.jeecg.erp.stock.entity.StkIoBillEntry;
import org.jeecg.erp.stock.service.IStkIoBillEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 明细
 * @Author:t
 * @Date:   2020-05-24
 * @Version: V1.0
 */
@Service
public class FinSalInvoiceEntryServiceImpl extends ServiceImpl<FinSalInvoiceEntryMapper, FinSalInvoiceEntry> implements IFinSalInvoiceEntryService {
	
	@Autowired
	private FinSalInvoiceEntryMapper finSalInvoiceEntryMapper;
	@Autowired
	private IStkIoBillEntryService stkIoBillEntryService;

	@Override
	public List<FinSalInvoiceEntry> selectByMainId(String mainId) {
		List<FinSalInvoiceEntry> list = finSalInvoiceEntryMapper.selectByMainId(mainId);
		for(FinSalInvoiceEntry entry : list) {
			//蓝单source为stk_io_bill_entry, 红单的source为fin_sal_invoice_entry
			String sourceId = entry.getSourceEntryId();
			if (entry.getSourceType().startsWith("fin_sal_invoice.fin_sal_invoice_entry")) {//红单
				FinSalInvoiceEntry source = getById(sourceId);
				if (source != null) {
					entry.setBlueInvoiceQty(source.getInvoiceQty());
					entry.setBlueInvoiceAmt(source.getInvoiceAmt());
					sourceId = source.getSourceEntryId();
				}
			}

			StkIoBillEntry source = stkIoBillEntryService.getById(sourceId);
			if (source != null) {
				entry.setMaterialId(source.getMaterialId());
				entry.setWarehouseId(source.getWarehouseId());
				entry.setBatchNo(source.getBatchNo());
				entry.setSettleQty(source.getSettleQty());
				entry.setSettleAmt(source.getSettleAmt());
				entry.setInvoicedQty(source.getInvoicedQty());
				entry.setInvoicedAmt(source.getInvoicedAmt());
			}
		}
		return list;
	}

}
