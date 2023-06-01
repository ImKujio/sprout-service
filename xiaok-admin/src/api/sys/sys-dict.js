import request from "@/api/request.js";

/**
 * @typedef {Object} SysDict
 * @property {number} id - id
 * @property {string} label - 字典名
 * @property {string} remark - 备注
 * @property {number} owner - 所属类型
 */

export default {
    /**
     * 查询系统字典列表
     * @param {Object} params
     * @returns {Promise<SysDict[]>}
     */
    list(params){
        return request({
            url: '/sys/dict/list',
            method: 'get',
            params: params
        })
    },
    /**
     * 查询系统字典总数量
     * @returns {number}
     */
    total(){
        return request({
            url: '/sys/dict/total',
            method: 'get'
        })
    },
    /**
     * 查询系统字典详情
     * @param {number} id
     * @returns {Promise<SysDict>}
     */
    get(id){
        return request({
            url: '/sys/dict/'+id,
            method: 'get'
        })
    },
    /**
     * 按字段查询所有系统字典
     * @param {string[]} fields
     * @returns {Promise<Object.<number,SysDict>>}
     */
    all(fields){
        return request({
            url: '/sys/dict/all',
            method: 'get',
            params: {fields}
        })
    },
    /**
     * 添加或修改系统字典
     * @param {SysDict} data
     */
    put(data){
        return request({
            url: '/sys/dict',
            method: 'put',
            data: data
        })
    },
    /**
     * 删除系统字典
     * @param {number} id
     */
    del(id){
        return request({
            url: '/sys/dict/'+id,
            method: 'delete'
        })
    }
}