// 消息提示
import 'element-plus/es/components/message/style/css'
// 页面顶部加载动画的效果
import nprogress from 'nprogress'

export function showMessage(message = '提示内容', type = 'success', customClass = '') {
    return ElMessage({
        type: type,
        message,
        customClass,
    })
}

export function showPageLoading() {
    nprogress.start()
}

export function hidePageLoading() {
    nprogress.done()
}