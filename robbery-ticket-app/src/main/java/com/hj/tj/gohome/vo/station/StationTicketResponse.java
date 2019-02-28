package com.hj.tj.gohome.vo.station;

import lombok.Data;

/**
 * @author tangj
 * @description
 * @since 2019/2/27 17:37
 */
@Data
public class StationTicketResponse {
    private StationTicket data;

    private Integer httpstatus;

    private String messages;

    private boolean status;
}
