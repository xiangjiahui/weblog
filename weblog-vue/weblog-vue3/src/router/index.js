import Index from '@/pages/frontend/Index'
import {createRouter,createWebHashHistory} from "vue-router"
import Login from '@/pages/admin/Login'
import AdminIndex from '@/pages/admin/AdminIndex'

const routers = [
    {
        path: '/',
        component: Index,
        meta: {
            title: 'Weblog 首页'
        }
    },
    {
        path: '/login',
        component: Login,
        meta: {
            title: 'Weblog 登录页'
        }
    },
    {
        path: '/admin/index',
        component: AdminIndex,
        meta: {
            title: 'Admin 后台首页'
        }
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes: routers
})

export default router