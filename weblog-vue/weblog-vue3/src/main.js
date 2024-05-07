import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router';
import '@/assets/main.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'animate.css';
// 导入全局路由守卫
import '@/permission'
import 'nprogress/nprogress.css'
import pinia from '@/stores'
// 图片点击放大
import 'viewerjs/dist/viewer.css'
import VueViewer from 'v-viewer'


const app = createApp(App);


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(router)
app.use(pinia)
app.use(VueViewer)
app.mount('#app')
