import log_service from '@/utils/request'

// 获取博客设置详情
export function getBlogSettingsDetail() {
    return log_service.post("/admin/blog/settings/getDetail")
}

// 更新博客设置
export function updateBlogSettings(data) {
    return log_service.post("/admin/blog/settings/update", data)
}