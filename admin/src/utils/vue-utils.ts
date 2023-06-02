import {getCurrentInstance, ref, Ref, UnwrapRef} from "vue";
import {CountDownLatch} from "./async-utils";


/**
 * ## 创建异步的ref响应式对象
 *
 * > 作用一: 用于在setup中创建异步的ref响应式对象，可以在异步获取值之后，自动更新ref的值。
 *
 * > 作用二: 异步数据对齐，避免因不同请求数据返回时间不同而导致的频繁触发DOM更新。
 *
 * > 作用三: 提供重新加载的方法句柄。
 *
 * *需要配合loader方法使用*
 *
 * 参数支持两种形式：
 * 1. 直接传入一个promise，这个promise的结果会被赋值给ref
 * 2. 传入一个箭头函数并且返回值为一个promise，这个promise的结果会被赋值给ref
 *
 * 无论传入的是哪种形式，都会在调用loader方法的时候立即执行，区别是第一种形式的只会执行一次，而第二种形式会在每次调用loader方法的时候重新执行
 *
 * ---
 *
 * 示例
 *
 * ```javascript
 * // dict为不需要重新加载的异步数据
 * const dict = asyncRef(api.dict(), {})
 * // list为需要重新加载的异步数据
 * const list = asyncRef(() => api.list(), [])
 * // 调用loader方法,立即请求所有异步数据
 * // 会在请求之前执行before方法，所有请求数据返回之后执行after方法
 * // 返回的reload方法可以在需要的时候重新请求所有以箭头函数创建的异步数据，这里只会重新请求list
 * const reload = loader(() => {
 *   loading.value = "加载中..."
 * }, () => {
 *   loading.value = "加载完成"
 * })
 *
 * ```
 *
 * @param  def 默认值
 * @param  promise 异步获取值的promise，或者是一个函数，函数返回一个promise
 */
export function asyncRef<T>(promise: Promise<T> | (() => Promise<T>), def: T): Ref<UnwrapRef<T>> {
    const data = ref(def);
    // 如果是函数，那么就记录到reloadRefs中
    if (typeof promise === 'function') {
        let reloadRefs = getCurrentInstance()['reloadRefs']
        if (!reloadRefs) reloadRefs = []
        getCurrentInstance()['reloadRefs'] = reloadRefs
        reloadRefs.push({
            data: data,
            handle: promise,
            value: null,
        })
    } else {
        let initRefs = getCurrentInstance()['initRefs']
        if (!initRefs) initRefs = []
        getCurrentInstance()['initRefs'] = initRefs
        initRefs.push({
            data: data,
            promise: promise,
            value: null,
        })
    }
    return data;
}

/**
 * ## 异步数据请求加载器
 *
 * 需要配合asyncRef方法使用
 * @param before 数据请求之前执行的方法
 * @param after 数据请求之后执行的方法
 * @returns 重新加载异步数据的方法
 */
export function loadAsyncRef(before: () => void = () => {}, after: () => void = () => {}): () => void {
    const instance = getCurrentInstance()
    before()
    const initLock = new CountDownLatch()
    const initRefs = instance['initRefs']
    const reloadRefs = instance['reloadRefs']
    if (!!initRefs && initRefs.length > 0) {
        for (let initRef of initRefs) {
            initLock.add()
            initRef.promise.then((value) => {
                initRef.value = value;
                initLock.cutDown()
            })
        }
    }
    if (!!reloadRefs && reloadRefs.length > 0) {
        for (let reloadRef of reloadRefs) {
            initLock.add()
            reloadRef.handle().then((value) => {
                reloadRef.value = value;
                initLock.cutDown()
            })
        }
    }
    initLock.await().then(() => {
        if (!!initRefs && initRefs.length > 0) {
            for (let initRef of initRefs) {
                initRef.data.value = initRef.value
            }
        }
        if (!!reloadRefs && reloadRefs.length > 0) {
            for (let reloadRef of reloadRefs) {
                reloadRef.data.value = reloadRef.value
            }
        }
        after()
    })
    return function () {
        before()
        const reloadLock = new CountDownLatch()
        if (!reloadRefs || reloadRefs.length === 0) {
            after()
            return
        }
        for (let reloadRef of reloadRefs) {
            reloadLock.add()
            reloadRef.handle().then((value) => {
                reloadRef.value = value;
                reloadLock.cutDown()
            })
        }
        reloadLock.await().then(() => {
            for (let reloadRef of reloadRefs) {
                reloadRef.data.value = reloadRef.value
            }
            after()
        })
    }
}