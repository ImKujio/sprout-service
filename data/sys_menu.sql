create table sys_menu
(
    id        int auto_increment comment '编号'
        primary key,
    pid       int           null comment '父编号',
    type      int           not null comment '类型',
    name      varchar(20)   not null comment '名称',
    icon      varchar(20)   null comment '图标',
    sort      int default 1 not null comment '排序',
    path      varchar(80)   null comment '组件地址',
    component varchar(40)   null comment '页面组件地址'
)
    comment '后台菜单';

INSERT INTO sprout.sys_menu (id, pid, type, name, icon, sort, path, component) VALUES (101, 0, 3, '系统管理', 'settings', 1, null, null);
INSERT INTO sprout.sys_menu (id, pid, type, name, icon, sort, path, component) VALUES (102, 101, 4, '菜单设置', 'menu', 1, '/sys-menu', 'SysMenu');
INSERT INTO sprout.sys_menu (id, pid, type, name, icon, sort, path, component) VALUES (103, 101, 4, '字典设置', 'dict', 1, '/sys-dict', 'SysDict');
