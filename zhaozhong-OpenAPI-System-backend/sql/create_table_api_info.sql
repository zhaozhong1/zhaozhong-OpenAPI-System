# 创建API信息表
create database if not exists zz_api;
use zz_api;
create table if not exists zz_api.api_info
(
    `id`              bigint       not null auto_increment comment 'id' primary key comment '主键自增',
    `api_name`        varchar(64)  not null unique comment '接口名称',
    `api_description` varchar(512) comment '接口描述',
    `api_url`         varchar(512) not null comment '接口地址',
    `method`          varchar(256) not null comment '方接口法',
    `req_header`      text         not null comment '请求头',
    `resp_header`     text         not null comment '响应头',
    `api_status`      tinyint      not null default 0 comment '接口状态 0-关闭 1-开启',
    `user_id`         bigint       not null comment '创建人',
    `create_time`     datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`     datetime     not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`       tinyint      not null default 0 comment '逻辑删除 0-未删 1-已删'
);
INSERT INTO zz_api.api_info
(`api_name`, `api_description`, `api_url`, `method`, `req_header`, `resp_header`, `api_status`, `user_id`)
VALUES ('Mock API 1', 'This is a mock API for testing 1', 'http://example.com//api/mock1', 'GET', '{"Content-Type": "application/json"}',
        '{"Content-Type": "application/json"}', 1, 1),
       ('Mock API 2', 'This is a mock API for testing 2', 'http://example.com//api/mock2', 'POST', '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/json"}', 0, 2),
       ('Mock API 3', 'This is a mock API for testing 3', 'http://example.com//api/mock3', 'PUT', '{"Content-Type": "application/xml"}',
        '{"Content-Type": "application/xml"}', 1, 1),
       ('Mock API 4', 'This is a mock API for testing 4', 'http://example.com//api/mock4', 'GET', '{"Content-Type": "application/json"}',
        '{"Content-Type": "application/json"}', 1, 2),
       ('Mock API 5', 'This is a mock API for testing 5', 'http://example.com//api/mock5', 'POST', '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/json"}', 0, 1),
       ('Mock API 6', 'This is a mock API for testing 6', 'http://example.com//api/mock6', 'PUT', '{"Content-Type": "application/xml"}',
        '{"Content-Type": "application/xml"}', 1, 2),
       ('Mock API 7', 'This is a mock API for testing 7', 'http://example.com//api/mock7', 'GET', '{"Content-Type": "application/json"}',
        '{"Content-Type": "application/json"}', 1, 1),
       ('Mock API 8', 'This is a mock API for testing 8', 'http://example.com//api/mock8', 'POST', '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/json"}', 0, 2),
       ('Mock API 9', 'This is a mock API for testing 9', 'http://example.com//api/mock9', 'PUT', '{"Content-Type": "application/xml"}',
        '{"Content-Type": "application/xml"}', 1, 1),
       ('Mock API 10', 'This is a mock API for testing 10', 'http://example.com//api/mock10', 'GET',
        '{"Content-Type": "application/json"}', '{"Content-Type": "application/json"}', 1, 2),
       ('Mock API 11', 'This is a mock API for testing 11', 'http://example.com//api/mock11', 'POST', '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/json"}', 0, 1),
       ('Mock API 12', 'This is a mock API for testing 12', 'http://example.com//api/mock12', 'PUT', '{"Content-Type": "application/xml"}',
        '{"Content-Type": "application/xml"}', 1, 2),
       ('Mock API 13', 'This is a mock API for testing 13', 'http://example.com//api/mock13', 'GET',
        '{"Content-Type": "application/json"}', '{"Content-Type": "application/json"}', 1, 1),
       ('Mock API 14', 'This is a mock API for testing 14', 'http://example.com//api/mock14', 'POST', '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/json"}', 0, 2),
       ('Mock API 15', 'This is a mock API for testing 15', 'http://example.com//api/mock15', 'PUT', '{"Content-Type": "application/xml"}',
        '{"Content-Type": "application/xml"}', 1, 1);