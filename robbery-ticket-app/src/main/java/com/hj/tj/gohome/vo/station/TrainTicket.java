package com.hj.tj.gohome.vo.station;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author tangj
 * @description
 * @since 2019/2/27 17:37
 */
@Data
public class TrainTicket {
    private String flag;

    private Map<String, Object> map;

    private List<String> result;
}
