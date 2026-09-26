import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router/router'
import BaiduMap from 'vue-baidu-map'
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import '@/assets/css/iconfont.css'
import config from './config/config'
import validate from './common/validate'

import {
	isAuth,
	getCurDateTime,
	getCurDate,
	isBackAuth,
	changeReturnGoods,
} from './common/system'
import App from './App.vue'
import Breadcrumb from '@/components/Breadcrumb'
import FileUpload from '@/components/FileUpload'
import Editor from "@/components/Editor";
import aplayer from 'vue-aplayer';
import store from './store'
import { encryptDes,decryptDes,encryptAes,decryptAes } from '@/common/des.js'
import VueLuckyCanvas from '@lucky-canvas/vue'

/** 与路由跳转同步的当前 path（beforeEach 里更新，早于旧页面销毁与请求回调） */
let __frontSyncRoutePath = ''
try {
	if (typeof window !== 'undefined') {
		const raw = (window.location.hash || '').replace(/^#/, '') || '/'
		const m = router.match(raw.split('?')[0])
		__frontSyncRoutePath = (m && m.path) || ''
	}
} catch (e) {
	__frontSyncRoutePath = ''
}
router.beforeEach((to, from, next) => {
	__frontSyncRoutePath = to.path || ''
	next()
})

Vue.use(VueLuckyCanvas)
Vue.config.productionTip = false;

Vue.prototype.$config = config;
Vue.prototype.$validate = validate;
Vue.prototype.isAuth = isAuth;
Vue.prototype.isBackAuth = isBackAuth;
Vue.prototype.getCurDateTime = getCurDateTime;
Vue.prototype.changeReturnGoods = changeReturnGoods;
Vue.prototype.getCurDate = getCurDate;
Vue.prototype.encryptDes = encryptDes
Vue.prototype.decryptDes = decryptDes
Vue.prototype.encryptAes = encryptAes
Vue.prototype.decryptAes = decryptAes

Vue.use(VueRouter);
Vue.use(VueResource);
Vue.use(ElementUI);
Vue.use(BaiduMap, {});
Vue.use(VueQuillEditor);

Vue.component('Breadcrumb', Breadcrumb);
Vue.component('file-upload', FileUpload);
Vue.component('editor', Editor);
Vue.component('aplayer', aplayer);

Vue.http.options.root = config.name;
Vue.http.headers.common['Token'] = localStorage.getItem('frontToken');

/** 解析 vue-resource 响应体（可能是已解析对象或 JSON 字符串） */
function getResponseData(response) {
	let data = response.body != null ? response.body : response.data;
	if (typeof data === 'string') {
		try {
			data = JSON.parse(data);
		} catch (e) {
			/* ignore */
		}
	}
	return data;
}

/** 是否处在注册/登录页：beforeEach 同步 path 优先，避免「人已在登录页但回调里还是首页」 */
function isRegisterOrLoginPage() {
	if (__frontSyncRoutePath === '/login' || __frontSyncRoutePath === '/register') {
		return true;
	}
	try {
		const path = router.currentRoute && router.currentRoute.path;
		if (path === '/login' || path === '/register') {
			return true;
		}
	} catch (e) {}
	if (typeof window === 'undefined') {
		return false;
	}
	if (window.__FRONT_AUTH_FREE_ROUTE__) {
		return true;
	}
	const { pathname, hash } = window.location;
	const h = hash || '';
	const p = pathname || '';
	if (h.indexOf('/register') !== -1 || h.indexOf('/login') !== -1) {
		return true;
	}
	if (p.endsWith('/register') || p.endsWith('/login')) {
		return true;
	}
	try {
		const full = router.currentRoute.fullPath || '';
		if (full.indexOf('/register') !== -1 || full.indexOf('/login') !== -1) {
			return true;
		}
	} catch (e2) {}
	return false;
}
function isPublicApiRequest(request) {
	let u = '';
	try {
		u = request.getUrl ? request.getUrl() : (request.url || '');
	} catch (e) {
		u = '';
	}
	u = String(u);
	// 兼容 getUrl 为相对路径（无前导 /）、或仅含 yonghu/login 片段
	const pub = /(yonghu|shangjia|users)\/(register|login)([?&]|$)/.test(u);
	return pub || u.indexOf('/file/upload') !== -1;
}
// 热更新会重复执行 main.js，避免多次 push 拦截器导致连弹多条「请先登录」
if (typeof window !== 'undefined' && !window.__FRONT_VUE_RESOURCE_AUTH_INSTALLED__) {
	window.__FRONT_VUE_RESOURCE_AUTH_INSTALLED__ = true;
	Vue.http.interceptors.push(function(request, next) {
		next((response) => {
			const data = getResponseData(response);
			const code = data && data.code;
			const isAuthError = code == 401 || code == 403;
			if (!data || !isAuthError) {
				return response;
			}

			// 地址栏兜底：避免路由状态与异步回调不同步时仍弹「请先登录」
			if (typeof window !== 'undefined') {
				const href = window.location.href || '';
				if (href.indexOf('#/login') !== -1 || href.indexOf('#/register') !== -1) {
					return response;
				}
			}
			if (isRegisterOrLoginPage() || isPublicApiRequest(request)) {
				return response;
			}

			localStorage.removeItem('frontToken');
			localStorage.removeItem('UserTableName');
			localStorage.removeItem('frontSessionTable');
			Vue.http.headers.common['Token'] = '';

			if (typeof window !== 'undefined') {
				if (!window.__FRONT_AUTH_TOAST_LOCK__) {
					window.__FRONT_AUTH_TOAST_LOCK__ = true;
					Vue.prototype.$message.error('请先登录');
					setTimeout(() => {
						router.replace('/login').catch(() => {});
						window.__FRONT_AUTH_TOAST_LOCK__ = false;
					}, 400);
				}
			}
			return response;
		});
	});
}

router.afterEach((to, from) => {
	if (typeof window !== 'undefined') {
		window.__FRONT_AUTH_FREE_ROUTE__ = to.path === '/login' || to.path === '/register';
	}
	if (from.path == '/login') {
		Vue.http.headers.common['Token'] = localStorage.getItem('frontToken');
	}
})

new Vue({
	render: h => h(App),
	router,
	store,
}).$mount('#app')