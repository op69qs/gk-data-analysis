import Vue from 'vue'
import Router from 'vue-router'
import {constantRouterMap} from '@/config/router.config'

const routerPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(error => error)
};
Vue.use(Router);
export default new Router({
  mode: 'history',
  //mode: 'hash',//极简
  //base: process.env.BASE_URL,
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})