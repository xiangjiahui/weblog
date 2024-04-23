import { useCookies } from '@vueuse/integrations/useCookies'

/**
 * 为了不丢失标签数据，同样需要将已添加的标签数据存储到 cookie 中，
 * 当页面重新加载时，再从 cookie 中获取，重新渲染到页面。
 */
// 存储在 Cookie 中的标签页数据的 key
const TAB_LIST_KEY = 'tabList'
const cookie = useCookies()

// 获取 TabList
export function getTabList() {
    return cookie.get(TAB_LIST_KEY)
}

// 存储 TabList 到 Cookie 中
export function setTabList(tabList) {
    return cookie.set(TAB_LIST_KEY, tabList)
}