
/**
 * 将Map转换为树形结构
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

/**
 * 将数组转换为树形结构
 * @param {Array} list
 */
export function list2Tree(list) {
    const map = {}
    list.forEach(item => {
        map[item.id] = item
    })
    return map2Tree(map)
}