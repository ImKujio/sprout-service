import request from "../request.js";

/**
 * 系统用户
 * @typedef {Object} SysUser
 * @property {string} name - 用户名
 * @property {string} nickName - 昵称
 * @property {string} createTime - 创建时间
 * @property {string} owner - 所属类型
 */

export default {
    /**
     * 查询系统用户列表
     * @param {Object} params
     * @returns {Promise<SysUser[]>}
     */
    list(params){
        return request({
            url: '/sys/user/list',
            method: 'get',
            params: params
        })
    },
    /**
     * 查询系统用户总数量
     * @returns {number}
     */
    total(){
        return request({
            url: '/sys/user/total',
            method: 'get'
        })
    },
    /**
     * 查询系统用户详情
     * @param {number} id
     * @returns {Promise<SysUser>}
     */
    get(id){
        return request({
            url: '/sys/user/'+id,
            method: 'get'
        })
    },
    /**
     * 按字段查询所有系统用户
     * @param {string[]} fields
     * @returns {Promise<Object.<number,SysUser>>}
     */
    all(fields){
        return request({
            url: '/sys/user/all',
            method: 'get',
            params: {fields}
        })
    },
    /**
     * 添加或修改系统用户
     * @param {SysUser} data
     * @returns {Promise<void>}
     */
    put(data){
        return request({
            url: '/sys/user',
            method: 'put',
            data: data
        })
    },
    /**
     * 删除系统用户
     * @param {number} id
     * @returns {Promise<void>}
     */
    del(id){
        return request({
            url: '/sys/user/'+id,
            method: 'delete'
        })
    }
}