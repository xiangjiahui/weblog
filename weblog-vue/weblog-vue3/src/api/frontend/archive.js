import log_service from "@/utils/request";

export function getArchivePageList(data){
    return log_service.post('/web/archive/list',data)
}