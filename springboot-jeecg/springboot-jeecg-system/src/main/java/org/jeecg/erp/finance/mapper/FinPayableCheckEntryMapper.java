package org.jeecg.erp.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.erp.finance.entity.FinPayableCheckEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: fin_payable_check_entry
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface FinPayableCheckEntryMapper extends BaseMapper<FinPayableCheckEntry> {

    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<FinPayableCheckEntry> selectByMainId(@Param("mainId") String mainId);
}
