package org.jeecg.modules.visualScreen.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysDict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VisLegacyDictScope {

	private static final List<String> ORDERED_DICT_CODES = Arrays.asList(
		"menu_type",
		"enable_status",
		"valid_status",
		"dict_item_status",
		"yn",
		"global_perms_type",
		"perms_type",
		"send_status",
		"user_status",
		"status",
		"user_type",
		"del_flag",
		"sex"
	);

	private static final Map<String, Integer> ORDER_INDEX = buildOrderIndex();

	private VisLegacyDictScope() {
	}

	public static void apply(QueryWrapper<SysDict> queryWrapper) {
		queryWrapper.in("dict_code", ORDERED_DICT_CODES);
	}

	public static List<SysDict> sort(List<SysDict> dictList) {
		List<SysDict> sortedList = new ArrayList<>(dictList);
		sortedList.sort(Comparator
			.comparingInt((SysDict item) -> ORDER_INDEX.getOrDefault(item.getDictCode(), Integer.MAX_VALUE))
			.thenComparing(SysDict::getDictCode, Comparator.nullsLast(String::compareTo)));
		return sortedList;
	}

	public static Page<SysDict> buildPage(List<SysDict> sortedList, int pageNo, int pageSize) {
		Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
		page.setTotal(sortedList.size());
		int safePageNo = Math.max(pageNo, 1);
		int safePageSize = Math.max(pageSize, 1);
		int startIndex = (safePageNo - 1) * safePageSize;
		if (startIndex >= sortedList.size()) {
			page.setRecords(Collections.emptyList());
			return page;
		}
		int endIndex = Math.min(startIndex + safePageSize, sortedList.size());
		page.setRecords(sortedList.subList(startIndex, endIndex));
		return page;
	}

	private static Map<String, Integer> buildOrderIndex() {
		Map<String, Integer> orderIndex = new HashMap<>();
		for (int index = 0; index < ORDERED_DICT_CODES.size(); index++) {
			orderIndex.put(ORDERED_DICT_CODES.get(index), index);
		}
		return orderIndex;
	}
}