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
    public void preservesWhetherAnIndicatorTreeNodeIsDisabled() {
        Map<String, Object> category = treeRow("category", "", "分类", true);
        Map<String, Object> indicator = treeRow("indicator", "category", "指标", false);

        List<TreeNode> tree = TreeFilterHeaper.definedTreeFilter(Arrays.asList(category, indicator));

        assertTrue(tree.get(0).isDisabled());
        assertFalse(tree.get(0).getChildren().get(0).isDisabled());
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
