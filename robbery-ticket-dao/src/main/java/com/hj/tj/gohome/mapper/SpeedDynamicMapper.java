package com.hj.tj.gohome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.tj.gohome.entity.SpeedDynamic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface SpeedDynamicMapper extends BaseMapper<SpeedDynamic> {

    @Update("update speed_dynamic set praise_num = praise_num + 1 where id = #{id}")
    void addPraiseNum(@Param("id") Integer id);

    @Update("update speed_dynamic set comment_num = comment_num + 1 where id = #{id}")
    void addCommentNum(@Param("id") Integer id);
}
