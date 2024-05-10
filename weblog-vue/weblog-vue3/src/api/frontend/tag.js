import log_service from "@/utils/request";

export function getTagList(data){
    return log_service.post('/web/tag/list',data)
}

export function getTagArticlePageList(data){
    return log_service.post('/web/tag/article/list',data)
}