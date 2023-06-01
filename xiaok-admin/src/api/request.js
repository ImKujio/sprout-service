import axios from 'axios'
import Cookies from 'js-cookie'
import {ElMessage} from "element-plus";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const TokenKey = 'Admin-Token'

const service = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    timeout: 10000
})

service.interceptors.request.use(config => {
    config.headers['Authorization'] = 'Bearer ' + Cookies.get(TokenKey)
    if ((config.method === 'get' || config.method === 'del') && config.params) {
        config.url = config.url + '?' + encodeParams(config.params);
        config.params = {};
    }
    return config
})

service.interceptors.response.use(response => {
    if (response.data.code === 200) return response.data.data
    if (response.data.code === 401) {
        ElMessage({message: '登录已过期，请重新登录', type: 'error'})
        Cookies.remove(TokenKey)
        location.href = '/index';
        return Promise.reject()
    }
    ElMessage({message: response.data.msg, type: 'error'})
    return Promise.reject(response.data.msg)
})

export function encodeParams(obj, prefix = null) {
    const str = [];
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            const value = obj[key];
            const finalKey = prefix ? `${prefix}[${key}]` : key;
            if (value !== null && Array.isArray(value)) {
                const encodedKey = encodeURIComponent(finalKey);
                const encodedValues = value.map((val) => encodeURIComponent(val)).join(",");
                str.push(`${encodedKey}=${encodedValues}`);
            } else if (value !== null && typeof value === "object") {
                str.push(encodeParams(value, finalKey));
            } else if (value !== undefined) {
                str.push(`${encodeURIComponent(finalKey)}=${encodeURIComponent(value)}`);
            }
        }
    }
    return str.join("&");
}

export default service