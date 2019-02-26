-- 1.删除用户表的sid 和过期时间
ALTER TABLE `portal_user`
DROP COLUMN `sid`,
DROP COLUMN `sid_expire`;

