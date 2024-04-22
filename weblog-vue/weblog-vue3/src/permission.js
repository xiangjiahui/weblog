import router from '@/router/index'
import {getToken} from '@/composables/auth'
import {showMessage,showPageLoading,hidePageLoading} from '@/composables/util'


router.beforeEach((to, from, next) => {
    console.log('==> 全局路由前置守卫')

    showPageLoading()

    const token = getToken();

    if (!token && to.path.startsWith('/admin')) {
        showMessage('请先登录', 'warning')
        next({path: '/login'})
    }else if (token && to.path === '/login'){
        showMessage('请勿重复登录','warning')
        next({path: '/admin/index'})
    } else {
        next()
    }
})


// 全局路由后置守卫
router.afterEach((to, from) => {
    // 隐藏页面加载 Loading
    hidePageLoading()

    // 动态设置页面 Title
    let title = (to.meta.title ? to.meta.title : '') + ' - Weblog'
    document.title = title
})