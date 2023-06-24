create table sys_user
(
    id          int auto_increment comment '编号'
        primary key,
    name        varchar(20)                    not null comment '用户名',
    nick_name   varchar(20)                    not null comment '昵称',
    avatar      varchar(100)                   null,
    password    varchar(50)   default '123456' not null comment '密码',
    permissions varchar(4000) default ''       not null comment '权限',
    create_time datetime                       null comment '创建时间',
    owner       int           default 2        not null comment '所属（0:系统,1:用户）',
    constraint sys_user_pk
        unique (name)
)
    comment '系统用户';

INSERT INTO sprout.sys_user (id, name, nick_name, avatar, password, permissions, create_time, owner) VALUES (1, 'admin', '管理员', 'sys/user/avatar/1.png', 'e10adc3949ba59abbe56e057f20f883e', 'admin', '2023-05-14 03:25:39', 0);
INSERT INTO sprout.sys_user (id, name, nick_name, avatar, password, permissions, create_time, owner) VALUES (2, 'chairman', '董事长', null, 'e10adc3949ba59abbe56e057f20f883e', '系统字典:查找列表', '2023-05-14 03:44:39', 1);
