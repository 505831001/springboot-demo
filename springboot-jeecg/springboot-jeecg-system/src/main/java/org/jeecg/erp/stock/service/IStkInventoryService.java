package org.jeecg.erp.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.erp.stock.entity.StkInventory;
import org.jeecg.erp.stock.entity.StkIoBillEntry;

/**
 * @Description: 库存
 * @Author: jeecg-boot
 * @Date:   2020-04-11
 * @Version: V1.0
 */
public interface IStkInventoryService extends IService<StkInventory> {
    void increase(StkIoBillEntry stkIoBillEntry);
    void decrease(StkIoBillEntry stkIoBillEntry);
    void changeCost(StkIoBillEntry stkIoBillEntry);
    StkInventory getByBatchNoAndX(String batchNo, String materialId, String warehouseId);
}
