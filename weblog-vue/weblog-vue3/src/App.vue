<template>
  <!-- 设置语言为中文 -->
  <el-config-provider :locale="locale">
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup>
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
const locale = zhCn

/**
 * 解决ResizeObserver loop completed with undelivered notifications这个错误
 * 这个错误是因为element-plus的el-table组件使用了ResizeObserver这个API，但是这个API在某些情况下会触发一个错误，导致浏览器崩溃
 */
const debounce = (fn, delay) => {
  let timer
  return (...args) => {
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      fn(...args)
    }, delay)
  }
}

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
  constructor(callback) {
    callback = debounce(callback, 200);
    super(callback);
  }
}

</script>

<style scoped>
#nprogress .bar {
  background: #409eff!important;
}
/* 暗黑模式 body 背景色 */
.dark body {
  --tw-bg-opacity: 1;
  background-color: rgb(17 24 39 / var(--tw-bg-opacity));
}
</style>
