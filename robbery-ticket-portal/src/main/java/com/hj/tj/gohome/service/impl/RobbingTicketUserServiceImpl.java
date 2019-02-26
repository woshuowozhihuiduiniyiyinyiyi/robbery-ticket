package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.entity.RobbingTicketUser;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.mapper.RobbingTicketUserMapper;
import com.hj.tj.gohome.service.RobbingTicketUserService;
import com.hj.tj.gohome.utils.StringUtil;
import com.hj.tj.gohome.vo.requestVo.RobbingTicketUserReqObj;
import com.hj.tj.gohome.vo.responseVO.RobbingTicketUserResObj;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RobbingTicketUserServiceImpl implements RobbingTicketUserService {

    @Resource
    private RobbingTicketUserMapper robbingTicketUserMapper;

    @Override
    public List<RobbingTicketUser> listRobbingTicketUser(RobbingTicketUserReqObj robbingTicketUserReqObj) {
        QueryWrapper<RobbingTicketUser> queryWrapper = genQueryWrapper(robbingTicketUserReqObj);

        return robbingTicketUserMapper.selectList(queryWrapper);
    }

    @Override
    public List<RobbingTicketUserResObj> listRobbingTicketUserResObj(RobbingTicketUserReqObj robbingTicketUserReqObj) {
        List<RobbingTicketUser> robbingTicketUsers = this.listRobbingTicketUser(robbingTicketUserReqObj);
        if (CollectionUtils.isEmpty(robbingTicketUsers)) {
            return new ArrayList<>();
        }

        List<RobbingTicketUserResObj> resultList = new ArrayList<>();
        for (RobbingTicketUser robbingTicketUser : robbingTicketUsers) {
            RobbingTicketUserResObj robbingTicketUserResObj = new RobbingTicketUserResObj();
            BeanUtils.copyProperties(robbingTicketUser, robbingTicketUserResObj);

            resultList.add(robbingTicketUserResObj);
        }

        return resultList;
    }

    private QueryWrapper<RobbingTicketUser> genQueryWrapper(RobbingTicketUserReqObj robbingTicketUserReqObj) {
        QueryWrapper<RobbingTicketUser> queryWrapper = new QueryWrapper<>();

        if (StringUtil.isNotBlank(robbingTicketUserReqObj.getName())) {
            queryWrapper.like("name", robbingTicketUserReqObj.getName());
        }

        if (!CollectionUtils.isEmpty(robbingTicketUserReqObj.getRobbingIdList())) {
            queryWrapper.in("id", robbingTicketUserReqObj.getRobbingIdList());
        }

        queryWrapper.eq("status", BaseStatusEnum.UN_DELETE.getValue());

        return queryWrapper;
    }
}
