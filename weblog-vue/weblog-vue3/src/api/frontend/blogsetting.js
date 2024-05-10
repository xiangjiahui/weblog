import log_service from '@/utils/request';

// 获取博客设置详情
export function getBlogSettingsDetail() {
    return log_service.post("/web/blog/setting/detail")
}