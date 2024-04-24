/**
 * Pinia 是 Vue 的专属状态管理库，它允许你跨组件或页面共享状态，
 * 更详细文档请访问官网地址：https://pinia.vuejs.org/zh/ 。它具备如下特性：
 * 1、所见即所得 ：与组件类似的 Store。其 API 的设计旨在让你编写出更易组织的 store；
 * 2、类型安全：类型可自动推断，即使在 JavaScript 中亦可为你提供自动补全功能！
 * 3、开发工具支持：不管是 Vue 2 还是 Vue 3，支持 Vue devtools 钩子的 Pinia 都能给你更好的开发体验。
 * 4、可扩展性 ： 可通过事务、同步本地存储等方式扩展 Pinia，以响应 store 的变更。
 * 5、模块化设计：可构建多个 Store 并允许你的打包工具自动拆分它们。
 * 6、极致轻量化: Pinia 大小只有 1kb 左右，你甚至可能忘记它的存在！
 */

import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useMenuStore = defineStore('menu', () => {
        // 左边栏菜单默认宽度
        const menuWidth = ref("250px")

        // 展开或伸缩左边栏菜单
        function handleMenuWidth() {
            menuWidth.value = menuWidth.value === '250px' ? '64px' : '250px'
        }

        return {menuWidth, handleMenuWidth}
    },
    {
        persist: true
    }
)