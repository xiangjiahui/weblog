import log_service from '@/utils/request'


export function getAllMenu(){
    return log_service.get('/admin/menu/getAllMenu')
}