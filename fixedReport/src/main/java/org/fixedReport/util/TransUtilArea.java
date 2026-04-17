// TransUtil.java

package org.fixedReport.util;


import org.fixedReport.model.TreeNodeArea;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/9/3.
 */
public class TransUtilArea {



    public static TreeNodeArea treeNodeArea = new TreeNodeArea();

    public static void getRootNode(List<Map<String, Object>> dataList) {
        treeNodeArea = new TreeNodeArea();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> cNode = dataList.get(i);
            String aa="500000";
            if (cNode.get("id").toString().equals(aa)){
                treeNodeArea.setValue(cNode.get("id").toString());
                treeNodeArea.setKey(cNode.get("id").toString());
                treeNodeArea.setLabel(cNode.get("name").toString());
                treeNodeArea.setTitle(cNode.get("name").toString());
                dataList.remove(i);
                return;
            }

        }
    }


    public static TreeNodeArea treeTrans(List<Map<String, Object>> dataList, TreeNodeArea tempNode) {
        for (int i = 0; i < dataList.size(); i++) {
            if( tempNode == null ){
                tempNode = treeNodeArea;
            }
            Map<String, Object> cNode = dataList.get(i);
            String vv =tempNode.getValue();
            if (cNode.get("pid").toString().equals(vv)){
                TreeNodeArea childNode = new TreeNodeArea();
                childNode.setValue(cNode.get("id").toString());
                childNode.setKey(cNode.get("id").toString());
                childNode.setLabel(cNode.get("name").toString());
                childNode.setTitle(cNode.get("name").toString());
                tempNode.addChild(childNode);
                treeTrans(dataList, childNode);
            }
        }
        return treeNodeArea;
    }


} ///:~
