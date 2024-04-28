import log_service from "@/utils/request"

// 上传文件
export function uploadFile(form) {
    return log_service.post("/admin/file/upload", form,{
        headers: {
            'Content-Type' : 'multipart/form-data'
        }
    })
}