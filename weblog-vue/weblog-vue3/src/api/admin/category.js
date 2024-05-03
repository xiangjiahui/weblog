import log_service from '@/utils/request';

export function getCategoryPageList(data){
    return log_service.post('/admin/category/getPageList',data)
}

export function addCategory(data) {
    return log_service.post('/admin/category/add',data)
}

export function deleteCategory(id) {
    return log_service.post('/admin/category/delete',{id},
        {
            headers: {
                'Content-Type' : 'multipart/form-data'
            }
        })
}

// 获取分类 select 数据
export function getCategorySelectList() {
    return log_service.get("/admin/category/getAll")
}