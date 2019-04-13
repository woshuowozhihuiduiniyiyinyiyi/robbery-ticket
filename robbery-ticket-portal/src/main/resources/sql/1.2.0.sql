-- 1.创建学生旅客表
CREATE TABLE `passenger_student` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `passenger_id` int(11) NOT NULL COMMENT '旅客表id',
  `school_id` int(11) NOT NULL DEFAULT '0' COMMENT '学校id',
  `school_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '学校名称',
  `student_no` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '学号',
  `educational_system` int(4) NOT NULL DEFAULT '0' COMMENT '学制',
  `enter_year` int(5) NOT NULL COMMENT '入学年份',
  `discount_start` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '优惠段始',
  `discount_end` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '优惠段终',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '修改人',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '旅客状态，0已删除，1未删除',
  PRIMARY KEY (`id`),
  KEY `idx_passengerId` (`passenger_id`) USING BTREE
) ;


-- 2.新建学校表
CREATE TABLE `school` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `school_code` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '学校编码',
  `school_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '学校名称 ',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '修改人',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '旅客状态，0已删除，1未删除',
  PRIMARY KEY (`id`)
) ;




