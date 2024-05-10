import log_service from "@/utils/request";

export function getCategoryList(data) {
    return log_service.post('/web/category/list',data)
}

// 获取分类-文章列表
export function getCategoryArticlePageList(data) {
    return log_service.post('/web/category/article/list', data)
}