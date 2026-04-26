package org.jeecg.modules.dimnsnSetting.service.impl;

import org.jeecg.modules.dimnsnSetting.mapper.GuokuMapper;
import org.jeecg.modules.dimnsnSetting.model.GuoKuTreeNode;
import org.jeecg.modules.dimnsnSetting.model.TreeNode;
import org.jeecg.modules.dimnsnSetting.service.GuokuService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Map;

@Service("guokuService")
public class GuokuServiceImpl implements GuokuService {

    @Autowired
    private GuokuMapper guokuMapper;

    @Override
    public List<GuoKuTreeNode> getGuoKuTreeList(PageData pd) {
        return guokuMapper.getGuoKuTreeList(pd);
    }



    @Override
    public List<TreeNode> getBookOrgList(PageData pd) {
        return guokuMapper.getBookOrgList(pd);
    }

    @Override
    public List<TreeNode> getBookOrgIsN(PageData pd) {
        return guokuMapper.getBookOrgIsN(pd);
    }

    @Override
    public List<Map<String, Object>> getOrgTree(PageData pd) {
        return guokuMapper.getOrgTree(pd);
    }

    @Override
    public List<Map<String, Object>> getGuokuTree(PageData pd) {
        return guokuMapper.getGuokuTree(pd);
    }


    @Override
    public List<Map<String, Object>> getAreaTree(PageData pd) {
        return guokuMapper.getAreaTree(pd);
    }


    @Override
    public List<Map<String, Object>> checkCode(PageData pd) {
        return guokuMapper.checkCode(pd);
    }

    @Override
    public void addBookOrg(PageData pd) {
        guokuMapper.addBookOrg(pd);
    }

    @Override
    public void updateAddBookOrg(PageData pd) {
        guokuMapper.updateAddBookOrg(pd);
    }

    @Override
    public void editBookOrg(PageData pd) {
        guokuMapper.editBookOrg(pd);
    }

    @Override
    public void delBookOrg(PageData pd) {
        guokuMapper.delBookOrg(pd);
    }

    @Override
    public int isDelBookOrg(PageData pd) {
        return guokuMapper.isDelBookOrg(pd);
    }

    @Override
    public void delBookOrgParent(PageData pd) {
        guokuMapper.delBookOrgParent(pd);
    }

    @Override
    public List<Map<String, Object>> getSubjectT(PageData pd) {
        return guokuMapper.getSubjectT(pd);
    }


    @Override
    public List<TreeNode> getGuokuIsN(PageData pd) {
        return guokuMapper.getGuokuIsN(pd);
    }

    @Override
    public List<Map<String, Object>> checkCodegk(PageData pd) {
        return guokuMapper.checkCodegk(pd);
    }

    @Override
    public void addGuoku(PageData pd) {
        guokuMapper.addGuoku(pd);
    }

    @Override
    public void updateAddGuoku(PageData pd) {
        guokuMapper.updateAddGuoku(pd);
    }

    @Override
    public void editGuoku(PageData pd) {
        guokuMapper.editGuoku(pd);
    }

    @Override
    public void delGuoku(PageData pd) {
        guokuMapper.delGuoku(pd);
    }

    @Override
    public Integer isDelGuoku(PageData pd) {
        return guokuMapper.isDelGuoku(pd);
    }

    @Override
    public void delGuokuParent(PageData pd) {
        guokuMapper.delGuokuParent(pd);
    }

    @Override
    public List<Map<String, Object>> getKeMu(PageData pd) {
        return guokuMapper.getKeMu(pd);
    }


}
