// InspectionSupplementService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/3/4.
 */
public interface InspectionSupplementService {

    /**
     * 获取检查记录
     * @param params
     * @return
     */
    List<Map<String, Object>> getSupplementLedgerInfo(PageData params);

    /**
     * 编辑检查补录信息
     * @param params
     * @return
     */
    void editSupplementTask(PageData params);

    /**
     * 获取检查补录任务信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getSupplementTask(PageData params);

    /**
     * 获取检查补录任务信息
     * @param params
     * @return
     */
    int getSupplementTaskCount(PageData params);

    /**
     * 新增检查补录信息
     * @param params
     * @return
     */
    void addSupplementTask(PageData params);

    /**
     * 删除检查补录信息
     * @param params
     * @return
     */
    void delSupplementTask(PageData params);

    /**
     * 新增检查补录台账
     * @param params
     * @return
     */
    void addSupplementLedger(PageData params);

    /**
     * 新增制度依据
     * @param params
     * @return
     */
    void addSupplementRule(PageData params);

    /**
     * 根据问题台账ID编辑
     * @param params
     * @return
     */
    void editSupplementLedgerById(PageData params);


    /**
     * 根据台账ID删除
     * @param params
     * @return
     */
    void delSupplementLedgerById(PageData params);

    /**
     * 删除问题台账制度依据
     * @param params
     * @return
     */
    void delSupplementRule(PageData params);

}///:~
