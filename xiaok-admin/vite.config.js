import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import autoImport from 'unplugin-auto-import/vite'
import {createSvgIconsPlugin} from "vite-plugin-svg-icons";
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig(({mode, command}) => {
    const env = loadEnv(mode, process.cwd(), '')
    const { VITE_APP_ENV } = env
    return {
        root:'./',
        base: VITE_APP_ENV === 'production' ? '/' : '/',
        plugins: [
            vue(),
            autoImport({
                imports: [
                    'vue',
                    ],
                dts:false
            }),
            createSvgIconsPlugin({
                iconDirs: [path.resolve(process.cwd(), 'src/assets/svg')],
                symbolId: 'icon-[name]',
                svgoOptions: true
            })
        ],
        resolve: {
            alias: {
                // 设置路径
                '~': path.resolve(__dirname, './'),
                // 设置别名
                '@': path.resolve(__dirname, './src')
            },
            extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
        },
        server: {
            port: 80,
            host: true,
            proxy: {
                '/dev': {
                    target: 'http://localhost:8080',
                    changeOrigin: true,
                    rewrite: (p) => p.replace(/^\/dev/, '')
                }
            },
            headers:{
                'Keep-Alive': 'timeout=60, max=1000',
            }
        },
        optimizeDeps: {
            include: [
                'vue',
                'axios',
                'element-plus',
                '@element-plus/icons-vue',
                'js-cookie'
            ],
        },
    }
})
