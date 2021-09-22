package org.jeecg.erp.stock.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 明细
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
@ApiModel(value="stk_check_bill对象", description="盘点卡")
@Data
@TableName("stk_check_bill_entry")
public class StkCheckBillEntry implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "ID")
	private String id;
	/**主表*/
	@ApiModelProperty(value = "主表")
	private String mid;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
	@ApiModelProperty(value = "单据编号")
	private String billNo;
	/**分录号*/
	@Excel(name = "分录号", width = 15)
	@ApiModelProperty(value = "分录号")
	private Integer entryNo;
	/**是否新批次*/
	@Excel(name = "是否新批次", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否新批次")
	private Integer isNewBatch;
	/**仓库*/
	@Excel(name = "仓库", width = 15, dictTable = "bas_warehouse", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "仓库")
	private String warehouseId;
	/**物料*/
	@Excel(name = "物料", width = 15, dictTable = "bas_material", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "物料")
	private String materialId;
	/**批号*/
	@Excel(name = "批号", width = 15)
	@ApiModelProperty(value = "批号")
	private String batchNo;
	/**供应商*/
	@Excel(name = "供应商", width = 15, dictTable = "bas_supplier", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "供应商")
	private String supplierId;
	/**计量单位*/
	@Excel(name = "计量单位", width = 15, dictTable = "bas_measure_unit", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "计量单位")
	private String unitId;
	/**账存数量*/
	@Excel(name = "账存数量", width = 15)
	@ApiModelProperty(value = "账存数量")
	private java.math.BigDecimal bookQty;
	/**实存数量*/
	@Excel(name = "实存数量", width = 15)
	@ApiModelProperty(value = "实存数量")
	private java.math.BigDecimal qty;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**备注2*/
	@Excel(name = "备注2", width = 15)
	@ApiModelProperty(value = "备注2")
	private String remark2;
	/**备注3*/
	@Excel(name = "备注3", width = 15)
	@ApiModelProperty(value = "备注3")
	private String remark3;
	/**版本号*/
	@ApiModelProperty(value = "版本号")
	private Integer version;

	@TableField(exist = false)
	private java.math.BigDecimal profitQty;
	public java.math.BigDecimal getProfitQty() {
		//controller中，QueryWrapper也会创建entity；
		//(uncheckedAmt == null)，是为了避免作为查询数据库参数。
		return (id == null || qty == null) ? null : qty.subtract(bookQty);
	}

}
