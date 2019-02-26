package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author tangj
 * @since 2019/1/30 15:10
 */
@Data
public class OrderExpectDateQuery {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 查询日期表id
     */
    private Integer expectDateQueryId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 状态
     */
    private Integer status;

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
