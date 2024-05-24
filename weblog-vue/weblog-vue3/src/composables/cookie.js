import { useCookies } from '@vueuse/integrations/useCookies'

const cookie = useCookies()

// ============================== Token 令牌 ==============================

// 存储在 Cookie 中的 Token 的 key
const TOKEN_KEY = 'Authorization'

// 获取 Token 值
export function getToken() {
    return cookie.get(TOKEN_KEY)
}

// 设置 Token 到 Cookie 中
export function setToken(token) {
    return cookie.set(TOKEN_KEY, token)
}

// 删除 Token
export function removeToken() {
    return cookie.remove(TOKEN_KEY)
}

// ============================== 标签页 ==============================

// 存储在 Cookie 中的标签页数据的 key
const TAB_LIST_KEY = 'tabList'

// 获取 TabList
export function getTabList() {
    return cookie.get(TAB_LIST_KEY)
}

// 存储 TabList 到 Cookie 中
export function setTabList(tabList) {
    return cookie.set(TAB_LIST_KEY, tabList)
}


// ============================== 菜单页 ==============================
const MENU_LIST_KEY = 'menuList'

// 获取 MenuList
export function getMenuList() {
    return cookie.get(MENU_LIST_KEY)
}

// 存储 MenuList 到 Cookie 中
const date = new Date();
const year = date.getFullYear()
const month = date.getMonth() + 1
const day = date.getDate() + 5
const expired = year + '-' + month + '-' + day
export function setMenuList(menuList) {
    return cookie.set(MENU_LIST_KEY, menuList,{
        expires: new Date(expired)
    })
}
export function removeMenuList() {
    return cookie.remove(MENU_LIST_KEY)
}