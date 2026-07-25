package org.indicatorsLib.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TreeFilterHeaperTest {

    @Test
    public void categoryWithSelectableChildrenRemainsCheckable() {
        Map<String, Object> category = treeRow("category", "", "分类", true);
        Map<String, Object> indicator = treeRow("indicator", "category", "指标", false);

        List<TreeNode> tree = TreeFilterHeaper.definedTreeFilter(Arrays.asList(category, indicator));

        assertFalse(tree.get(0).isDisabled());
        assertFalse(tree.get(0).getChildren().get(0).isDisabled());
    }

    @Test
    public void leafWithoutExecutableMetadataRemainsDisabled() {
        Map<String, Object> invalidIndicator = treeRow("indicator", "", "无效指标", true);

        List<TreeNode> tree = TreeFilterHeaper.definedTreeFilter(Arrays.asList(invalidIndicator));

        assertTrue(tree.get(0).isDisabled());
    }

    @Test
    public void nestedCategoriesLeadingToSelectableIndicatorRemainCheckable() {
        Map<String, Object> root = treeRow("root", "", "根分类", true);
        Map<String, Object> category = treeRow("category", "root", "子分类", true);
        Map<String, Object> indicator = treeRow("indicator", "category", "指标", false);

        List<TreeNode> tree = TreeFilterHeaper.definedTreeFilter(Arrays.asList(root, category, indicator));

        assertFalse(tree.get(0).isDisabled());
        assertFalse(tree.get(0).getChildren().get(0).isDisabled());
    }

    @Test
    public void categoryWithOnlyInvalidLeavesRemainsDisabled() {
        Map<String, Object> category = treeRow("category", "", "分类", true);
        Map<String, Object> invalidIndicator = treeRow("indicator", "category", "无效指标", true);

        List<TreeNode> tree = TreeFilterHeaper.definedTreeFilter(Arrays.asList(category, invalidIndicator));

        assertTrue(tree.get(0).isDisabled());
    }

    private Map<String, Object> treeRow(String id, String parentId, String text, boolean disabled) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", id);
        row.put("pId", parentId);
        row.put("text", text);
        row.put("disabled", disabled);
        return row;
    }
}
