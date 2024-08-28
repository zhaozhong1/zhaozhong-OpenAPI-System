# 数据库初始化

-- 创建库
create database if not exists zz_api;

-- 切换库
use zz_api;

-- 用户表


create table if not exists zz_api.api_info
(
    `id`              bigint       not null auto_increment comment 'id' primary key comment '主键自增',
    `api_name`        varchar(64)  not null unique comment '接口名称',
    `api_description` varchar(512) comment '接口描述',
    `api_url`         varchar(512) not null comment '接口地址',
    `method`          varchar(256) not null comment '方接口法',
    `request_params`  text         not null comment '请求参数',
    `req_header`      text         not null comment '请求头',
    `resp_header`     text         not null comment '响应头',
    `api_status`      tinyint      not null default 0 comment '接口状态 0-关闭 1-开启',
    `user_id`         bigint       not null comment '创建人',
    `create_time`     datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`     datetime     not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`       tinyint      not null default 0 comment '逻辑删除 0-未删 1-已删'
);

create table if not exists user_key
(
    `id`          bigint auto_increment comment 'id' primary key,
    `user_id`     bigint comment 'user_id'           not null,
    `access_key`  varchar(512)                       not null comment 'access_key',
    `secret_key`  varchar(512)                       not null comment 'access_key',
    `is_active`   tinyint  default 0                 not null comment '是否激活 0激活 1非激活',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `is_delete`   tinyint  default 0                 not null comment '是否删除'
) comment '用户ak/sk列表' collate = utf8mb4_unicode_ci;

create table if not exists zz_api.user_api_info
(
    `id`          bigint                             not null auto_increment comment '主键id' primary key,
    `user_id`     bigint                             not null comment '调用用户id',
    `api_info_id` bigint                             not null comment '接口id',
    `total_num`   int      default 0                 not null comment '总调用次数',
    `left_num`    int      default 0                 not null comment '剩余调用次数',
    `status`      int      default 0                 not null comment '0-正常 1-禁用',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime                           not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`   tinyint                            not null default 0 comment '逻辑删除 0-未删 1-已删'

) comment '用户调用接口关系表';