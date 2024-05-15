import log_service from '@/utils/request'

// 文章搜索
export function getArticleSearchPageList(data) {
    return log_service.post('/web/article/search', data)
}