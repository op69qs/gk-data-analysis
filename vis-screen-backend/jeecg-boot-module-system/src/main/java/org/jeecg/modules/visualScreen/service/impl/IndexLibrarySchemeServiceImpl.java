package org.jeecg.modules.visualScreen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.util.DateUtil;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.util.UuidUtil;
import org.jeecg.modules.visualScreen.mapper.IndexLibrarySchemeMapper;
import org.jeecg.modules.visualScreen.service.IndexLibrarySchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexLibrarySchemeServiceImpl implements IndexLibrarySchemeService {

    private static final Map<String, String> QUERY_PATH_MAP = new HashMap<>();

    static {
        QUERY_PATH_MAP.put("bar", "IndexBarLine/getIndexBarLineData");
        QUERY_PATH_MAP.put("line", "IndexBarLine/getIndexBarLineData");
        QUERY_PATH_MAP.put("barAndLine", "IndexBarLine/getIndexBarLineData");
        QUERY_PATH_MAP.put("pie", "IndexPie/getIndexPieData");
        QUERY_PATH_MAP.put("map", "IndexMap/getIndexMapData");
    }

    @Autowired
    private IndexLibrarySchemeMapper indexLibrarySchemeMapper;

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return indexLibrarySchemeMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return indexLibrarySchemeMapper.getCount(pd);
    }

    @Override
    public Map<String, Object> getById(PageData pd) {
        return indexLibrarySchemeMapper.getById(pd);
    }

    @Override
    public void del(PageData pd) {
        indexLibrarySchemeMapper.del(pd);
    }

    @Override
    public void toGallery(PageData pd) {
        String schemeId = pd.getString("id");
        if (StringUtils.isBlank(schemeId)) {
            throw new IllegalArgumentException("指标方案ID不能为空");
        }
        PageData query = new PageData();
        query.put("id", schemeId);
        Map<String, Object> scheme = indexLibrarySchemeMapper.getById(query);
        if (scheme == null || scheme.isEmpty()) {
            throw new IllegalArgumentException("指标方案不存在");
        }

        String type = StringUtils.defaultIfBlank(pd.getString("type"), "bar");
        String queryPath = QUERY_PATH_MAP.get(type);
        if (queryPath == null) {
            throw new IllegalArgumentException("不支持的图表类型: " + type);
        }

        String title = StringUtils.defaultIfBlank(pd.getString("title"),
                scheme.get("scheme_descr") == null ? "" : String.valueOf(scheme.get("scheme_descr")));
        String schemeConditon = scheme.get("scheme_conditon") == null ? "" : String.valueOf(scheme.get("scheme_conditon"));
        String columns = scheme.get("scheme_colums") == null ? "" : String.valueOf(scheme.get("scheme_colums"));

        JSONObject condition = new JSONObject();
        if (StringUtils.isNotBlank(schemeConditon)) {
            try {
                condition = JSON.parseObject(schemeConditon);
                if (condition == null) {
                    condition = new JSONObject();
                }
            } catch (Exception e) {
                condition = new JSONObject();
            }
        }

        String periodFlag = condition.getString("periodFlag");
        String timeType = mapTimeType(periodFlag);
        if (StringUtils.isNotBlank(condition.getString("price")) && !condition.containsKey("unit")) {
            condition.put("unit", condition.getString("price"));
        }
        if (StringUtils.isBlank(condition.getString("columns")) && StringUtils.isNotBlank(columns)) {
            condition.put("columns", columns);
        }
        condition.put("type", type);
        condition.put("title", title);
        condition.put("query_path", queryPath);
        condition.put("scheme_id", schemeId);
        condition.put("time_type", timeType);
        if (StringUtils.isNotBlank(periodFlag)) {
            try {
                condition.put("timeType", Integer.parseInt(periodFlag));
                condition.put("time_interval", Integer.parseInt(periodFlag));
            } catch (NumberFormatException ignored) {
                // keep raw periodFlag only
            }
        }

        Integer maxSort = indexLibrarySchemeMapper.getGalleryMaxSort();
        PageData gallery = new PageData();
        gallery.put("id", UuidUtil.get32UUID());
        gallery.put("option", null);
        gallery.put("query_path", queryPath);
        gallery.put("content", null);
        gallery.put("type", type);
        gallery.put("title", title);
        gallery.put("sort", maxSort == null ? 1 : maxSort + 1);
        gallery.put("state", "0");
        gallery.put("business_id", StringUtils.defaultIfBlank(pd.getString("business_id"), "1010"));
        gallery.put("time_type", timeType);
        gallery.put("dimension_type", condition.getString("dimensionFlag"));
        gallery.put("index_scheme_id", schemeId);
        gallery.put("index_scheme_name", scheme.get("scheme_descr"));
        gallery.put("condition", condition.toJSONString());
        gallery.put("add_time", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        gallery.put("add_user", pd.getString("add_user"));
        indexLibrarySchemeMapper.insertGallery(gallery);
    }

    private String mapTimeType(String periodFlag) {
        if ("1".equals(periodFlag)) {
            return "d";
        }
        if ("3".equals(periodFlag)) {
            return "q";
        }
        if ("4".equals(periodFlag)) {
            return "y";
        }
        return "m";
    }
}
