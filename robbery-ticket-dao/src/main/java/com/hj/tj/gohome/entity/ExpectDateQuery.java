package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author tangj
 * @description
 * @since 2019/1/30 15:07
 */
@Data
public class ExpectDateQuery {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 出发时间
     */
    private Date expectDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改人
     */
    private String updater;

    /**
     * 修改时间
     */
    private Date updatedAt;

}
