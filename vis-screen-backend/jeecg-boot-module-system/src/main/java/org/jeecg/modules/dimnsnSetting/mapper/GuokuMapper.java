package org.jeecg.modules.dimnsnSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.dimnsnSetting.model.GuoKuTreeNode;
import org.jeecg.modules.dimnsnSetting.model.TreeNode;
import org.jeecg.modules.util.PageData;


import java.util.List;
import java.util.Map;

public interface GuokuMapper {
    List<Map<String,Object>> getOrgTree(@Param("params") PageData pd);
    List<TreeNode> getBookOrgList(@Param("params") PageData pd);
    List<TreeNode> getBookOrgIsN(@Param("params") PageData pd);
    List<Map<String,Object>> checkCode(@Param("params") PageData pd);
    void addBookOrg(@Param("params") PageData pd);
    void updateAddBookOrg(@Param("params") PageData pd);
    void editBookOrg(@Param("params") PageData pd);
    void delBookOrg(@Param("params") PageData pd);
    int isDelBookOrg(@Param("params") PageData pd);
    void delBookOrgParent(@Param("params") PageData pd);



    //国库管理
    List<GuoKuTreeNode> getGuoKuTreeList(@Param("params") PageData pd);
    List<Map<String,Object>> getGuokuTree(@Param("params") PageData pd);
    List<Map<String,Object>> getAreaTree(@Param("params") PageData pd);
    List<TreeNode> getGuokuIsN(@Param("params") PageData pd);
    List<Map<String,Object>> checkCodegk(@Param("params") PageData pd);
    void addGuoku(@Param("params") PageData pd);
    void updateAddGuoku(@Param("params") PageData pd);
    void editGuoku(@Param("params") PageData pd);
    void delGuoku(@Param("params") PageData pd);
    Integer isDelGuoku(@Param("params") PageData pd);
    void delGuokuParent(@Param("params") PageData pd);

    List<Map<String,Object>> getKeMu(@Param("params") PageData pd);
    List<Map<String,Object>> getSubjectT(@Param("params") PageData pd);
}
