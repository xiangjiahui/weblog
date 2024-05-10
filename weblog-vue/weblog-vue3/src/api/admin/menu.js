import log_service from '@/utils/request'


export function getAllMenu(data){
    return log_service.post('/admin/menu/getAllMenu',data)
}

// export function getTreeMenu(){
//     return log_service.get('/admin/menu/getTreeMenu')
// }

export function addMenu(data){
    return log_service.post('/admin/menu/addMenu',data)
}