import { useCookies } from '@vueuse/integrations/useCookies'

const TOKEN_KEY = 'Authorization'
const cookie = useCookies()

export function getToken() {
    return cookie.get(TOKEN_KEY)
}

export function setToken(token) {
    return cookie.set(TOKEN_KEY,token)
}

export function removeToken() {
    return cookie.remove(TOKEN_KEY)
}