create table sys_menu
(
    id   int auto_increment comment '编号'
        primary key,
    pid  int           null comment '父编号',
    type int           not null comment '类型',
    name varchar(20)   not null comment '名称',
    sort int default 1 not null comment '排序',
    path varchar(80)   null comment '组件地址'
)
    comment '后台菜单';

INSERT INTO sprout.sys_menu (id, pid, type, name, sort, path) VALUES (1, 0, 3, '系统管理', 1, null);
INSERT INTO sprout.sys_menu (id, pid, type, name, sort, path) VALUES (2, 1, 4, '菜单设置', 1, null);
INSERT INTO sprout.sys_menu (id, pid, type, name, sort, path) VALUES (3, 1, 4, '字典设置', 1, null);
