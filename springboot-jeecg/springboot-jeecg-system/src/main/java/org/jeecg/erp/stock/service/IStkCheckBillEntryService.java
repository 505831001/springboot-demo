package org.jeecg.erp.stock.service;

import org.jeecg.erp.stock.entity.StkCheckBillEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 明细
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
public interface IStkCheckBillEntryService extends IService<StkCheckBillEntry> {

	public List<StkCheckBillEntry> selectByMainId(String mainId);
}
