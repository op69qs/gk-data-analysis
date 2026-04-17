import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
import {VueAxios} from './axios'
import {Modal, notification} from 'ant-design-vue'
import {ACCESS_TOKEN} from '@/store/mutation-types'

//自动设置后台服务 baseURL (也可以手工指定写死项目名字)
let baseDomain = window._CONFIG['domianURLRequest'];
let baseProject = baseDomain.substring(baseDomain.lastIndexOf('/'));

// 创建 axios 实例
  const service = axios.create({
  baseURL: baseProject,
  timeout: 1000 * 300 // 请求超时时间
});

const err = (error) => {
  if (error.response) {
    let data = error.response.data,
      pathname = window.document.location.pathname;
    const token = Vue.ls.get(ACCESS_TOKEN);
    console.log('------异常响应------', token);
    console.log('------异常响应------', error.response.status);
    switch (error.response.status) {
      case 403:
        notification.error({message: '系统提示', description: '拒绝访问', duration: 4});
        break;
      case 500:
        //notification.error({ message: '系统提示', description:'Token失效，请重新登录!',duration: 4})
        if (token && data.message === 'Token失效，请重新登录' && pathname.indexOf('login') < 0) {
          // update-begin- --- author:scott ------ date:20190225 ---- for:Token失效采用弹框模式，不直接跳转----
          // store.dispatch('Logout').then(() => {
          //     window.location.reload()
          // })
          /*Modal.error({
            title: '登录已过期',
            content: '很抱歉，登录已过期，请重新登录',
            okText: '重新登录',
            mask: false,
            onOk: () => {
              store.dispatch('Logout').then(() => {
                Vue.ls.remove(ACCESS_TOKEN);
                window.location.reload()
              })
            }
          });*/
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN);
            window.location.reload()
          })
          // update-end- --- author:scott ------ date:20190225 ---- for:Token失效采用弹框模式，不直接跳转----
        }
        break;
      case 404:
        notification.error({message: '系统提示', description: '很抱歉，资源未找到!', duration: 4});
        break;
      case 504:
        notification.error({message: '系统提示', description: '网络超时'});
        break;
      case 401:
        notification.error({message: '系统提示', description: '未授权，请重新登录', duration: 4});
        if (token) {
          store.dispatch('Logout').then(() => {
            setTimeout(() => {
              window.location.reload()
            }, 1500)
          })
        }
        break;
      default:
        notification.error({
          message: '系统提示',
          description: data.message,
          duration: 4
        });
        break
    }
  }
  return Promise.reject(error)
};

// request interceptor
service.interceptors.request.use(config => {
  const token = Vue.ls.get(ACCESS_TOKEN);
  if (token) {
    config.headers['X-Access-Token'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
  }
  if (config.method === 'get') {
    if (config.url.indexOf('sys/dict/getDictItems') < 0) {
      config.params = {
        _t: Date.parse(new Date()) / 1000,
        ...config.params
      }
    }
  }
  return config
}, (error) => {
  return Promise.reject(error)
});

// response interceptor
service.interceptors.response.use((response) => {
  return response.data
}, err);

const installer = {
  vm: {},
  install(Vue, router = {}) {
    Vue.use(VueAxios, router, service)
  }
};
const GetUrlParam = (paraName) => {
  var url = document.location.toString();
  var arrObj = url.split('?');

  if (arrObj.length > 1) {
    var arrPara = arrObj[1].split('&');
    var arr;

    for (var i = 0; i < arrPara.length; i++) {
      arr = arrPara[i].split('=');

      if (arr != null && arr[0] === paraName) {
        return arr[1]
      }
    }
    return ''
  } else {
    return ''
  }
};
export {
  installer as VueAxios,
  service as axios,
  GetUrlParam
}