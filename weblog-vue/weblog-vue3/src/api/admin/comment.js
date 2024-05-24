import request from "@/utils/request"

// 获取评论分页数据
export function getCommentPageList(data) {
    return request.post("/admin/comment/list", data)
}

// 删除评论
export function deleteComment(id) {
    return request.post("/admin/comment/delete", {id})
}

// 审核评论
export function examineComment(data) {
    return request.post("/admin/comment/examine", data)
}