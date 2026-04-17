// TransUtil.java

package org.jeecg.modules.enumSetting.util;

import org.jeecg.modules.enumSetting.model.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/9/3.
 */
public class TransUtil {


    public static TreeNode treeNode = new TreeNode();

    public static void getRootNode(List<Map<String, Object>> dataList) {
        treeNode = new TreeNode();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> cNode = dataList.get(i);
            if (("2400000000").equals(cNode.get("id"))) {
                treeNode.setValue((String)(cNode.get("id")));
                treeNode.setKey((String)(cNode.get("id")));
                treeNode.setLabel((String)(cNode.get("name")));
                treeNode.setTitle((String)(cNode.get("name")));
                dataList.remove(i);
                return;
            }

        }
    }


    public static TreeNode treeTrans(List<Map<String, Object>> dataList, TreeNode tempNode) {
        for (int i = 0; i < dataList.size(); i++) {
            if( tempNode == null ){
                tempNode = treeNode;
            }
            Map<String, Object> cNode = dataList.get(i);
            if ((tempNode.getValue()).equals(cNode.get("pid"))) {
                TreeNode childNode = new TreeNode();
                childNode.setValue((String)cNode.get("id"));
                childNode.setKey((String)cNode.get("id"));
                childNode.setLabel((String)cNode.get("name"));
                childNode.setTitle((String)cNode.get("name"));
                tempNode.addChild(childNode);
                treeTrans(dataList, childNode);
            }
        }
        return treeNode;
    }


}
