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
import ArchiveList from '@/pages/frontend/Archive-List'
import WebCategoryList from '@/pages/frontend/Category-List'
import CategoryArticleList from '@/pages/frontend/Category-Article-List'
import WebTagList from '@/pages/frontend/Tag-List'
import TagArticleList from '@/pages/frontend/Tag-Article-List'
import ArticleDetail from '@/pages/frontend/ArticleDetail'
import NotFound from '@/pages/frontend/404'

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
    },
    {
        path: '/archive/list', // 归档页
        component: ArchiveList,
        meta: { // meta 信息
            title: 'Weblog 归档页'
        }
    },
    {
        path: '/category/list', // 分类页
        component: WebCategoryList,
        meta: { // meta 信息
            title: 'Weblog 分类页'
        }
    },
    {
        path: '/category/article/list', // 分类文章页
        component: CategoryArticleList,
        meta: { // meta 信息
            title: 'Weblog 分类文章页'
        }
    },
    {
        path: '/tag/list', // 标签列表页
        component: WebTagList,
        meta: { // meta 信息
            title: 'Weblog 标签列表页'
        }
    },
    {
        path: '/tag/article/list', // 标签列表页
        component: TagArticleList,
        meta: { // meta 信息
            title: 'Weblog 标签文章页'
        }
    },
    {
        path: '/article/:articleId', // 文章详情页
        component: ArticleDetail,
        meta: { // meta 信息
            title: 'Weblog 详情页'
        }
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: NotFound,
        meta: {
            title: '404 页'
        }
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes: routers
})

export default router