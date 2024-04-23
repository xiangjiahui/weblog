<template>
  <div class="bg-slate-800 h-screen text-white menu-container transition-all" :style="{ width: menuStore.menuWidth }">
    <!-- 顶部 Logo, 指定高度为 64px, 和右边的 Header 头保持一样高 -->
    <!-- 顶部 Logo, 指定高度为 64px, 和右边的 Header 头保持一样高 -->
    <div class="flex items-center justify-center h-[64px]">
      <img v-if="menuStore.menuWidth === '250px'" src="@/assets/weblog-logo.png" class="h-[60px]">
      <img v-else src="@/assets/weblog-logo-mini.png" class="h-[60px]">
    </div>

    <!-- 下方菜单 -->
    <el-menu :default-active="defaultActive" @select="handleSelect" :collapse="isCollapse" :collapse-transition="false"
             class="el-menu-vertical-demo">
      <template v-for="(item, index) in menus" :key="index">
        <el-menu-item :index="item.path">
          <el-icon>
            <!-- 动态图标 -->
            <component :is="item.icon"></component>
          </el-icon>
          <span>{{ item.name }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script setup>
import { ref,computed } from 'vue'
import { useRoute,useRouter } from 'vue-router'
import { useMenuStore } from '@/stores/menu'
// 引入 useMenuStore
const menuStore = useMenuStore()

const menus = [
  {
    'name': '仪表盘',
    'icon': 'Monitor',
    'path': '/admin/index'
  },
  {
    'name': '文章管理',
    'icon': 'Document',
    'path': '/admin/article/list',
  },
  {
    'name': '分类管理',
    'icon': 'FolderOpened',
    'path': '/admin/category/list',
  },
  {
    'name': '标签管理',
    'icon': 'PriceTag',
    'path': '/admin/tag/list',
  },
  {
    'name': '博客设置',
    'icon': 'Setting',
    'path': '/admin/blog/setting',
  },
]

const route = useRoute()
const router = useRouter()

// 根据路由地址判断哪个菜单被选中
const defaultActive = ref(route.path)

// 菜单选择事件
const handleSelect = (path) => {
  router.push(path)
}

const isCollapse = computed(() =>  !(menuStore.menuWidth === '250px'))
</script>

<style scoped>
.el-menu {
  background-color: rgb(30 41 59);
  border-right: 0;
}

.el-sub-menu__title {
  color: #fff;
}

.el-sub-menu__title:hover {
  background-color: #ffffff10;
}

.el-menu-item.is-active {
  background-color: var(--el-color-primary);
  color: #fff;
}

.el-menu-item.is-active:hover {
  background-color: var(--el-color-primary);
}

.el-menu-item {
  color: #fff;
}

.el-menu-item:hover {
  background-color: #ffffff10;
}

</style>