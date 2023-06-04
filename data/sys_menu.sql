create table sys_menu
(
    id   int auto_increment comment '编号'
        primary key,
    pid  int           null comment '父编号',
    type int           not null comment '类型',
    name varchar(20)   not null comment '名称',
    sort int default 1 not null comment '排序',
    path varchar(80)   null comment '组件地址',
    page varchar(40)   null comment '页面组件地址'
)
    comment '后台菜单';

INSERT INTO sprout.sys_menu (id, pid, type, name, sort, path, page) VALUES (101, 0, 3, '系统管理', 1, null, null);
INSERT INTO sprout.sys_menu (id, pid, type, name, sort, path, page) VALUES (102, 101, 4, '菜单设置', 1, '/sys-menu', 'SysMenu');
INSERT INTO sprout.sys_menu (id, pid, type, name, sort, path, page) VALUES (103, 101, 4, '字典设置', 1, null, null);
