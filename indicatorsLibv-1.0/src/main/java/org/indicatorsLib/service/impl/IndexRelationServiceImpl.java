package org.indicatorsLib.service.impl;

import org.indicatorsLib.dao.mapper.indicatorsLib.IndexRelationMapper;
import org.indicatorsLib.service.IndexRelationService;
import org.indicatorsLib.util.PageData;
import org.indicatorsLib.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexRelationServiceImpl implements IndexRelationService {

    @Autowired
    private IndexRelationMapper indexRelationMapper;

    @Override
    public List<Map<String, Object>> selectIndexRelationInfo(PageData pd) {
        return indexRelationMapper.selectIndexRelationInfo(pd);
    }

    /**
     * 获取指标详细描述
     *
     * @param pd
     * @return
     */
    @Override
    public Map<String, Object> getIndexDetails(PageData pd) {
        return indexRelationMapper.getIndexDetails(pd);
    }

    @Override
    public List<Map<String, Object>> selectIndexRelationTree(PageData pageData) {
        return indexRelationMapper.selectIndexRelationTree(pageData);
    }

    @Override
    public Integer getIndicatorsCount(String schemeSql) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(1) FROM(" + schemeSql + ") C");
        return indexRelationMapper.getIndicatorsCount(builder.toString());
    }

    @Override
    public List<Map<String, Object>> getDetectionValue(String[] indexArray) {
        return indexRelationMapper.getDetectionValue(indexArray);
    }

    @Override
    public List<Map<String, Object>> getIndicatorsTable(String schemeSql) {
        return indexRelationMapper.getIndicatorsTable(schemeSql);
    }

    @Override
    public List<Map<String, Object>> getIndicatorsTableName(String[] array) {
        return indexRelationMapper.getIndicatorsTableName(array);
    }

    @Override
    public Object[] getIndicatorsECharts(List<String> list, Map<String, Object> map, String schemeSql) {
        StringBuilder builder = new StringBuilder();
        Object[] array = null;
        if ("cake".equals(map.get("eChartsFlag").toString())) { //饼图
            builder.append("SELECT  ");
            //direction：【X：横向，Y：纵向】
            if ("X".equals(map.get("direction").toString())) { //横向
                builder.append("C." + map.get("indexName").toString());
            } else {
                builder.append("C.GK,C." + map.get("indexName").toString());
            }
            builder.append(" FROM(" + schemeSql + ") C WHERE ");
            if ("X".equals(map.get("direction").toString())) { //横向
                builder.append("C.ACCOUNT_PERIOD='" + map.get("eChartsDate").toString() + "' AND C.CODE='" + map.get("GK").toString() + "'");
            } else {
                builder.append("C.ACCOUNT_PERIOD='" + map.get("eChartsDate").toString() + "'");
            }
            schemeSql = builder.toString();
        } else if ("columnar,lineChart".contains(map.get("eChartsFlag").toString())) { //柱状图、折线图
            builder.append("SELECT  C.*" + " FROM(" + schemeSql + ") C WHERE ");
            if (oConvertUtils.isNotEmpty(map.get("direction"))) { //横向坐标显示账期，维度参数必传
                builder.append("C.CODE IN('" + map.get("direction").toString().replaceAll(",", "','") + "')");
            } else if (oConvertUtils.isNotEmpty(map.get("eChartsDate"))) { //横向坐标显示维度(国库、地区、核算主体)，账期参数必传
                builder.append("C.ACCOUNT_PERIOD='" + map.get("eChartsDate").toString() + "'");
            }
            schemeSql = builder.toString();
        }
        List<Map<String, Object>> dataList = indexRelationMapper.getIndicatorsECharts(schemeSql);
        if ("cake".equals(map.get("eChartsFlag").toString())) { //饼图
            array = new Object[dataList.size()];
            for (int i = 0; i < dataList.size(); i++) {
                array[i] = dataList.get(i);
            }
        } else {
            String column = list.get(list.size() - 1); //取出最后一位的字段
            list.remove(column); //移除最后一位的字段
            array = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                for (Map<String, Object> data : dataList) {
                    if (list.get(i).equals(data.get(column).toString())) {
                        array[i] = data;
                        dataList.remove(data);
                        break;
                    }
                }
            }
        }
        return array;
    }

    @Override
    public Map<String, Object> getMinDateAndMaxDate(String accountPeriodSql) {
        return indexRelationMapper.getMinDateAndMaxDate(accountPeriodSql);
    }

    @Override
    public List<Map<String, Object>> getDimensionData(String dimensionSql) {
        return indexRelationMapper.getDimensionData(dimensionSql);
    }

    @Override
    public Object[] getColumnLineChart(List<String> list, Map<String, Object> map, String schemeSql) {
        Object[] resultArray = new Object[2];
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT  C.*" + " FROM(" + schemeSql + ") C ");
        if (oConvertUtils.isNotEmpty(map.get("direction"))) { //横向坐标显示账期，维度参数必传
            builder.append(" WHERE C.CODE='" + map.get("direction").toString() + "'");
        } else if (oConvertUtils.isNotEmpty(map.get("eChartsDate"))) { //横向坐标显示维度(国库、地区、核算主体)，账期参数必传
            builder.append(" WHERE C.ACCOUNT_PERIOD='" + map.get("eChartsDate").toString() + "'");
        }
        schemeSql = builder.toString();

        List<Map<String, Object>> dataList = indexRelationMapper.getIndicatorsECharts(schemeSql);
        String column = list.get(list.size() - 1);
        list.remove(column); //移除最后一位的字段

        if (list.size() == 0) {
            for (Map<String, Object> data : dataList) {
                if (!list.contains(data.get(column))) {
                    list.add(data.get(column).toString());
                }
            }
        }

        Object[] columnarArray = new Object[list.size()]; //柱状指标数组
        Object[] lineArray = new Object[list.size()]; //柱状指标数组
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> columnarMap = new HashMap<>();
            Map<String, Object> lineMap = new HashMap<>();
            for (Map<String, Object> data : dataList) {
                if (list.get(i).equals(data.get(column).toString())) {
                    for (String indexName : map.keySet()) {
                        if ("Columnar".equals(map.get(indexName).toString())) { //柱状图
                            columnarMap.put("ACCOUNT_PERIOD", list.get(i));
                            columnarMap.put("GK", data.get("GK").toString());
                            columnarMap.put("CODE", data.get("CODE").toString());
                            columnarMap.put(indexName, data.get(indexName));
                        } else if ("Line".equals(map.get(indexName).toString())) { //折线图
                            lineMap.put("ACCOUNT_PERIOD", list.get(i));
                            lineMap.put("GK", data.get("GK").toString());
                            lineMap.put("CODE", data.get("CODE").toString());
                            lineMap.put(indexName, data.get(indexName));
                        }
                    }

                }
            }
            columnarArray[i] = (columnarMap != null && columnarMap.size() > 0) ? columnarMap : null;
            lineArray[i] = (lineMap != null && lineMap.size() > 0) ? lineMap : null;
        }

        resultArray[0] = columnarArray;
        resultArray[1] = lineArray;
        return resultArray;
    }

    @Override
    public List<Map<String, String>> getDimensionSelectById(String[] codeArry) {
        return indexRelationMapper.getDimensionSelectById(codeArry);
    }

    /**
     * 转换账期季度
     *
     * @param date
     * @return
     */
    private String getQuarter(String date) {
        if (date.contains("Q1")) {
            date = date.replace("Q1", "年第一季度");
        } else if (date.contains("Q2")) {
            date = date.replace("Q1", "年第二季度");
        } else if (date.contains("Q3")) {
            date = date.replace("Q1", "年第三季度");
        } else if (date.contains("Q4")) {
            date = date.replace("Q1", "年第四季度");
        }
        return date;
    }
}
