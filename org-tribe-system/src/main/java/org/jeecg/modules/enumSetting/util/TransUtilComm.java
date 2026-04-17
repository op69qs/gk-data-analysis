// TransUtil.java

package org.jeecg.modules.enumSetting.util;

import org.jeecg.modules.enumSetting.model.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/9/3.
 */
public class TransUtilComm {

/*    public static List<TreeNode> treeTrans(List<Map<String, Object>> dataList) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            TreeNode pNode = new TreeNode();
            pNode.setValue((String) dataList.get(i).get("id"));
            pNode.setValue((String) dataList.get(i).get("name"));
            for (int j = 0; j < dataList.size(); j++) {
                if (((String) dataList.get(i).get("id")).equals((String) dataList.get(j).get("pid"))) {
                    TreeNode childNode = new TreeNode();
                    childNode.setValue((String) dataList.get(i).get("id"));
                    childNode.setValue((String) dataList.get(i).get("name"));
                    pNode.addChild(childNode);
                }
            }
            treeNodeList.add(pNode);
        }
        return treeNodeList;
    }

    public static TreeNode treePackage(List<TreeNode>){
        if(  ){

        }
    }*/

    public static TreeNode treeNode = new TreeNode();

    public static void getRootNode(List<Map<String, Object>> dataList,int guoKuorBookOrg) {
//        0代表国库1代表核算主体
        String code=(guoKuorBookOrg==0)?"2400000000":"240000000002";
        treeNode = new TreeNode();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> cNode = dataList.get(i);
            if (code.equals(cNode.get("id"))) {
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


} ///:~
