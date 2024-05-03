import axios from 'axios';
import { getToken } from '@/composables/cookie';
import {showMessage} from '@/composables/util';
import {removeToken} from '@/composables/cookie';

const log_service = axios.create({
    baseURL: 'http://192.168.2.10:8088',
    timeout: 3000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
})

log_service.interceptors.request.use(config => {
    const token = getToken()
    console.log('统一添加请求头中的Token: ' + token)

    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
},error => {
    return Promise.reject(error)
});


log_service.interceptors.response.use(function (response) {
    return response.data
},function (error) {
    console.log(error)

    if (error.response.status === 401) {
        // 删除 cookie 中的令牌
        removeToken()
        // 刷新页面
        location.reload()
    }

    let errorMsg =  error.response.data.message ? error.response.data.message : '请求错误'
    console.log(errorMsg)
    showMessage(errorMsg,'error')

    return Promise.reject(error)
})



export default log_service