package org.jeecg.erp.base.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.erp.base.entity.BasMeasureUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 计量单位
 * @Author: jeecg-boot
 * @Date:   2020-03-27
 * @Version: V1.0
 */
public interface BasMeasureUnitMapper extends BaseMapper<BasMeasureUnit> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
