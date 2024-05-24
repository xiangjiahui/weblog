import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useCommentStore = defineStore('comment', () => {
        // 评论用户信息
        const userInfo = ref({})

        return {userInfo}
    },
    {
        persist: true
    }
)