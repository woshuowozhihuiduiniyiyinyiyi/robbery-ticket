package com.hj.tj.gohome.service;

import com.hj.tj.gohome.vo.school.SchoolResObj;

import java.util.List;

public interface SchoolService {

    /**
     * 获取学校列表，每次5条
     *
     * @param name
     * @return
     */
    List<SchoolResObj> listSchool(String name);

}
