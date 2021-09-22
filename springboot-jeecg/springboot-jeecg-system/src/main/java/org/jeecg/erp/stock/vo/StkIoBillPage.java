package org.jeecg.erp.stock.vo;

import java.util.List;

import org.jeecg.erp.stock.entity.StkIoBillEntry;
import lombok.Data;
import org.jeecg.common.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 出入库单
 * @Author: jeecg-boot
 * @Date:   2020-03-31
 * @Version: V1.0
 */
@Data
@ApiModel(value="stk_io_billPage对象", description="出入库单")
public class StkIoBillPage {
	
	/**ID*/
	@ApiModelProperty(value = "ID")
	private String id;
	/**出入库类型*/
	@Excel(name = "出入库类型", width = 15)
	@ApiModelProperty(value = "出入库类型")
	private String stockIoType;
	/**是否自动生成*/
	@Excel(name = "是否自动生成", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否自动生成")
	private Integer isAuto;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
	@ApiModelProperty(value = "单据编号")
	private String billNo;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "单据日期")
	private java.util.Date billDate;
	/**源单类型*/
	@Excel(name = "源单类型", width = 15, dicCode = "x_bill_type")
	@Dict(dicCode = "x_bill_type")
	@ApiModelProperty(value = "源单类型")
	private String sourceType;
	/**源单id*/
	@Excel(name = "源单id", width = 15)
	@ApiModelProperty(value = "源单id")
	private String sourceId;
	/**源单号*/
	@Excel(name = "源单号", width = 15)
	@ApiModelProperty(value = "源单号")
	private String sourceNo;
	/**业务员*/
	@Excel(name = "业务员", width = 15)
	@ApiModelProperty(value = "业务员")
	private String clerkId;
	/**经办人*/
	@Excel(name = "经办人", width = 15)
	@ApiModelProperty(value = "经办人")
	private String handlerId;
	/**是否有往来*/
	@Excel(name = "是否有往来", width = 15)
	@ApiModelProperty(value = "是否有往来")
	private Integer hasRp;
	/**结算是否相同*/
	@Excel(name = "结算是否相同", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	@ApiModelProperty(value = "结算是否相同")
	private Integer isSameSettle;
	/**是否红字*/
	@Excel(name = "是否红字", width = 15)
	@ApiModelProperty(value = "是否红字")
	private Integer isRubric;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
	@ApiModelProperty(value = "供应商")
	private String supplierId;
	/**客户*/
	@Excel(name = "客户", width = 15)
	@ApiModelProperty(value = "客户")
	private String customerId;
	/**附件id*/
	@Excel(name = "附件", width = 15)
	@ApiModelProperty(value = "附件")
	private String attachment;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**处理状态*/
	@Excel(name = "处理状态", width = 15)
	@ApiModelProperty(value = "处理状态")
	private String billProcStatus;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
	@ApiModelProperty(value = "审核人")
	private String approverId;
	/**流程id*/
	@Excel(name = "流程id", width = 15)
	@ApiModelProperty(value = "流程id")
	private String flowId;
	/**是否通过*/
	@Excel(name = "是否通过", width = 15)
	@ApiModelProperty(value = "是否通过")
	private Integer isApproved;
	/**生效时间*/
	@Excel(name = "生效时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "生效时间")
	private java.util.Date effectiveTime;
	/**是否作废*/
	@Excel(name = "是否作废", width = 15)
	@ApiModelProperty(value = "是否作废")
	private Integer isVoided;
	/**是否关闭*/
	@Excel(name = "是否关闭", width = 15)
	@ApiModelProperty(value = "是否关闭")
	private Integer isClosed;
	/**创建部门*/
	@Excel(name = "创建部门", width = 15)
	@ApiModelProperty(value = "创建部门")
	private String sysOrgCode;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	@ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**版本*/
	@Excel(name = "版本", width = 15)
	@ApiModelProperty(value = "版本")
	private Integer version;
	
	@ExcelCollection(name="明细")
	@ApiModelProperty(value = "明细")
	private List<StkIoBillEntry> stkIoBillEntryList;
	
}
