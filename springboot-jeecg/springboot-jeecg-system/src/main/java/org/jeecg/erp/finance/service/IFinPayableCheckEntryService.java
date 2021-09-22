package org.jeecg.erp.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.erp.finance.entity.FinPayableCheckEntry;

import java.util.List;

/**
 * @Description: fin_rp_check_entry
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface IFinPayableCheckEntryService extends IService<FinPayableCheckEntry> {

    public List<FinPayableCheckEntry> selectByMainId(String mainId);
}
