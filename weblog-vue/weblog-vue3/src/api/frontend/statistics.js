import log_service from "@/utils/request";

// 获取统计信息（文章总数、分类总数、标签总数、总访问量）
export function getStatisticsInfo(){
    return log_service.post('/web/statistics/info')
}