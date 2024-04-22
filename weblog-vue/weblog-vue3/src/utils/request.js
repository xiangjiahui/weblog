import axios from 'axios';
import { getToken } from '@/composables/auth';
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

    let errorMsg =  '请求失败'
    showMessage(errorMsg,'error')

    return Promise.reject(error)
})



export default log_service