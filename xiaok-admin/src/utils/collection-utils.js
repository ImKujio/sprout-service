import {unref} from "vue";

/**
 * 将数组转换为树形结构
 * @param {Object.<number,?>} map
 */
export function map2Tree(map) {
    const tree = []
    Object.values(map).forEach(item => {
        const parent = map[item.pid];
        if (parent) {
            if (!parent.children) {
                parent.children = []
            }
            parent.children.push(map[item.id]);
        } else {
            tree.push(map[item.id]);
        }
    })
    return tree
}
