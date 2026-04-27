## 背景

- 登录成功后浏览器额外发起了 `GET /user/undefined/login?...` 请求。
- 该请求来自登录页 `loginSuccess()` 中的 JSONP 外部联动，而不是主登录接口。
- 当前环境未配置 `window._CONFIG['domianRunURL']`，所以运行时把目标地址拼成了 `undefined/login`。

## 实施

- 在 `org-tribe-view/src/views/user/Login.vue` 中为 `domianRunURL` 增加判空保护。
- 同步修正主系统静态资源中的相同逻辑，避免当前部署继续引用旧前端包时重复发错请求。

## 验证

- 登录成功后仍然正常跳转到首页。
- 未配置 `domianRunURL` 时，不再发起额外的 `undefined/login` JSONP 请求。

## 取舍

- 这次只保护可选的外部联动请求，不改变正常登录主流程。
- 如果后续确实需要该联动，应通过明确配置 `domianRunURL` 恢复，而不是依赖未定义配置。