import axios from 'axios';
import { getToken } from '@/composables/cookie';
import {showMessage} from '@/composables/util';

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
    let errorMsg =  error.message === 'Network Error' ? '网络连接出错' : '请求失败'
    showMessage(errorMsg,'error')

    return Promise.reject(error)
})



export default log_service