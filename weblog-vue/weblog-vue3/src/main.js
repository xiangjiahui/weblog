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


const app = createApp(App);


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(router)
app.use(pinia)
app.mount('#app')
