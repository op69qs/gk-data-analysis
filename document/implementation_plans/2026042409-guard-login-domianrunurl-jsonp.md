## 背景

- 在主系统登录后，从旧 vis 入口 `/BigScreen/PageList` 跳转到页面管理页时，浏览器控制台出现额外的 404 请求：`/user/undefined/login?...`。
- 页面本身可正常进入，但登录成功后的附加 JSONP 同步请求会因为 `window._CONFIG['domianRunURL']` 未配置而拼出错误地址。

## 实施

- 在 `org-tribe-view/src/views/user/Login.vue` 的 `loginSuccess()` 中增加 `domianRunURL` 判空，只有配置存在时才发起 JSONP 登录同步请求。
- 同步修正主系统当前静态资源中的对应逻辑，避免现网构建产物继续发出 `undefined/login` 请求。

## 结果

- 主系统统一登录仍保持原行为。
- 当环境未配置 `domianRunURL` 时，不再额外发起错误的 JSONP 请求，也不会再出现 `/user/undefined/login` 404 噪音。