// TransUtil.java

package org.jeecg.modules.util;

import org.jeecg.modules.dimnsnSetting.model.TreeNodeComm;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/9/3.
 */
public class TransUtilComm {


    public static TreeNodeComm treeNodeComm = new TreeNodeComm();

//    public static TreeNode treeNode = new TreeNode();


    public static void getRootNode(List<Map<String, Object>> dataList, String guoKuorBookOrg) {
        String code = "";
//         1代表国库0代表核算主体2地区
        if (guoKuorBookOrg.equals("0")) {
            code = "240000000002";
        } else if (guoKuorBookOrg.equals("1")) {
            code = "2400000000";
        } else if (guoKuorBookOrg.equals("2")) {
            code = "530000";
        } else {
            code = String.valueOf(dataList.get(0).get("id"));
        }
        treeNodeComm = new TreeNodeComm();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> cNode = dataList.get(i);
            if (code.equals(cNode.get("id"))) {
                treeNodeComm.setValue((String) (cNode.get("id")));
                treeNodeComm.setKey((String) (cNode.get("id")));
                treeNodeComm.setLabel((String) (cNode.get("name")));
                treeNodeComm.setTitle((String) (cNode.get("name")));
                dataList.remove(i);
                return;
            }
        }
    }


    public static TreeNodeComm treeTrans(List<Map<String, Object>> dataList, TreeNodeComm tempNode, int index) {
        for (int i = 0; i < dataList.size(); i++) {
            if (tempNode == null) {
                tempNode = treeNodeComm;
            }
            Map<String, Object> cNode = dataList.get(i);
            if ((tempNode.getValue()).equals(cNode.get("pid"))) {
                TreeNodeComm childNode = new TreeNodeComm();
                childNode.setValue((String) cNode.get("id"));
                childNode.setKey((String) cNode.get("id"));
                childNode.setLabel((String) cNode.get("name"));
                childNode.setTitle((String) cNode.get("name"));
                tempNode.addChild(childNode);
                treeTrans(dataList, childNode, i);
            }
        }
        return treeNodeComm;
    }


} ///:~
