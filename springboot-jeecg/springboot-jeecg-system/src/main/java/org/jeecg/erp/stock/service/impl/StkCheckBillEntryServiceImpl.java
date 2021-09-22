package org.jeecg.erp.stock.service.impl;

import org.jeecg.erp.stock.entity.StkCheckBillEntry;
import org.jeecg.erp.stock.mapper.StkCheckBillEntryMapper;
import org.jeecg.erp.stock.service.IStkCheckBillEntryService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 明细
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
@Service
public class StkCheckBillEntryServiceImpl extends ServiceImpl<StkCheckBillEntryMapper, StkCheckBillEntry> implements IStkCheckBillEntryService {
	
	@Autowired
	private StkCheckBillEntryMapper stkCheckBillEntryMapper;
	
	@Override
	public List<StkCheckBillEntry> selectByMainId(String mainId) {
		return stkCheckBillEntryMapper.selectByMainId(mainId);
	}
}
