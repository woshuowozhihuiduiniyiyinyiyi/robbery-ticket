package com.hj.tj.gohome.controller;

import com.hj.tj.gohome.service.SchoolService;
import com.hj.tj.gohome.vo.school.SchoolResObj;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class SchoolController {

    @Resource
    private SchoolService schoolService;

    @GetMapping("/school/list")
    public ResponseEntity<List<SchoolResObj>> listSchool(@NotBlank(message = "学校查询名称不能为空") String schoolName) {
        List<SchoolResObj> schoolResObjList = schoolService.listSchool(schoolName);

        return ResponseEntity.ok(schoolResObjList);
    }
}
