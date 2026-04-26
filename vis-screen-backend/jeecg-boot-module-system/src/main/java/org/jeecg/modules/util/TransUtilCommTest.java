// TransUtil.java

package org.jeecg.modules.util;

import org.jeecg.modules.dimnsnSetting.model.TreeNode;

/**
 * @author Created by Samer on 2019/9/3.
 */
public class TransUtilCommTest {


    public static TreeNode treeNode = new TreeNode();

//
//    public static  List<TreeNode> getRootNode(List<Map<String, Object>> dataList,int guoKuorBookOrg) {
////        0代表国库1代表核算主体
//        String code=(guoKuorBookOrg==0)?"2400000000":"240000000002";
//        List<TreeNode> treeNodeList=new ArrayList<TreeNode>();
//
//        for (int i = 0; i < dataList.size(); i++) {
//            Map<String, Object> cNode = dataList.get(i);
//            if (cNode.get("id").equals(cNode.get("pid"))) {
//                treeNode = new TreeNode();
//                treeNode.setBOOKORGCODE((String)(cNode.get("id")));
//                treeNode.setBOOKORGNAME((String)(cNode.get("name")));
//                treeNode.setBOOKORGCODEPID((String)(cNode.get("pid")));
//                treeNode.setLvltype((String)(cNode.get("lvltype")));
//                treeNode.setSTATE((String)(cNode.get("STATE")));
//                treeNode.setSTATE_desc((String)(cNode.get("STATE_desc")));
//                dataList.remove(i);
//                treeNodeList.add(TransUtilCommTest.treeTrans(dataList,treeNode));
//            }
//        }
//        return treeNodeList;
//    }
//
//
//    public static TreeNode treeTrans(List<Map<String, Object>> dataList, TreeNode tempNode) {
//        for (int i = 0; i < dataList.size(); i++) {
//
////            if( tempNode == null ){
////                tempNode = treeNode;
////            }
//            Map<String, Object> cNode = dataList.get(i);
//            if ((tempNode.getBOOKORGCODE()).equals(cNode.get("pid"))) {
//                TreeNode childNode = new TreeNode();
//                childNode.setBOOKORGCODE((String)cNode.get("id"));
//                childNode.setSTATE((String)(cNode.get("STATE")));
//                childNode.setSTATE_desc((String)(cNode.get("STATE_desc")));
//                childNode.setBOOKORGNAME((String)cNode.get("name"));
//                childNode.setBOOKORGCODEPID((String)(cNode.get("pid")));
//                childNode.setLvltype((String)(cNode.get("lvltype")));
////                String vv=(String)cNode.get("id");
//                tempNode.addChild(childNode);
//                treeTrans(dataList, childNode);
//            }
//        }
//        return treeNode;
//    }


} ///:~
