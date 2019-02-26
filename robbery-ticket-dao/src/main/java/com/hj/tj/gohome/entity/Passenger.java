package com.hj.tj.gohome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("passenger")
public class Passenger {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 旅客身份证号、港澳通行证号、台湾通行证号、护照等
     */
    private String idCard;

    /**
     * 旅客名称
     */
    private String name;

    /**
     * 证件类型，0身份证，1港澳通行证，2台湾护照，3护照
     */
    private Integer idCardType;

    /**
     * 旅客类型，0成人，1学生，2伤残军人
     */
    private Integer type;

    /**
     * 0已删除，1未删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String updater;
}
