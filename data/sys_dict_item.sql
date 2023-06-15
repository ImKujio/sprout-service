create table sys_dict_item
(
    id     int auto_increment comment '编号'
        primary key,
    dict   int         not null comment '字典',
    name   varchar(40) not null comment '名称',
    label  varchar(20) not null comment '显示名',
    style  varchar(20) null comment '样式',
    remark varchar(80) null comment '备注',
    constraint sys_dict_item_sys_dict_null_fk
        foreign key (dict) references sys_dict (id)
)
    comment '系统字典项';

INSERT INTO sprout.sys_dict_item (id, dict, name, label, style, remark) VALUES (1, 1, 'system', '系统', null, null);
INSERT INTO sprout.sys_dict_item (id, dict, name, label, style, remark) VALUES (2, 1, 'user', '用户', null, null);
INSERT INTO sprout.sys_dict_item (id, dict, name, label, style, remark) VALUES (3, 2, 'menu', '菜单', null, null);
INSERT INTO sprout.sys_dict_item (id, dict, name, label, style, remark) VALUES (4, 2, 'page', '页面', null, null);
INSERT INTO sprout.sys_dict_item (id, dict, name, label, style, remark) VALUES (6, 5, 'item1', '字典项1', 'class1', '备注1，修改');
