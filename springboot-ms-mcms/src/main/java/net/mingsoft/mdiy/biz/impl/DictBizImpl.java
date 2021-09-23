/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.mingsoft.mdiy.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.mdiy.biz.IDictBiz;
import net.mingsoft.mdiy.dao.IDictDao;
import net.mingsoft.mdiy.entity.DictEntity;

import java.util.List;


/**
 * 字典表管理持久化层
 * @author 铭飞开发团队
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2016-9-8 17:11:19<br/>
 * 历史修订：<br/>
 */
@Service("dictBizImpl")
public class DictBizImpl extends BaseBizImpl implements IDictBiz {


    @Autowired
    private IDictDao dictDao;


    @Override
    protected IBaseDao getDao() {
        // TODO Auto-generated method stub
        return dictDao;
    }


    @Override
    public DictEntity getByTypeAndLabelAndValue(String dictType, String dictLabel , String dictValue) {
        DictEntity dict = new DictEntity();
        dict.setDictType(dictType);
        dict.setDictLabel(dictLabel);
        dict.setDictValue(dictValue);
        return (DictEntity) dictDao.getByEntity(dict);
    }

    @Override
    public List<DictEntity> dictType(DictEntity dictEntity)
    {
        return dictDao.dictType(dictEntity);
    }

    @Override
    public List queryExcludeApp(DictEntity dictEntity) {
        return dictDao.queryExcludeApp(dictEntity);
    }
}
