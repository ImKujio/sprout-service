create table sys_dict
(
    id     int auto_increment comment '编号'
        primary key,
    name   varchar(20)   not null comment '字典名',
    label  varchar(20)   not null comment '显示名',
    remark varchar(80)   null comment '备注',
    owner  int default 2 not null comment '所属',
    constraint sys_dict_pk
        unique (name)
)
    comment '系统字典';

INSERT INTO xiaok.sys_dict (id, name, label, remark, owner) VALUES (1, 'sys_owner', '所属类型', null, 1);
INSERT INTO xiaok.sys_dict (id, name, label, remark, owner) VALUES (2, 'admin_menu_type', '后台菜单类型', null, 1);
