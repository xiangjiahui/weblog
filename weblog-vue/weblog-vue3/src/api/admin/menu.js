import log_service from '@/utils/request'


export function getPageMenuList(data){
    return log_service.post('/admin/menu/getPageList',data)
}

// export function getTreeMenu(){
//     return log_service.get('/admin/menu/getTreeMenu')
// }

export function addMenu(data){
    return log_service.post('/admin/menu/addMenu',data)
}

export function getAllMenu(){
    return log_service.get('/admin/menu/getAllMenu')
}