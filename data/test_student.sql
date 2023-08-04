create table test_student
(
    id                    int auto_increment comment '编号'
        primary key,
    name                  varchar(10)       not null comment '姓名',
    age                   tinyint           null comment '年龄',
    stay                  tinyint default 1 not null comment '是否在校',
    score                 decimal(14, 4)    null comment '分数',
    birthday              date              null comment '生日',
    school_dismissal_time time              null comment '放学时间',
    registration_time     datetime          null comment '注册时间'
)
    comment '测试学生';

INSERT INTO sprout.test_student (id, name, age, stay, score, birthday, school_dismissal_time, registration_time) VALUES (1, '小明', 14, 1, 72.2340, '2023-05-01', '17:40:00', '2022-09-01 03:23:49');
INSERT INTO sprout.test_student (id, name, age, stay, score, birthday, school_dismissal_time, registration_time) VALUES (4, '小红', 12, 0, 66.7500, '2009-09-08', '20:30:00', '2022-09-14 03:36:00');
