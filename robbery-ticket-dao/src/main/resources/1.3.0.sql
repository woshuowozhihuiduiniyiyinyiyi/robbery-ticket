-- 1.添加加速地区表
CREATE TABLE `speed_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '地区名称',
  `max_top_num` int(5) NOT NULL DEFAULT '0' COMMENT '最大置顶数',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '业主状态，0已删除，1未删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
);


-- 2.添加加速评论表
CREATE TABLE `speed_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `speed_dynamic_id` int(11) NOT NULL DEFAULT '0' COMMENT '动态id',
  `owner_id` int(11) NOT NULL DEFAULT '0' COMMENT '评论用户id',
  `reply_owner_id` int(11) NOT NULL DEFAULT '0' COMMENT '回复用户id',
  `content` varchar(1000) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '内容',
  `post_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `reply_num` int(10) NOT NULL DEFAULT '0' COMMENT '回复数',
  `praise_num` int(10) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `share_num` int(10) NOT NULL DEFAULT '0' COMMENT '分享数',
  `picture` varchar(1024) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '图片',
  `root_id` int(11) NOT NULL DEFAULT '0' COMMENT '根id，评论的根id为0',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '业主状态，0已删除，1未删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_dynamicId_ownerId_postTime` (`speed_dynamic_id`,`owner_id`,`post_time`) USING BTREE,
  KEY `idx_replyOwnerId_postTime` (`reply_owner_id`,`post_time`) USING BTREE
) ;

-- 3.添加动态表
CREATE TABLE `speed_dynamic` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `speed_area_id` int(11) NOT NULL DEFAULT '0' COMMENT '地区id',
  `has_top` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否置顶，0否1是',
  `top_expire` datetime DEFAULT NULL COMMENT '置顶过期时间',
  `content` varchar(1000) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '内容',
  `post_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `comment_num` int(10) NOT NULL DEFAULT '0' COMMENT '评论数',
  `praise_num` int(10) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `share_num` int(10) NOT NULL DEFAULT '0' COMMENT '分享数',
  `picture` varchar(1024) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '图片',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '业主状态，0已删除，1未删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_speedAreaId_postTime` (`speed_area_id`,`post_time`) USING BTREE,
  KEY `idx_ownerId_postTime` (`owner_id`,`post_time`) USING BTREE
) ;

-- 4.添加数据点赞表
CREATE TABLE `speed_praise` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` int(11) NOT NULL DEFAULT '0' COMMENT '数据id，可能是动态，也可能是评论',
  `data_type` int(11) NOT NULL DEFAULT '0' COMMENT '数据类型，0为动态，1为评论',
  `post_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  `owner_id` int(11) NOT NULL DEFAULT '0' COMMENT '点赞人',
  `praise_owner_id` int(11) NOT NULL DEFAULT '0' COMMENT '被点赞人',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '状态，0已删除，1未删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_dataId_dataType_postTime` (`data_id`,`data_type`,`post_time`) USING BTREE,
  KEY `idx_praiseOwnerId_portTime` (`praise_owner_id`,`post_time`) USING BTREE
) ;


