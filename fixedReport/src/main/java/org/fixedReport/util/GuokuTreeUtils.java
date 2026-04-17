// GuokuTreeUtils.java

package org.fixedReport.util;

import org.fixedReport.model.QueTreeNode;
import org.fixedReport.model.QueTreeNodeQue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/2/27.
 */
public class GuokuTreeUtils {
    //根据传值过滤问题分类及制度依据树形结构
    public static void QuestionBankTreeNodes(List<QueTreeNodeQue> treeList, String queQuery, String queQuery2, String queQuery3) {
//        queQuery    QUESTION_DSCR     问题描述
//        queQuery2   QUESTION_TYPE     检查分类
//        queQuery3   RULE_FILE_CONTENT 制度依据
        for (int i = 0; i < treeList.size(); i++) {
            QueTreeNodeQue  treeNode = treeList.get(i);
            String a=treeNode.getQUESTION_DSCR();
            if (treeNode.getChildren() != null && treeNode.getChildren().size() > 0) {
                            if( treeNode.getQUESTION_DSCR().indexOf(queQuery) != -1 && treeNode.getQuestion_type().indexOf(queQuery2) != -1  && treeNode.getRule_file_content().indexOf(queQuery3) != -1){
                                continue;
                            }
                QuestionBankTreeNodes(treeNode.getChildren(), queQuery,queQuery2,queQuery3);
            } else {
                Iterator<QueTreeNodeQue> iterator = treeList.iterator();
                while (iterator.hasNext()) {
                    QueTreeNodeQue tempNode_2 = iterator.next();
                    boolean a1="".equals(queQuery);
                    boolean a2= tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1;
                    boolean a3= "".equals(queQuery3);
                    boolean a4= treeNode.getRule_file_content().indexOf(queQuery3) == -1;


                    if ((("".equals(queQuery) ? true  :  tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1)
                            &&
                            ("".equals(queQuery2) ? true  :  treeNode.getQuestion_type().indexOf(queQuery2) == -1)
//                            treeNode.getQuestion_type().indexOf(queQuery2) == -1
                            &&
                            ("".equals(queQuery3)? true  :  treeNode.getRule_file_content().indexOf(queQuery3) == -1)

                              && treeNode.getTitle().equals(tempNode_2.getTitle())
                    ) ) {
                        iterator.remove();
                        i= i==.0? 0:i-1;
                    }
                }
            }
        }
        Iterator<QueTreeNodeQue> iterator = treeList.iterator();
        while (iterator.hasNext()) {
            QueTreeNodeQue tempNode_2 = iterator.next();
            if (tempNode_2.getChildren() == null || tempNode_2.getChildren().size() == 0) {

                boolean a1="".equals(queQuery);
                boolean a2= tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1;
                boolean a3= "".equals(queQuery3);
                boolean a4= tempNode_2.getRule_file_content().indexOf(queQuery3) == -1;
                if("".equals(queQuery) ){
                    if("".equals(queQuery2)){
                        if (  tempNode_2.getRule_file_content().indexOf(queQuery3) == -1
                        ) {
                            iterator.remove();
                        }
                    }else if ("".equals(queQuery3)){
                        if(tempNode_2.getQuestion_type().indexOf(queQuery2) == -1){
                            iterator.remove();
                        }
                    }else {
                        if (  tempNode_2.getRule_file_content().indexOf(queQuery3) == -1  ||  tempNode_2.getQuestion_type().indexOf(queQuery2) == -1
                        ) {
                            iterator.remove();
                        }
                    }
                }else if("".equals(queQuery2)){
                    if("".equals(queQuery) ){
                        if (  tempNode_2.getRule_file_content().indexOf(queQuery3) == -1
                        ) {
                            iterator.remove();
                        }
                    }else  if("".equals(queQuery3)){
                        if(tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1){
                            iterator.remove();
                        }
                    }else{
                        if (  tempNode_2.getRule_file_content().indexOf(queQuery3) == -1  ||  tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1
                        ) {
                            iterator.remove();
                        }

                    }

                }else if("".equals(queQuery3)){
                    if("".equals(queQuery) ){
                        if ( tempNode_2.getQuestion_type().indexOf(queQuery2) == -1
                        ) {
                            iterator.remove();
                        }
                    }else if("".equals(queQuery2))  {
                        if(tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1){
                            iterator.remove();
                        }
                    }else {
                        if (  tempNode_2.getQuestion_type().indexOf(queQuery2) == -1  ||  tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1
                        ) {
                            iterator.remove();
                        }

                    }
                }else {
                    if (tempNode_2.getQUESTION_DSCR().indexOf(queQuery) == -1
                           ||
                            tempNode_2.getQuestion_type().indexOf(queQuery2) == -1
                            ||
                            tempNode_2.getRule_file_content().indexOf(queQuery3) == -1
                    ) {
                        iterator.remove();
                    }
                }

            }
        }
    }

    //根据传值过滤问题分类及制度依据
    public static void queryTreeNodes(List<QueTreeNode> treeList, String queQuery) {
        for (int i = 0; i < treeList.size(); i++) {
            QueTreeNode  treeNode = treeList.get(i);
            if (treeNode.getChildren() != null && treeNode.getChildren().size() > 0) {
                if( treeNode.getTitle().indexOf(queQuery) != -1 || treeNode.getConcent().indexOf(queQuery) != -1){
                    continue;
                }
                queryTreeNodes(treeNode.getChildren(), queQuery);
            } else {
                Iterator<QueTreeNode> iterator = treeList.iterator();
                while (iterator.hasNext()) {
                    QueTreeNode tempNode_2 = iterator.next();
                    if ((tempNode_2.getTitle().indexOf(queQuery) == -1 && treeNode.getConcent().indexOf(queQuery) == -1)
                            && treeNode.getTitle().equals(tempNode_2.getTitle()) ) {
                        iterator.remove();
                        i= i==0? 0:i-1;
                    }
                }
            }
        }
        Iterator<QueTreeNode> iterator = treeList.iterator();
        while (iterator.hasNext()) {
            QueTreeNode tempNode_2 = iterator.next();
            if (tempNode_2.getChildren() == null || tempNode_2.getChildren().size() == 0) {
                if (tempNode_2.getTitle().indexOf(queQuery) == -1 && tempNode_2.getConcent().indexOf(queQuery) == -1) {
                    iterator.remove();
                }
            }
        }
    }

    //组装国库树
    public static void getTreeList(List<QueTreeNode> treeList, List<Map<String, Object>> metaList, QueTreeNode temp, int index) {
        for (int i = index; i < metaList.size(); i++) {
            String tempPid = metaList.get(i).get("pid").toString();
            QueTreeNode tree = new QueTreeNode();
            tree.setValue(metaList.get(i).get("id").toString());
            tree.setKey(metaList.get(i).get("id").toString());
            if (metaList.get(i).get("concent") == null) {
                tree.setConcent("");
            } else {
                tree.setConcent(metaList.get(i).get("concent").toString());
            }
            tree.setPid(metaList.get(i).get("pid").toString());
            tree.setTitle(metaList.get(i).get("name").toString());
            tree.setLabel(metaList.get(i).get("name").toString());
            tree.setIsleaf(metaList.get(i).get("isleaf").toString());
            tree.setPath(metaList.get(i).get("path").toString());
            tree.setSort(metaList.get(i).get("SORT").toString());
            tree.setType((metaList.get(i).get("TYPE") == null || metaList.get(i).get("TYPE").toString() == "") ? "" : metaList.get(i).get("TYPE").toString());
            if (temp == null) {
                boolean flage = oConvertUtils.isEmpty(tempPid);
                if (oConvertUtils.isEmpty(tempPid)) {
                    treeList.add(tree);
                    if (tree.getIsleaf().equals("1")) {
                        getTreeList(treeList, metaList, tree, i);
                    }
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getValue())) {
                temp.getChildren().add(tree);
                if (tree.getIsleaf().equals("1")) {
                    getTreeList(treeList, metaList, tree, i);
                }
            }
        }
    }

    /*树形结构裁剪*/
    public static void filterNOdes(List<QueTreeNode> treeList, String type, String flag, String exceptionWord) {
        for (int i = 0; i < treeList.size(); i++) {
            if (treeList.get(i).getChildren() != null && treeList.get(i).getChildren().size() > 0) {
                filterNOdes(treeList.get(i).getChildren(), type, flag, exceptionWord);
            } else {
                Iterator<QueTreeNode> iterator = treeList.iterator();
                while (iterator.hasNext()) {
                    QueTreeNode tempNode_2 = iterator.next();
                    if (((!type.equals(tempNode_2.getType())) || exceptionWord.equals(tempNode_2.getLabel()))
                            && flag.equals(tempNode_2.getIsleaf())) {
                        iterator.remove();
                        i--;
                    }
                }
            }
        }
        Iterator<QueTreeNode> iterator = treeList.iterator();
        while (iterator.hasNext()) {
            QueTreeNode tempNode_2 = iterator.next();
            if (tempNode_2.getChildren() == null || tempNode_2.getChildren().size() == 0) {
                if (!((type.equals(tempNode_2.getType()) && flag.equals(tempNode_2.getIsleaf())))
                        && !(exceptionWord.equals(tempNode_2.getLabel()) && flag.equals(tempNode_2.getIsleaf()))
                        && !(tempNode_2.getChildren() != null && tempNode_2.getChildren().size() > 0)) {
                    iterator.remove();
                }
            }
        }
    }

} ///:~
