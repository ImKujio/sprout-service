import {defineStore} from 'pinia'
import {asyncRef, loadAsyncRef} from "@/utils/vue-utils.ts";
import sysDictItem from "@/api/sys/sys-dict-item.js";


export const useSysDictItemStore = defineStore('sysDictItem', () => {
    const sysDicts = asyncRef(() => sysDictItem.all(['label', 'style' ]), {})
    loadAsyncRef()
    return {sysDicts}
})