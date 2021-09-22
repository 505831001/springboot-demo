package org.jeecg.erp.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.erp.finance.entity.FinReceivableCheckEntry;

import java.util.List;

/**
 * @Description: fin_receivable_check_entry
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface IFinReceivableCheckEntryService extends IService<FinReceivableCheckEntry> {

    public List<FinReceivableCheckEntry> selectByMainId(String mainId);
}
