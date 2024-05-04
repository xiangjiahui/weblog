import log_service from "@/utils/request";

export function getCategoryList(data) {
    return log_service.post('/category/list',data)
}