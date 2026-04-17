package org.jeecg.modules.dimnsnSetting.util;


import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeFilterHeaper {


    /**
     * 封装数据，将list结果集封装为树形结果集
     *
     * @param data 返回值必须包含 id主键，name描述，pId父级ID
     * @returns {*}
     */
    public static List<TreeNodeNews> definedTreeFilter(List<Map<String, Object>> data) {

        String idFiled = "id",
                textFiled = "name",
                parentField = "pId";

        List<TreeNodeNews> treeData = new ArrayList<>();
        Map<String, TreeNodeNews>  tmpMap = new HashMap<>();

        for (int i = 0, l = data.size(); i < l; i++) {
            TreeNodeNews tempNode = new TreeNodeNews();
            // 主键
            tempNode.setId(String.valueOf(data.get(i).get(idFiled)));
            tempNode.setKey(String.valueOf(data.get(i).get(idFiled)));
            tempNode.setValue(String.valueOf(data.get(i).get(idFiled)));

            //描述
            tempNode.setTitle(String.valueOf(data.get(i).get(textFiled)));
            tempNode.setLabel(String.valueOf(data.get(i).get(textFiled)));
            tempNode.setParentId(String.valueOf(data.get(i).get(parentField)));
            tmpMap.put(String.valueOf(data.get(i).get(idFiled)), tempNode);
        }

        for (int i = 0, l = data.size(); i < l; i++) {
            if ( !StringUtils.isEmpty(tmpMap.get(String.valueOf(data.get(i).get(parentField))))
                    &&
                    !( String.valueOf(data.get(i).get(idFiled))).equals( String.valueOf(data.get(i).get(parentField)))
            ) {
                if (CollectionUtils.isEmpty(tmpMap.get(String.valueOf(data.get(i).get(parentField))).getChildren()))
                    tmpMap.get(String.valueOf(data.get(i).get(parentField))).setChildren( new ArrayList() );
                tmpMap.get(String.valueOf(data.get(i).get(parentField))).addChild(tmpMap.get(String.valueOf(data.get(i).get(idFiled))));
            } else {
                treeData.add(tmpMap.get(String.valueOf(data.get(i).get(idFiled))));
            }
        }
        return treeData;
    }

}
