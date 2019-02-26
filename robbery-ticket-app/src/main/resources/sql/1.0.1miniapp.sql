# 1.修改业主添加相关微信字段
ALTER TABLE `owner`
ADD COLUMN `province`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '省' AFTER `wx_nickname`,
ADD COLUMN `city`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '市' AFTER `province`,
ADD COLUMN `avatar_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像' AFTER `city`,
ADD COLUMN `open_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '微信的open_id' AFTER `avatar_url`;

#2.旅客表添加旅客类型
ALTER TABLE `passenger`
MODIFY COLUMN `id_card`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '旅客身份证号、港澳通行证号、台湾通行证号、护照等' AFTER `id`,
ADD COLUMN `type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '旅客类型，0成人，1学生，2伤残军人' AFTER `name`,
ADD COLUMN `id_card_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '证件类型，0身份证，1港澳通行证，2台湾护照，3护照' AFTER `name`;



#3.业主旅客关联表
CREATE TABLE `rel_owner_passenger` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL COMMENT '业主id',
  `passenger_id` int(11) NOT NULL COMMENT '旅客id',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '旅客状态，0未删除，1已删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#4.所有表加上创建人和修改人
ALTER TABLE `order`
ADD COLUMN `creator`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `status`,
ADD COLUMN `updater`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `created_at`;

ALTER TABLE `owner`
ADD COLUMN `creator`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `updated_at`,
ADD COLUMN `updater`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `creator`;

ALTER TABLE `passenger`
ADD COLUMN `creator`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `updated_at`,
ADD COLUMN `updater`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `creator`;

ALTER TABLE `portal_user`
ADD COLUMN `creator`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `updated_at`,
ADD COLUMN `updater`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `creator`;

ALTER TABLE `rel_owner_passenger`
ADD COLUMN `creator`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `updated_at`,
ADD COLUMN `updater`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `creator`;

ALTER TABLE `rel_passenger_order`
ADD COLUMN `creator`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `updated_at`,
ADD COLUMN `updater`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `creator`;

ALTER TABLE `robbing_ticket_user`
ADD COLUMN `creator`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `updated_at`,
ADD COLUMN `updater`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `creator`;


#5.修改订单表
ALTER TABLE `order`
ADD COLUMN `expect_date`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户预定日期' AFTER `destination`,
ADD COLUMN `phone`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '订单联系手机号' AFTER `robbing_ticket_user_id`,
ADD COLUMN `can_buy_ticket_later`  char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N' COMMENT '是否接受上车补票' AFTER `phone`,
MODIFY COLUMN `departure_date`  datetime NULL COMMENT '出发日期' AFTER `expect_date`;





