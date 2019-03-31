package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.hj.tj.gohome.entity.School;
import com.hj.tj.gohome.mapper.SchoolMapper;
import com.hj.tj.gohome.service.SchoolService;
import com.hj.tj.gohome.vo.school.SchoolResObj;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Resource
    private SchoolMapper schoolMapper;

    @Override
    public List<SchoolResObj> listSchool(String name) {
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("school_name", name);
        PageHelper.startPage(1, 5);

        List<School> schools = schoolMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(schools)) {
            return new ArrayList<>();
        }

        List<SchoolResObj> schoolResObjs = new ArrayList<>(schools.size());
        for(School school: schools){
            SchoolResObj schoolResObj = new SchoolResObj();
            BeanUtils.copyProperties(school, schoolResObj);
            schoolResObjs.add(schoolResObj);
        }

        return schoolResObjs;
    }
}
