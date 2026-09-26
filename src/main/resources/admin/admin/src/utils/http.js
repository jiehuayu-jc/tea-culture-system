import axios from 'axios'
import router from '@/router/router-static'
import storage from '@/utils/storage'
import {Message} from 'element-ui'

const http = axios.create({
	timeout: 1000 * 86400,
	withCredentials: true,
	baseURL: '/springbootj8kskvkr',
	headers: {
		'Content-Type': 'application/json; charset=utf-8'
	}
})
// 请求拦截
	http.interceptors.request.use(config => {
		// 注册接口不需要Token
		if (!config.url.includes('register')) {
			config.headers['Token'] = storage.get('Token') // 请求头带上token
		}
		return config
	}, error => {
		return Promise.reject(error)
	})
// 响应拦截
http.interceptors.response.use(response => {
	if (response.data && response.data.code === 401) {
		const routeName = router.currentRoute.name
		// 已在登录/注册页时，不再弹「请先登录」（页面加载时可能有多余请求返回 401）
		if (routeName !== 'login' && routeName !== 'register') {
			Message.error('请先登录')
			setTimeout(() => {
				router.push({ name: 'login' })
			}, 1000)
		}
	}
	return response
}, error => {
	return Promise.reject(error)
})
export default http