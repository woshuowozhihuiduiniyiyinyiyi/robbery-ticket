package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.entity.SpeedComment;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.SpeedCommentMapper;
import com.hj.tj.gohome.service.OwnerService;
import com.hj.tj.gohome.service.SpeedCommentService;
import com.hj.tj.gohome.vo.comment.SpeedCommentParam;
import com.hj.tj.gohome.vo.comment.SpeedCommentReplyResult;
import com.hj.tj.gohome.vo.comment.SpeedCommentResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpeedCommentServiceImpl implements SpeedCommentService {

    @Resource
    private SpeedCommentMapper speedCommentMapper;

    @Resource
    private OwnerService ownerService;

    @Override
    public PageInfo<SpeedCommentResult> listSpeedComment(SpeedCommentParam speedCommentParam) {
        QueryWrapper<SpeedComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("speed_dynamic_id", speedCommentParam.getSpeedDynamicId())
                .eq("root_id", 0)
                .eq("status", StatusEnum.UN_DELETE.getStatus());

        PageHelper.startPage(speedCommentParam.getPage().getPage(), speedCommentParam.getPage().getSize(),
                "post_time desc");
        List<SpeedComment> commentList = speedCommentMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(commentList)) {
            return new PageInfo<>();
        }

        PageInfo<SpeedComment> pageInfo = new PageInfo<>(commentList);

        // 回复Map
        List<Integer> commentIds = commentList.stream().map(SpeedComment::getId).collect(Collectors.toList());
        queryWrapper = new QueryWrapper<>();
        queryWrapper.in("root_id", commentIds)
                .eq("status", StatusEnum.UN_DELETE.getStatus());
        List<SpeedComment> replyList = speedCommentMapper.selectList(queryWrapper);
        Map<Integer, List<SpeedComment>> replyMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(replyList)) {
            replyMap = replyList.stream().collect(Collectors.groupingBy(SpeedComment::getRootId));
        }

        List<SpeedComment> allSpeedComments = new ArrayList<>();
        allSpeedComments.addAll(commentList);
        allSpeedComments.addAll(replyList);
        Map<Integer, Owner> ownerMap = getOwnerMap(allSpeedComments);

        List<SpeedCommentResult> resultList = new ArrayList<>();
        for (SpeedComment speedComment : commentList) {
            SpeedCommentResult speedCommentResult = genCommentResult(speedComment, replyMap, ownerMap);
            resultList.add(speedCommentResult);
        }

        PageInfo<SpeedCommentResult> resultPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, resultPageInfo);
        resultPageInfo.setList(resultList);

        return resultPageInfo;
    }

    /**
     * 构建单个结果集
     *
     * @param speedComment 评论列表
     * @param replyMap     回复Map，key 为评论id，value 为回复
     * @param ownerMap     评论和回复人Map
     * @return 单个评论结果集
     */
    private SpeedCommentResult genCommentResult(SpeedComment speedComment,
                                                Map<Integer, List<SpeedComment>> replyMap,
                                                Map<Integer, Owner> ownerMap) {
        SpeedCommentResult commentResult = new SpeedCommentResult();
        BeanUtils.copyProperties(speedComment, commentResult);

        if (StringUtils.isEmpty(speedComment.getPicture())) {
            commentResult.setPictureList(Arrays.asList(speedComment.getPicture().split("`")));
        }

        if (Objects.nonNull(ownerMap.get(commentResult.getOwnerId()))) {
            Owner owner = ownerMap.get(commentResult.getOwnerId());
            commentResult.setAvatarUrl(owner.getAvatarUrl());
            commentResult.setWxNickName(owner.getWxNickname());
        }

        List<SpeedComment> replyList = replyMap.get(commentResult.getId());
        if (CollectionUtils.isEmpty(replyList)) {
            return commentResult;
        }

        List<SpeedCommentReplyResult> replyResultList = genReplyList(ownerMap, replyList);

        commentResult.setReplyResults(replyResultList);
        return commentResult;
    }

    /**
     * 构造回复结果列表
     *
     * @param ownerMap  评论人Map
     * @param replyList 回复列表
     * @return 回复结果列表
     */
    private List<SpeedCommentReplyResult> genReplyList(Map<Integer, Owner> ownerMap, List<SpeedComment> replyList) {
        List<SpeedCommentReplyResult> replyResultList = new ArrayList<>();

        for (SpeedComment replyComment : replyList) {
            SpeedCommentReplyResult speedCommentReplyResult = new SpeedCommentReplyResult();
            BeanUtils.copyProperties(replyComment, speedCommentReplyResult);

            if (!StringUtils.isEmpty(replyComment.getPicture())) {
                speedCommentReplyResult.setPictureList(Arrays.asList(replyComment.getPicture().split("`")));
            }

            Owner owner = ownerMap.get(replyComment.getOwnerId());
            if (Objects.nonNull(owner)) {
                speedCommentReplyResult.setWxNickName(owner.getWxNickname());
            }

            Owner replyOwner = ownerMap.get(replyComment.getReplyOwnerId());
            if (Objects.nonNull(replyOwner)) {
                speedCommentReplyResult.setReplyWxNickName(replyOwner.getWxNickname());
            }

            replyResultList.add(speedCommentReplyResult);
        }

        return replyResultList;
    }

    /**
     * 获取评论人和回复人Map
     *
     * @param speedComments 评论列表
     * @return 评论人和回复人Map
     */
    private Map<Integer, Owner> getOwnerMap(List<SpeedComment> speedComments) {
        List<Integer> ownerIds = speedComments.stream().map(SpeedComment::getOwnerId).collect(Collectors.toList());
        List<Integer> replyOwnerIds = speedComments.stream().map(SpeedComment::getReplyOwnerId).collect(Collectors.toList());
        ownerIds.addAll(replyOwnerIds);
        List<Owner> owners = ownerService.selectByIds(ownerIds);
        Map<Integer, Owner> ownerMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(owners)) {
            ownerMap = owners.stream().collect(Collectors.toMap(Owner::getId, o -> o));
        }

        return ownerMap;
    }

}
