import log_service from '@/utils/request'

// 获取 QQ 用户信息
export function getUserInfoByQQ(qq) {
    return log_service.post("/web/comment/qq/userInfo", {qq})
}

// 发布评论
export function publishComment(data) {
    return log_service.post("/web/comment/publish", data)
}

// 获取所有评论
export function getComments(routerUrl) {
    return log_service.post("/web/comment/list", {routerUrl})
}