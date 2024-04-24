import log_service from '@/utils/request';

export function login(username,password) {
    return log_service.post('/login',{
        username,password
    })
}

export function getUserInfo(){
    return log_service.post('/admin/user/getUserInfo')
}

export function updateAdminPassword(data) {
    return log_service.post('/admin/password/update',data)
}