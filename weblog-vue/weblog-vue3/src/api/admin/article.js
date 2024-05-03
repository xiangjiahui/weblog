import log_service from "@/utils/request";

export function getArticlePageList(data){
    return log_service.post('/admin/article/getPageList',data)
}

// 删除文章
export function deleteArticle(id) {
    return log_service.post("/admin/article/delete", {id})
}

// 发布文章
export function publishArticle(data) {
    return log_service.post("/admin/article/publish", data)
}

// 获取文章详情
export function getArticleDetail(id) {
    return log_service.post("/admin/article/detail", {id})
}

// 更新文章
export function updateArticle(data) {
    return log_service.post("/admin/article/update", data)
}