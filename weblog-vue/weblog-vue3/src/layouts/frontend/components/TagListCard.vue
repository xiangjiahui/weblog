<template>
  <div v-if="tags.length > 0" class="w-full p-5 mb-3 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700">
    <!-- 标签标题 -->
    <h2 class="flex items-center mb-2 font-bold text-gray-900 uppercase dark:text-white">
      <svg t="1714986920439" class="icon w-5 h-5 mr-2" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4429" width="200" height="200"><path d="M463.6416 161.28a44.7552 44.7552 0 0 1 40.4032 12.2752l393.7216 393.7152a44.8 44.8 0 0 1 0 63.36l-262.4832 262.4768a44.8 44.8 0 0 1-63.36 0L178.2208 499.3856a44.7232 44.7232 0 0 1-12.8-37.0944V212.4864a51.2 51.2 0 0 1 51.2-51.2h247.0272zM381.856 359.104c24.9984-24.9984 24.9984-65.5232 0-90.5152-24.992-24.992-65.5168-24.992-90.5088 0-24.992 24.992-24.992 65.5168 0 90.5088 24.992 24.992 65.5168 24.992 90.5088 0z" fill="#59AAFF" p-id="4430"></path></svg>
      标签
    </h2>
    <!-- 标签列表 -->
    <span v-for="(tag, index) in tags" :key="index" @click="goTagArticleListPage(tag.id, tag.name)"
          class="inline-block mb-1 cursor-pointer bg-green-100 text-green-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded hover:bg-green-200 hover:text-green-900 dark:bg-green-900 dark:text-green-300">
            {{ tag.name }}
        </span>
  </div>
</template>

<script setup>
import { getTagList } from '@/api/frontend/tag'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 所有标签
const tags = ref([])
getTagList().then((res) => {
  if (res.success) {
    tags.value = res.data
  }
})

// 跳转标签文章列表页
const goTagArticleListPage = (id, name) => {
  // 跳转时通过 query 携带参数（标签 ID、标签名称）
  router.push({path: '/tag/article/list', query: {id, name}})
}
</script>

<style scoped>

</style>