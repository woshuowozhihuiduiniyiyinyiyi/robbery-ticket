package com.hj.tj.gohome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.tj.gohome.entity.SpeedComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface SpeedCommentMapper extends BaseMapper<SpeedComment> {

    @Update("update speed_comment set praise_num = praise_num + 1 where id = #{id}")
    void addPraiseNum(@Param("id") Integer id);

    @Update("update speed_comment set reply_num = reply_num + 1 where id = #{id}")
    void addReplyNum(@Param("id") Integer id);
}
