package org.inspect.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.inspect.dao.mapper.inspect.InspectionNationalDebtMapper;
import org.inspect.service.InspectionNationalDebtService;
import org.inspect.util.PageData;
import org.inspect.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InspectionNationalDebtServiceImpl implements InspectionNationalDebtService {

    @Autowired
    private InspectionNationalDebtMapper nationalDebtMapper;

    @Override
    public List<Map<String, Object>> getInspectionNationalDebtTreeList(PageData pageData) {
        if (pageData.get("GUOKU_ID") != null && !"3".equals(pageData.get("LEVEL"))) { //检查国库搜索查询、当前查询国库级别不为3
            String querKeys = nationalDebtMapper.getqQeryConditon(pageData);
            if (StringUtils.isNotBlank(querKeys)) {
                pageData.put("queryByKey", querKeys.split(","));
            } else {
                pageData.put("queryByKey", new String[]{""});
            }
        }
        return nationalDebtMapper.getInspectionNationalDebtTreeList(pageData); //查询国债巡查信息表记录
    }

    @Override
    public void updateNationalDebtData(Map<String, Object> map) {
        Map<String, Object> operates = this.selectNationalDebtDataList(map);
        //新增国债巡查记录
        if (operates.get("insert") != null) {
            nationalDebtMapper.insertInspectionNationalDebtData((List<Map<String, Object>>) operates.get("insert")); //国债巡查信息表新增记录
        }
        nationalDebtMapper.updateNationalDebtData(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void insertInspectionProjectRate(Map<String, Object> map, List<Map<String, Object>> list) {
        String[] array = new String[1];
        String parentsGK = ""; //上级国库
        Map<String, Object> guokuData = nationalDebtMapper.getGuoKuDataById(map);
        map.put("LEVEL", guokuData.get("LEVEL"));

        if ("2".equals(guokuData.get("LEVEL").toString())) { //最上级国库
            array[0] = map.get("CHECK_GK").toString();
        } else { //国库级别非2，则表示该国库有上级国库
            //根据该用户的所属国库查询期其所对应的所有上级国库，并保存在arry数组中
            List<Map<String, Object>> parentList1 = nationalDebtMapper.getAllParentGuoKu(map);
            for (Map<String, Object> data : parentList1) {
                if (data.get("GUOKU_ID").toString().equals(map.get("CHECK_GK").toString())) { //当前新增国库巡查
                    array[0] = data.get("GUOKU_ID").toString();
                    parentsGK = data.get("GUOKU_PID").toString();
                    break;
                }
            }
        }

        map.put("array", array);
        Map<String, Object> guokuMap = new HashMap<>();
        Map<String, Object> operates = new HashMap<>();
        List<Map<String, Object>> debtList = nationalDebtMapper.getParentNationalDebt(map);
        if (debtList.size() > 0) {
            for (Map<String, Object> data : debtList) {
                if (map.get("CHECK_GK").equals(data.get("CHECK_GK"))) { //表示该国库检查已新增
                    guokuMap.put("NATIONAL_DEBT_ID", data.get("NATIONAL_DEBT_ID"));
                    guokuMap.put("CHECK_COUNT", (Integer.valueOf(data.get("CHECK_COUNT").toString()) + 1)); //检查次数+1
                    guokuMap.put("MODIFY_DATE", map.get("ADD_DATE"));
                    guokuMap.put("MODIFY_USERID", map.get("ADD_USERID"));
                    operates.put("update", guokuMap);
                    break;
                }
            }
        } else { //数据库从未新增过记录
            guokuMap.put("CHECK_GK", array[0]);
            guokuMap.put("PARENT_GK", parentsGK);
            guokuMap.put("CHECK_DATE", map.get("CHECK_DATE"));
            guokuMap.put("CHECK_COUNT", "1");
            guokuMap.put("NATIONAL_DEBT_ID", map.get("NATIONAL_DEBT_ID"));
            guokuMap.put("SUMMARY_STATE", "0");
            guokuMap.put("ADD_USERID", map.get("ADD_USERID"));
            guokuMap.put("ADD_DATE", map.get("ADD_DATE"));
            debtList.clear();
            debtList.add(guokuMap);
            operates.put("insert", debtList);
        }

        //新增国债巡查记录
        if (operates.get("insert") != null) {
            nationalDebtMapper.insertInspectionNationalDebtData((List<Map<String, Object>>) operates.get("insert")); //国债巡查信息表新增记录
        }
        //修改国债巡查检测次数
        if (operates.get("update") != null) {
            nationalDebtMapper.updateNationalDebtData((Map<String, Object>) operates.get("update")); //更新国债巡查信息的检查次数
        }
        //项目评分表新增记录
        nationalDebtMapper.insertInspectionProjectRate(list); //项目评分表新增记录
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateInspectionProjectRate(Map<String, Object> map, List<Map<String, Object>> list) {
        nationalDebtMapper.deleteInspectionProjectRate(map); //项目评分表删除记录
        nationalDebtMapper.insertInspectionProjectRate(list); //项目评分表新增记录
    }

    @Override
    public List<Map<String, Object>> selectInspectionProjectRate(PageData pageData) {
        return nationalDebtMapper.selectInspectionProjectRate(pageData); //查询项目评分表记录
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void insertInspectionProjectSummary(Map<String, Object> map, List<Map<String, Object>> list) {
        nationalDebtMapper.insertInspectionProjectSummary(list); //项目评分汇总表新增记录
        nationalDebtMapper.updateNationalDebtData(map); //修改检查国库汇总的状态
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateInspectionProjectSummary(PageData pageData, List<Map<String, Object>> list) {
        nationalDebtMapper.deleteInspectionProjectSummary(pageData); //项目评分汇总表删除记录
        nationalDebtMapper.insertInspectionProjectSummary(list); //项目评分汇总表新增记录
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void revokeNationalSummary(PageData pageData) {
        nationalDebtMapper.deleteInspectionProjectSummary(pageData); //项目评分汇总表删除记录
        nationalDebtMapper.updateNationalDebtData(pageData); //修改检查国库的汇总状态
    }

    @Override
    public List<Map<String, Object>> selectInspectionProjectSummary(PageData pageData) {
        return nationalDebtMapper.selectInspectionProjectSummary(pageData); //查询项目评分汇总表记录
    }

    @Override
    public int selectProjectSummaryCount(Map<String, Object> map) {
        return nationalDebtMapper.selectProjectSummaryCount(map);
    }

    @Override
    public List<Map<String, Object>> selectProjectSummary(PageData pageData, List<String> sumList) {
        StringBuilder builder = new StringBuilder();
        String sumItem = StringUtils.join(sumList, ","); //考评加总项
        //组装查询主SQL
        builder.append("SELECT NATIONAL_DEBT_ID,ITEM_ID,BANK_CODE,SUM(SCORE) AS SCORE FROM (");

        //先组装省、市级国库自身的汇总数据SQL
        builder.append(" SELECT NATIONAL_DEBT_ID,ITEM_ID,BANK_CODE,CASE WHEN SCORE = '√' THEN '0' ELSE SCORE END AS SCORE");
        builder.append(" FROM inspection.inspection_project_rate ");
        builder.append(" WHERE NATIONAL_DEBT_ID = '" + pageData.get("NATIONAL_DEBT_ID") + "' AND ITEM_ID NOT IN('" + sumItem.replaceAll(",", "','") + "')");

        if (!"4".equals(pageData.getString("LEVEL"))) {//省、市级国库
            int index = 0;
            List<Map<String, Object>> childrenList = nationalDebtMapper.selectNationalDebtChildren(pageData);
            String[] childrenKeys = new String[childrenList.size()];
            for (Map<String, Object> children : childrenList) {
                childrenKeys[index] = children.get("NATIONAL_DEBT_ID").toString();
                index += 1;
            }

            //组装省、市下级国库汇总数据SQL
            for (String childrenKey : childrenKeys) {
                builder.append(" UNION ALL ");
                builder.append(" SELECT NATIONAL_DEBT_ID,ITEM_ID,BANK_CODE,CASE WHEN SCORE = '√' THEN '0' ELSE SCORE END AS SCORE ");
                builder.append(" FROM inspection.inspection_project_summary ");
                builder.append(" WHERE NATIONAL_DEBT_ID = '" + childrenKey + "'  AND ITEM_ID NOT IN('" + sumItem.replaceAll(",", "','") + "')");
            }
        }

        //组装查询尾部SQL
        builder.append(") V GROUP BY BANK_CODE,ITEM_ID");
        List<Map<String, Object>> list = nationalDebtMapper.getProjectSummaryList(builder.toString());

        return list;
    }

    @Override
    public List<Map<String, Object>> selectClearZeroProjects(PageData pageData, String[] array, List<String> list) {
        return nationalDebtMapper.selectClearZeroProjects(pageData, array, list);
    }

    @Override
    public Map<String, Object> getClearZeroProjectItems() {
        Map<String, Object> map = new HashMap<>();
        List<String> clearList = new ArrayList<>(); //考评清零项
        List<String> sumList = new ArrayList<>(); //考评加总项
        List<Map<String, Object>> dataList = nationalDebtMapper.getClearZeroProjectItems();
        for (Map<String, Object> data : dataList) {
            if ("1".equals(data.get("SCORE_CLASSIFY").toString())) { //考评清零项
                clearList.add(data.get("ITEM_ID").toString());
            } else { //考评加总项
                sumList.add(data.get("ITEM_ID").toString());
            }
        }
        map.put("clear", clearList);
        map.put("sum", sumList);
        return map;
    }

    @Override
    public String[] getNationalDebtId(Map<String, Object> map) {
        String[] nationalDebtArray = new String[2];
        nationalDebtArray[0] = UuidUtil.get32UUID(); //巡查表主键
        nationalDebtArray[1] = "0"; //巡查检查次数
        map.put("array", map.get("CHECK_GK").toString().split(","));
        List<Map<String, Object>> debtList = nationalDebtMapper.getParentNationalDebt(map);
        if (debtList != null && debtList.size() > 0) {
            nationalDebtArray[0] = debtList.get(0).get("NATIONAL_DEBT_ID").toString();
            nationalDebtArray[1] = debtList.get(0).get("CHECK_COUNT").toString();
        }
        return nationalDebtArray;
    }

    @Override
    public Map<String, Object> getCheckDataList(PageData pageData) {
        Map<String, Object> map = new HashMap<>();
        String[] array = this.getCheckDataSQL(pageData);
        map.put("count", array[0]);
        map.put("rows", nationalDebtMapper.getCheckDataList(array[1]));
        return map;
    }

    @Override
    public Map<String, Object> getExprotData(PageData pageData) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> exportMap = new HashMap<>();
        builder.append("SELECT ITEM_ID,BANK_CODE,SCORE FROM ");
        if ("0".equals(pageData.getString("CHECK_COUNT"))) { //查询汇总数据
            builder.append(" inspection.inspection_project_summary ");
        } else { //查询检查数据
            builder.append(" inspection.inspection_project_rate ");
        }
        builder.append(" WHERE NATIONAL_DEBT_ID='" + pageData.getString("NATIONAL_DEBT_ID") + "' ");
        //非0表示需要查询每次检查
        if (!"0".equals(pageData.getString("CHECK_COUNT"))) {
            builder.append(" AND CHECK_COUNT='" + pageData.getString("CHECK_COUNT") + "'");
        }

        //获取导出数据
        List<Map<String, Object>> exprotData = nationalDebtMapper.getExprotData(builder.toString());
        for (Map<String, Object> data : exprotData) {
            exportMap.put(data.get("ITEM_ID") + "_" + data.get("BANK_CODE"), data.get("SCORE"));
        }

        return exportMap;
    }

    /**
     * 整理国债巡查的新增记录
     *
     * @param map
     * @return
     */
    private Map<String, Object> selectNationalDebtDataList(Map<String, Object> map) {
        String[] array = null; //国库Id
        String[] parentsArray = null; //国库父Id
        Map<String, Object> operates = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> guokuData = nationalDebtMapper.getGuoKuDataByKey(map);
        map.put("LEVEL", guokuData.get("LEVEL"));
        map.put("CHECK_GK", guokuData.get("GUOKU_ID"));
        map.put("PARENT_GK", guokuData.get("GUOKU_PID"));

        if ("2".equals(guokuData.get("LEVEL").toString())) { //最上级国库
            array = new String[1];
            parentsArray = new String[1];
            array[0] = guokuData.get("GUOKU_ID").toString();
            parentsArray[0] = "";
        } else { //国库级别非2，则表示该国库有上级国库
            List<Map<String, Object>> parentList1 = nationalDebtMapper.getAllParentGuoKu(map);
            array = new String[parentList1.size()];
            parentsArray = new String[parentList1.size()];
            for (int i = 0; i < parentList1.size(); i++) {
                array[i] = parentList1.get(i).get("GUOKU_ID").toString();
                parentsArray[i] = parentList1.get(i).get("GUOKU_PID").toString();
            }
        }

        //查询当前国库的上级国库在国债巡查表中的信息
        map.put("array", array);
        List<Map<String, Object>> debtList = nationalDebtMapper.getParentNationalDebt(map);
        if (debtList.size() > 0) {
            boolean existsFlag = false;
            for (int i = 0; i < array.length; i++) {
                for (Map<String, Object> debs : debtList) {
                    if (debs.get("CHECK_GK").equals(array[i])) { //当前国库没有上级国库信息，需要新增上级国库信息
                        existsFlag = true;
                        break;
                    }
                    if (!existsFlag) { //不存在则需要新增
                        Map<String, Object> guokuMap = new HashMap<>();
                        guokuMap.put("CHECK_COUNT", "0");
                        guokuMap.put("NATIONAL_DEBT_ID", UuidUtil.get32UUID());
                        guokuMap.put("CHECK_GK", array[i]);
                        guokuMap.put("PARENT_GK", parentsArray[i]);
                        guokuMap.put("CHECK_DATE", map.get("CHECK_DATE"));
                        guokuMap.put("SUMMARY_STATE", "0");
                        guokuMap.put("ADD_USERID", map.get("MODIFY_USERID"));
                        guokuMap.put("ADD_DATE", map.get("MODIFY_DATE"));
                        list.add(guokuMap);
                        //需要新增的国债巡查记录
                        if (list.size() > 0) operates.put("insert", list);
                        break;
                    }
                }
            }
        }

        return operates;
    }

    /**
     * 组装考评评分明细查询SQL
     *
     * @param pageData
     * @return
     */
    private String[] getCheckDataSQL(PageData pageData) {
        String[] array = new String[2]; //存放最大检查次数、组装明细SQL
        StringBuilder builder = new StringBuilder();
        int checkCount = 0; //总共的检查次数
        if ("4".equals(pageData.getString("LEVEL"))) { //区、县级国库检查次数用自身的检查次数
            checkCount = Integer.parseInt(pageData.getString("sumCount"));
        } else { //省、市级国库检查次数用自身及下级国库的检查次数最大值
            pageData.put("GUOKU_ID", pageData.get("CHECK_GK"));
            Map<String, Object> map = nationalDebtMapper.getMaxCheckCount(pageData);
            pageData.put("CHECK_GK", map.get("CHECK_GK"));
            checkCount = Integer.parseInt(map.get("CHECK_COUNT").toString());
        }
        builder.append("SELECT ");
        //组装列SQL
        for (int i = 1; i <= checkCount; i++) {
            builder.append("IFNULL(CONVERT(SUM(CASE WHEN CHECK_COUNT = '" + i + "' THEN SCORE END),CHAR),'') AS '第" + i + "次检查',");
        }
        builder.append("GUOKU_ID,GUOKU_PID,GUOKU_DSCR,BANK_CODE FROM (");

        //组装查询具体数据SQL
        for (int i = 1; i <= checkCount; i++) {
            if (i > 1) builder.append(" UNION ALL "); //除了第一次遍历外都加 UNION ALL
            builder.append(" SELECT d.GUOKU_ID,d.GUOKU_PID,d.GUOKU_DSCR,BANK_CODE,SCORE,'" + i + "'  AS CHECK_COUNT ");
            builder.append(" FROM inspection.inspection_project_rate r ");
            builder.append(" LEFT JOIN dmcode.cm_guoku_dimnsn d ON R.CHECK_GK=D.GUOKU_ID ");
            builder.append(" WHERE r.CHECK_GK IN('" + pageData.getString("CHECK_GK").replaceAll(",", "','") + "') ");
            builder.append(" AND r.CHECK_DATE='" + pageData.get("CHECK_DATE") + "' AND r.ITEM_ID='" + pageData.get("ITEM_ID") + "' ");
            builder.append(" AND r.BANK_CODE='" + pageData.get("BANK_CODE") + "' AND r.CHECK_COUNT='" + i + "'");
        }

        builder.append(") V GROUP BY V.GUOKU_ID ORDER BY V.GUOKU_PID,V.GUOKU_ID");

        array[0] = String.valueOf(checkCount);
        array[1] = builder.toString();

        return array;
    }

}

