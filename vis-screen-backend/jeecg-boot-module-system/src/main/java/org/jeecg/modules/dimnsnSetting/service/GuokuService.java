package org.jeecg.modules.dimnsnSetting.service;



import org.jeecg.modules.dimnsnSetting.model.GuoKuTreeNode;
import org.jeecg.modules.dimnsnSetting.model.TreeNode;
import org.jeecg.modules.util.PageData;


import java.util.List;
import java.util.Map;

public interface GuokuService {
    List<TreeNode> getBookOrgList(PageData pd);
    List<TreeNode> getBookOrgIsN(PageData pd);
    List<Map<String,Object>> getOrgTree(PageData pd);
    List<Map<String,Object>> checkCode(PageData pd);
    void addBookOrg(PageData pd);
    void updateAddBookOrg(PageData pd);
    void editBookOrg(PageData pd);
    void delBookOrg(PageData pd);
    int isDelBookOrg(PageData pd);
    void delBookOrgParent(PageData pd);
    List<Map<String,Object>> getSubjectT(PageData pd);



//国库管理
    List<GuoKuTreeNode> getGuoKuTreeList(PageData pd);
    List<Map<String,Object>> getGuokuTree(PageData pd);
    List<Map<String,Object>> getAreaTree(PageData pd);
    List<TreeNode> getGuokuIsN(PageData pd);
    List<Map<String,Object>> checkCodegk(PageData pd);
    void addGuoku(PageData pd);
    void updateAddGuoku(PageData pd);
    void editGuoku(PageData pd);
    void delGuoku(PageData pd);
    Integer isDelGuoku(PageData pd);
    void delGuokuParent(PageData pd);


    List<Map<String,Object>> getKeMu(PageData pd);
}
