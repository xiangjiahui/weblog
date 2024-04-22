import log_service from '@/utils/request';

export function login(username,password) {
    return log_service.post('/login',{
        username,password
    })
}