import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import { createPinia } from 'pinia'



import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import locale from 'element-plus/lib/locale/lang/zh-cn'

import 'virtual:svg-icons-register'
import SvgIcon from '@/components/base/SvgIcon.vue'


const pinia = createPinia()
const app = createApp(App)

app.use(pinia)

app.use(ElementPlus, {locale})

app.component('SvgIcon',SvgIcon)


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')
