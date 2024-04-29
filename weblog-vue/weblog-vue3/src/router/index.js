import Index from '@/pages/frontend/Index'
import {createRouter,createWebHashHistory} from "vue-router"
import Login from '@/pages/admin/Login'
import AdminIndex from '@/pages/admin/AdminIndex'
import Admin from '@/layouts/admin/Admin'
import ArticleList from '@/pages/admin/Article-List'
import TagList from '@/pages/admin/Tag-List'
import BlogSetting from '@/pages/admin/Blog-Setting'
import CategoryList from '@/pages/admin/Category-List'
import MenuList from '@/pages/admin/Menu-List'

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
        path: '/admin',
        component: Admin,
        children: [
            {
                path: '/admin/index',
                component: AdminIndex,
                meta: {
                    title: '仪表盘'
                }
            },
            {
                path: '/admin/article/list',
                component: ArticleList,
                meta: {
                    title: '文章管理'
                }
            },
            {
                path: "/admin/category/list",
                component: CategoryList,
                meta: {
                    title: '分类管理'
                }
            },
            {
                path: "/admin/tag/list",
                component: TagList,
                meta: {
                    title: '标签管理'
                }
            },
            {
                path: "/admin/blog/setting",
                component: BlogSetting,
                meta: {
                    title: '博客设置'
                }
            },
            {
                path: '/admin/menu/setting',
                component: MenuList,
                meta: {
                    title: '菜单设置'
                }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes: routers
})

export default router