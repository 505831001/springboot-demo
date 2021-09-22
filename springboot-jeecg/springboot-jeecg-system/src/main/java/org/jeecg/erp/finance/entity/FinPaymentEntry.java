package org.jeecg.erp.finance.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 付款明细
 * @Author: jeecg-boot
 * @Date:   2020-04-14
 * @Version: V1.0
 */
@ApiModel(value="fin_payment对象", description="付款单")
@Data
@TableName("fin_payment_entry")
public class FinPaymentEntry implements Serializable {
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
	/**源单类型*/
	@Excel(name = "源单类型", width = 15, dicCode = "x_bill_type")
	@Dict(dicCode = "x_bill_type")
	@ApiModelProperty(value = "源单类型")
	private String sourceType;
	/**源单分录id*/
	@Excel(name = "源单分录id", width = 15)
	@ApiModelProperty(value = "源单分录id")
	private String sourceEntryId;
	/**源单分录号*/
	@Excel(name = "源单分录号", width = 15)
	@ApiModelProperty(value = "源单分录号")
	private String sourceEntryNo;
	/**结算方式*/
	@Excel(name = "结算方式", width = 15, dicCode = "x_settle_method")
	@Dict(dicCode = "x_settle_method")
	@ApiModelProperty(value = "结算方式")
	private String settleMethod;
	/**资金账户*/
	@Excel(name = "资金账户", width = 15, dictTable = "bas_bank_account", dicText = "account_no", dicCode = "id")
	@Dict(dictTable = "bas_bank_account", dicText = "account_no", dicCode = "id")
	@ApiModelProperty(value = "资金账户")
	private String bankAccountId;
	/**金额*/
	@Excel(name = "金额", width = 15)
	@ApiModelProperty(value = "金额")
	private java.math.BigDecimal amt;
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
	/**版本*/
	@ApiModelProperty(value = "版本")
	private Integer version;
}
