# 修复 vis 指标库方案页签行为

## 问题

用户访问 `/vis/index-library` 时页面会脱离主界面页签壳子，表现成整页切换；
同时 `schemeIndex.vue` 内部的“运行/查看方案”入口仍然跳转到旧的 `/statistics/indexLibrary`。

这两个点叠加后，用户看到的效果不是和其他菜单一致的页签内打开。

## 根因

1. `router.config.js` 中 `/vis/index-library` 被定义在 `constantRouterMap` 顶层，未挂在 `TabLayout` 下。
2. `schemeIndex.vue` 的 `handleRun` 仍然直接跳旧路径，没有统一到 `/vis/index-library`。

## 修改

1. 保持 `util.js` 对历史菜单 `/statistics/indexLibrary` -> `/vis/index-library` 的归一化，让数据库菜单继续生成根 `/` 下 `TabLayout` 子路由。
2. 移除 `constantRouterMap` 里额外新增的 `/vis/index-library` 常量路由，避免它先于动态菜单路由命中，导致行为和普通菜单不一致。
3. 将 `schemeIndex.vue` 的跳转路径改为 `/vis/index-library`。

## 结果

`/vis/index-library` 会走现有动态菜单树，在主界面内部以页签形式打开；
从方案列表进入指标库方案页时，也会走同一条新路由，不再落回旧页面，也不会再被独立常量路由抢先匹配。