import log_service from "@/utils/request";

export function getArticlePageList(data) {
    return log_service.post('/article/list',data)
}

export function getArticleDetail(articleId) {
    return log_service.post("/article/detail", {articleId})
}