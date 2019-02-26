package com.hj.tj.gohome.service;

import com.hj.tj.gohome.entity.RobbingTicketUser;
import com.hj.tj.gohome.vo.requestVo.RobbingTicketUserReqObj;
import com.hj.tj.gohome.vo.responseVO.RobbingTicketUserResObj;

import java.util.List;

public interface RobbingTicketUserService {

    List<RobbingTicketUser> listRobbingTicketUser(RobbingTicketUserReqObj robbingTicketUserReqObj);

    List<RobbingTicketUserResObj> listRobbingTicketUserResObj(RobbingTicketUserReqObj robbingTicketUserReqObj);
}
