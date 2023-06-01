import request from "@/api/request.js";

/**
 * @description 系统字典项
 * @typedef {Object} SysDictItem
 * @property {number} id - id
 * @property {string} label - 字典项名
 * @property {string} style - 样式
 * @property {string} remark - 备注
 */

export default {
    /**
     * 查询系统字典项列表
     * @param {Object} params
     * @returns {Promise<SysDictItem[]>}
     */
    list(params) {
        return request({
            url: '/sys/dict-item/list',
            method: 'get',
            params: params
        })
    },
    /**
     * 查询系统字典项总数量
     * @returns {number}
     */
    total() {
        return request({
            url: '/sys/dict-item/total',
            method: 'get'
        })
    },
    /**
     * 查询系统字典项详情
     * @param {number} id
     * @returns {Promise<SysDictItem>}
     */
    get(id) {
        return request({
            url: '/sys/dict-item/' + id,
            method: 'get'
        })
    },
    /**
     * 按字段查询所有系统字典项
     * @param {string[]} fields
     * @returns {Promise<Object.<number,SysDictItem>>}
     */
    all(fields) {
        return request({
            url: '/sys/dict-item/all',
            method: 'get',
            params: {fields}
        })
    },
    /**
     * 添加或修改系统字典项
     * @param {SysDictItem} data
     */
    put(data) {
        return request({
            url: '/sys/dict-item',
            method: 'put',
            data: data
        })
    },
    /**
     * 删除系统字典项
     * @param {number} id
     */
    del(id) {
        return request({
            url: '/sys/dict-item/' + id,
            method: 'delete'
        })
    }
}