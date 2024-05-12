// 消息提示
import 'element-plus/es/components/message/style/css'
// 页面顶部加载动画的效果
import nprogress from 'nprogress'

export function showMessage(message = '提示内容', type = 'success', customClass = '') {
    return ElMessage({
        type: type,
        message,
        customClass,
        showClose: true,
        duration: 1000
    })
}

export function showPageLoading() {
    nprogress.start()
}

export function hidePageLoading() {
    nprogress.done()
}


// 弹出确认框
export function showModel(content = '提示内容', type = 'warning', title = 'Warning') {
    return ElMessageBox.confirm(
        content,
        title,
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type,
        }
    )
}