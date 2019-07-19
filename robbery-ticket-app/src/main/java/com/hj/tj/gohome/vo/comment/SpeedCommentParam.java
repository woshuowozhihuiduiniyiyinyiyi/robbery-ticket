package com.hj.tj.gohome.vo.comment;

import com.hj.tj.gohome.utils.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SpeedCommentParam {

    @NotNull(message = "动态id不能为空")
    private Integer speedDynamicId;

    private Page page = new Page();
}
