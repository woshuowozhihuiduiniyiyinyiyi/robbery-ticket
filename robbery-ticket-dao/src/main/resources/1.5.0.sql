-- 1.formId 表
CREATE TABLE `wx_form_id` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '微信的appId',
  `form_id` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '微信推送formId',
  `owner_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '对应的业主id',
  `has_use` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0未使用，1已使用',
  `expire_date` datetime DEFAULT NULL COMMENT 'formId过期日期',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '状态，0已删除，1未删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
);

