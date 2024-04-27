import log_service from '@/utils/request';


export function addTag(data) {
    return log_service.post('/admin/tag/add',data)
}

export function getTagPageList(data){
    return log_service.post('/admin/tag/getPageList',data)
}

export function deleteTag(id) {
    return log_service.post('/admin/tag/delete',{id},{
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}