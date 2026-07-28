package org.seo.service.impl;

import org.seo.dao.mapper.DataTableMapper;
import org.seo.service.DataTableService;
import org.seo.util.IndexTreeNode;
import org.seo.util.PageData;
import org.seo.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataTableServiceImpl implements DataTableService {

    @Autowired
    private DataTableMapper dataTableMapper;

    @Override
    public List<IndexTreeNode> getDataSourceTree(PageData pageData) {
        List<IndexTreeNode> treeNodeList = new ArrayList<>();
        List<Map<String, Object>> dataList = dataTableMapper.getDataSourceTree(pageData);
        getTreeList(treeNodeList, dataList, null); //将数据源与数据库组成Tree
        return treeNodeList;
    }

    @Override
    public List<Map<String, Object>> getRelationTree(PageData pageData) {
        return dataTableMapper.getRelationTree(pageData);
    }

    @Override
    public List<Map<String, Object>> getRelationData(PageData pageData) {
        return dataTableMapper.getRelationData(pageData);
    }

    @Override
    public int getTableCount(PageData pageData) {
        return dataTableMapper.getTableCount(pageData);
    }

    @Override
    public void addDataTable(List<Map<String, Object>> list) { dataTableMapper.addDataTable(list); }

    @Transactional(rollbackFor = RuntimeException.class)
    public void editDataTable(String tableId,List<Map<String, Object>> list) {
        //编辑数据表需要先删除数据信息，再新增
        dataTableMapper.deleteDataTable(tableId);
        dataTableMapper.addDataTable(list);
    }

    /**
     * 处理Tree数据结构
     *
     * @param treeList
     * @param metaList
     * @param temp
     */
    private void getTreeList(List<IndexTreeNode> treeList, List<Map<String, Object>> metaList, IndexTreeNode temp) {
        metaList.forEach(map -> {
            String tempPid = map.get("pId").toString();
            IndexTreeNode tree = new IndexTreeNode();
            tree.setId(map.get("id").toString());
            tree.setLabel(map.get("lable").toString());
            tree.setParentId(tempPid);
            tree.setNodeType(map.get("nodeType").toString());

            if (oConvertUtils.isEmpty(tempPid)) {
                if (temp == null) {
                    treeList.add(tree);
                    getTreeList(treeList, metaList, tree);
                }
            } else {
                if (temp == null) {
                    if (oConvertUtils.isEmpty(tempPid)) {
                        treeList.add(tree);
                    }
                    getTreeList(treeList, metaList, tree);
                } else if (temp != null && tempPid.equals(temp.getId())) {
                    temp.getChildren().add(tree);
                    getTreeList(treeList, metaList, tree);
                }
            }
        });
    }

}
