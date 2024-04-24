import {defineStore} from 'pinia'
import {ref} from 'vue'
import {getUserInfo} from '@/api/admin/user'
import {getToken,removeToken} from '@/composables/cookie';

export const useUserStore = defineStore('user', () => {
        // 用户信息
        const userInfo = ref({})

        // 设置用户信息
        function setUserInfo() {
            // 调用后头获取用户信息接口
            getUserInfo().then(res => {
                if (res.success === true) {
                    userInfo.value = res.data
                }
            }).catch(error => {
                console.log('获取登录用户信息=====================')
                console.log(error)
            })
        }

        // 退出登录
        function logout() {
            removeToken()

            userInfo.value = {}
        }

        return {userInfo, setUserInfo,logout}
    },
    {
        persist: true
    }
)
