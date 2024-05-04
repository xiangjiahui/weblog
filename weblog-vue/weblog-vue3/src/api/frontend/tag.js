import log_service from "@/utils/request";

export function getTagList(data){
    return log_service.post('/tag/list',data)
}