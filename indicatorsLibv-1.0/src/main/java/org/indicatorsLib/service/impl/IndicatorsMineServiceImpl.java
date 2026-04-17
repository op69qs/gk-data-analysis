// IndicatorsMineNewServiceImpl.java

package org.indicatorsLib.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.indicatorsLib.dao.mapper.indicatorsLib.IndicatorsMineMapper;
import org.indicatorsLib.service.IndicatorsMineService;
import org.indicatorsLib.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 我的指标
 *
 * @author Created by Samer on 2019/12/30.
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class IndicatorsMineServiceImpl implements IndicatorsMineService {

    @Autowired
    private IndicatorsMineMapper indicatorsMineMapper;
    @Autowired
    private DataSource dataSource;

    @Override
    public List<Map<String, Object>> getIndexParentInfo(PageData params) {
        return indicatorsMineMapper.getIndexParentInfo(params);
    }


    @Override
    public void detelePublicRelation(PageData params) {
        indicatorsMineMapper.detelePublicRelation(params);
    }

    @Override
    public List<Map<String, Object>> getIndexDimnsn(String[] indexArray) {
        return indicatorsMineMapper.getIndexDimnsn(indexArray);
    }

    @Override
    public List<Map<String, Object>> getIndexPeriod(String[] indexArray) {
        return indicatorsMineMapper.getIndexPeriod(indexArray);
    }

    /**
     * 指标管理获取指标信息
     *
     * @param params
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map<String, Object>> getIndexManageList(PageData params) {
        if(indicatorsMineMapper.selectRoleByUserId(params) > 0){ //管理员角色，查询个人的和公共指标
            params.put("PERSONAL_FLAG","true");
            params.put("USERID","");
        }
        return indicatorsMineMapper.getIndexManageList(params);
    }

    /**
     * 指标管理获取指标计数
     *
     * @param params
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int getIndexManageCount(PageData params) {
        return indicatorsMineMapper.getIndexManageCount(params);
    }

    /**
     * 指标SQL试运行(获取数据条数)
     *
     * @param runSQL
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String[] pilotRunSQL(String runSQL) {
        String[] result = new String[2];
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        Connection conn = null;

        try {
            conn = dataSource.getConnection(); //获取数据库连接
            String countSQL = "SELECT COUNT(1) FROM (" + runSQL + ") V"; //查询条数
            runSQL = runSQL + " LIMIT 0,10"; //先查试运行sql的千10条数据
            pst = conn.prepareStatement(runSQL); //先试运行sql看看是不是报错
            resultSet = pst.executeQuery();
            if (resultSet.next()) { //如果sql试运行不抱错则查数据条数
                pst = conn.prepareStatement(countSQL);
                resultSet = pst.executeQuery();
            }
            result[0] = "true";
            result[1] = resultSet.next() ? String.valueOf(resultSet.getInt(1)) : "0";
        } catch (Exception e) {
            result[0] = "false";
            result[1] = e.getMessage();
            e.printStackTrace();
        } finally {
            //关闭数据库连接资源
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (pst != null) {
                    pst.close();
                    pst = null;
                }
                ;
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectDataBySQL(String runSQL) {
        return indicatorsMineMapper.selectDataBySQL(runSQL);
    }

    /**
     * 获取最大序列号
     *
     * @param params
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Map<String, String> getMaxSeq(PageData params) {
        return indicatorsMineMapper.getMaxSeq(params);
    }

    /**
     * 获取指标信息
     *
     * @param params
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map<String, String>> getIndexInfo(PageData params) {
        return indicatorsMineMapper.getIndexInfo(params);
    }

    /**
     * 指标对应数据表新增我的指标信息
     *
     * @param params
     * @return
     */
    @Override
    public void addMineNewRelation(PageData params) {
        indicatorsMineMapper.addMineNewRelation(params);
    }

    /**
     * 提交个人指标到公共指标
     *
     * @param params
     * @return
     */
    @Override
    public void submitIndexData(PageData params) {
        indicatorsMineMapper.submitIndexData(params);
    }

    /**
     * 新增指标公式表信息
     *
     * @param params
     * @return
     */
    @Override
    public void addFormula(PageData params) {
        indicatorsMineMapper.addFormula(params);
    }

    @Override
    public void updateFormula(PageData params) {
        indicatorsMineMapper.updateFormula(params);
    }

    @Override
    public boolean deleteFormula(PageData params) {
        return indicatorsMineMapper.deleteFormula(params);
    }

    /**
     * 新建指标表
     *
     * @param params
     * @return
     */
    @Override
    public void createIndexTable(PageData params) {
        indicatorsMineMapper.createIndexTable(params);
    }

    /**
     * 删除指标表
     *
     * @param params
     * @return
     */
    @Override
    public void dropIndexTable(PageData params) {
        indicatorsMineMapper.dropIndexTable(params);
    }

    /**
     * 手动调用加工逻辑
     *
     * @param params
     * @return
     */
    public void callExeFormulaHand(PageData params) {
        indicatorsMineMapper.callExeFormulaHand(params);
    }

    /**
     * 新增指标和个人关系表信息
     *
     * @param params
     * @return
     */
    public void addIndexUser(PageData params) {
        indicatorsMineMapper.addIndexUser(params);
    }

    @Override
    public void updateIndexUser(PageData params) {
        indicatorsMineMapper.updateIndexUser(params);
    }

    @Override
    public boolean deleteIndexUser(PageData params) {
        return indicatorsMineMapper.deleteIndexUser(params);
    }

    /**
     * 指标对应数据表修改指标信息
     *
     * @param params
     */
    @Override
    public void updateMineRelation(PageData params) {
        indicatorsMineMapper.updateMineRelation(params);
    }

    /**
     * 指标对应数据表删除指标信息
     *
     * @param params
     */
    @Override
    public boolean deteleMineRelation(PageData params) {
        return indicatorsMineMapper.deteleMineRelation(params);
    }

    @Override
    @Async("asyncExecutor")
    public void indexHistoryRunBatch(PageData params) {
        params.put("RUN_BATCH_STATUS", "0");
        System.out.println("--------------  indexHistoryRunBatch  start  " + System.currentTimeMillis() + "-------------------- ");
//        log.info("--------------  indexHistoryRunBatch  start  " + System.currentTimeMillis() + "-------------------- ");
        indicatorsMineMapper.indexHistoryRunBatch(params);
        System.out.println("--------------  indexHistoryRunBatch  end    " + System.currentTimeMillis() + "-------------------- ");
//        log.info("--------------  indexHistoryRunBatch  end    " + System.currentTimeMillis() + "-------------------- ");
        if (StringUtils.isBlank(params.getString("returnVal"))) { //如果返回值为空，则将跑批状态修改为未开始
            this.updateHistoryState(params);
        } else {
            String[] resultArray = params.getString("returnVal").split(",");
            if (!"success".equals(resultArray[0])) { //存储过程执行未成功或出错
                this.updateHistoryState(params);
            }
        }
    }

    @Override
    public void updateHistoryState(PageData params) {
        indicatorsMineMapper.updateHistoryState(params);
    }


} ///:~
