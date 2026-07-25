package org.indicatorsLib.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.indicatorsLib.dao.mapper.indicatorsLib.IndexSchemeMapper;
import org.indicatorsLib.service.IndexSchemeService;
import org.indicatorsLib.util.PageData;
import org.indicatorsLib.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 指标方案Service
 */
@Service
public class IndexSchemeServiceImpl implements IndexSchemeService {

    @Autowired
    private IndexSchemeMapper indexSchemeMapper;

    @Override
    public void pushIndexToVS(PageData pageData) {
        String schemeSql = indexSchemeMapper.selectSchemeSQL(pageData.getString("ID"));
        validateVastbaseSchemeSql(schemeSql, true);
        indexSchemeMapper.pushIndexToVS(pageData);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveIndexScheme(Map<String, Object> scheme) {
        validateVastbaseSchemeSql(scheme.get("schemeSql"), true);
        indexSchemeMapper.saveIndexScheme(scheme);
    }

    @Override
    public List<Map<String, Object>> selectSchemeTable(PageData pageData) {
        return indexSchemeMapper.selectSchemeTable(pageData);
    }

    @Override
    public String selectSchemeSQL(String schemeId) {
        return indexSchemeMapper.selectSchemeSQL(schemeId);
    }

    @Override
    public String selectSchemeCondition(String schemeId) {
        return indexSchemeMapper.selectSchemeCondition(schemeId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteSchemeById(Map<String, Object> params) {
        indexSchemeMapper.deleteSchemeById(params);
        //如果删除的是公共指标方案，且公共方案主键为空，则也需要把个人常用的指标方案删除
        if ("0".equals(params.get("isPublicScheme")) && oConvertUtils.isEmpty(params.get("publicId"))) {
            indexSchemeMapper.deletePublicScheme(params);
        }
    }

    @Override
    public List<Map<String, Object>> selectPublicScheme(PageData pageData) {
        return indexSchemeMapper.selectPublicScheme(pageData);
    }

    @Override
    public boolean isUsedPublicScheme(PageData pageData) {
        return indexSchemeMapper.isUsedPublicScheme(pageData) > 0 ? true : false;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void insertPublicScheme(Map<String, Object> params) {
        indexSchemeMapper.insertPublicScheme(params);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deletePublicScheme(Map<String, Object> params) {
        indexSchemeMapper.deletePublicScheme(params);
    }

    @Override
    public Integer validatySchemeDescr(Map<String, Object> params) {
        return indexSchemeMapper.validatySchemeDescr(params);
    }

    @Override
    public void updateSchemeData(Map<String, Object> params) {
        validateVastbaseSchemeSql(params.get("schemeSql"), false);
        indexSchemeMapper.updateSchemeData(params);
    }

    private void validateVastbaseSchemeSql(Object schemeSqlValue, boolean required) {
        String schemeSql = schemeSqlValue == null ? null : schemeSqlValue.toString();
        if (StringUtils.isBlank(schemeSql)) {
            if (required) {
                throw new IllegalArgumentException("指标方案SQL不能为空");
            }
            return;
        }

        String uppercaseSql = schemeSql.toUpperCase(Locale.ROOT);
        String compactSql = uppercaseSql.replaceAll("\\s+", "");
        if (schemeSql.indexOf('`') >= 0
                || compactSql.contains("IFNULL(")
                || compactSql.contains("SUM(IF(")
                || compactSql.contains("AA.COLID,")) {
            throw new IllegalArgumentException("指标方案SQL包含MySQL语法，禁止保存或推送");
        }

        for (String alias : new String[]{"ACCOUNT_DATE", "ACCOUNT_PERIOD", "CODE", "GK"}) {
            if (!schemeSql.contains("AS \"" + alias + "\"")) {
                throw new IllegalArgumentException("指标方案SQL缺少双引号大写别名: " + alias);
            }
        }
    }

    /**
     * 查询保存方案的SCHEME_COLUMS
     *
     * @param schemeId
     * @return
     */
    private String[] getColumnArray(String schemeId) {
        String schemeColumn = indexSchemeMapper.selectSchemeColumn(schemeId);
        return StringUtils.isNotBlank(schemeColumn) ? schemeColumn.split(",") : null;
    }

    @Override
    public List<Map<String, Object>> selectSchemeThead(String schemeId) {
        String[] arry = getColumnArray(schemeId);
        if (arry != null) {
            return indexSchemeMapper.selectSchemeThead(arry);
        }
        return null;
    }

    @Override
    public int getSchemeCount(PageData pageData) {
        return indexSchemeMapper.getSchemeCount(pageData);
    }

    @Override
    public int getUsedPublicSchemeCount(PageData pageData) {
        return indexSchemeMapper.getUsedPublicSchemeCount(pageData);
    }

}
