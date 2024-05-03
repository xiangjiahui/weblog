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

// 根据标签名模糊查询
export function searchTags(key) {
    return log_service.post("/admin/tag/search", {key})
}

// 获取标签 select 列表数据
export function getTagSelectList() {
    return log_service.get("/admin/tag/getAll")
}