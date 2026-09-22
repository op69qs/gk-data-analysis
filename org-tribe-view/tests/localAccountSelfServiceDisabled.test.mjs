import assert from 'assert'
import { readFile } from 'fs/promises'
import { createRequire } from 'module'

const require = createRequire(import.meta.url)
const VueRouter = require('vue-router')
// Replace layout imports only; execute the real route table and Vue Router matcher.
const source = (await readFile(new URL('../src/config/router.config.js', import.meta.url), 'utf8'))
  .replace(/^import .* from '@\/components\/layouts'\r?\n/m,
    'const UserLayout = {}, TabLayout = {}, RouteView = {}, BlankLayout = {}, PageView = {}\n')
const { constantRouterMap } = await import(`data:text/javascript;base64,${Buffer.from(source).toString('base64')}`)
const router = new VueRouter({ mode: 'abstract', routes: constantRouterMap })
for (const path of ['/user/register', '/user/register-result', '/user/alteration']) {
  assert.strictEqual(router.match(path).path, '/user/login', `${path} must redirect to login`)
}
assert.strictEqual(router.match('/user/login').name, 'login')
assert.strictEqual(router.match('/oauth/callback').name, 'OAuthCallback')
console.log('Local account self-service routes disabled; login and OAuth callback preserved')
