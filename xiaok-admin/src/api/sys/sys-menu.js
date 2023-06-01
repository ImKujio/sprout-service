/**
 * 系统菜单接口
 * @typedef {Object} SysMenu
 * @property {number} id - 编号
 * @property {number} pid - 父编号
 * @property {number} type - 类型
 * @property {string} name - 菜单名
 * @property {string} path - 路径
 * @property {number} sort - 排序
 */

import request from "@/api/request.js";

export default {
    /**
     * 查询系统菜单列表
     * @param {Object} params
     * @returns {Promise<SysMenu[]>}
     */
    list(params){
        return request({
            url: '/sys/menu/list',
            method: 'get',
            params: params
        })
    },
    /**
     * 查询系统菜单总数量
     * @returns {number}
     */
    total(){
        return request({
            url: '/sys/menu/total',
            method: 'get'
        })
    },
    /**
     * 查询系统菜单详情
     * @param {number} id
     * @returns {Promise<SysMenu>}
     */
    get(id){
        return request({
            url: '/sys/menu/'+id,
            method: 'get'
        })
    },
    /**
     * 按字段查询所有系统菜单
     * @returns {Promise<Object.<number,SysMenu>>}
     */
    all(fields){
        return request({
            url: '/sys/menu/all',
            method: 'get',
            params: {fields}
        })
    },
    /**
     * 添加或修改系统菜单
     * @param {SysMenu} data
     */
    put(data){
        return request({
            url: '/sys/menu',
            method: 'put',
            data: data
        })
    },
    /**
     * 删除系统菜单
     * @param {number} id
     */
    del(id){
        return request({
            url: '/sys/menu/'+id,
            method: 'delete'
        })
    }
}