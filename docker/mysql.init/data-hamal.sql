create database data_hamal character set utf8mb4;
use data_hamal;

-- ----------------------------
-- 1. 用户表(用户鉴权服务)
-- ----------------------------
drop table if exists users;
create table users (
    id            varchar(50)    not null       comment '用户ID',
    username      varchar(30)    not null       comment '用户账号',
    password      varchar(255)   not null       comment '密码',
    nickname      varchar(30)    default ''     comment '用户昵称',
    email         varchar(50)    default ''     comment '用户邮箱',
    tel_number    varchar(15)    default ''     comment '手机号码',
    sex           tinyint(1)     default 0      comment '用户性别（1男 2女 0未知）',
    avatar        varchar(255)   default ''     comment '头像地址',
    status        tinyint(1)     default 0      comment '帐号状态（0正常 1停用）',
    create_time   datetime       default now()  comment '创建时间',
    update_time   datetime       default now()  comment '更新时间',
    deleted       tinyint(1)     default 0      comment '删除标志（0否 1是）',
    primary key (id)
) engine = innodb comment = '用户表';
create unique index users_username_index on users (username);


-- ----------------------------
-- 2. 文件表(文件存储服务)
-- ----------------------------
drop table if exists files;
create table files (
    id            varchar(50)    not null       comment '文件ID',
    filename      varchar(30)    not null       comment '文件名',
    filepath      varchar(255)   not null       comment '文件路径',
    filetype      varchar(30)    not null       comment '文件类型',
    hashcode      varchar(255)   not null       comment '文件Hash',
    create_time   datetime       default now()  comment '创建时间',
    update_time   datetime       default now()  comment '更新时间',
    deleted       tinyint(1)     default 0      comment '删除标志（0否 1是）',
    primary key (id)
) engine = innodb comment = '文件表';

-- ----------------------------
-- 3. 号段表(ID服务)
-- ----------------------------
drop table if exists alloc;
create table alloc (
    biz_tag       varchar(128)   not null default '',
    max_id        bigint(20)     not null default '1',
    step          int(11)        not null,
    description   varchar(256)   default null,
    update_time   timestamp      not null default current_timestamp on update current_timestamp,
    primary key (biz_tag)
) engine=innodb comment = '号段表';

insert into alloc (biz_tag, max_id, step, description, update_time) value ('test', 1, 1, 'test case', now());
