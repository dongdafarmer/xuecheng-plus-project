package com.xuecheng.content;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 9:24
 */
@SpringBootTest
public class CourseCategotyServiceTests {

    @Autowired
    CourseCategoryService courseCategoryService;

    @Test
    public void testCourseBaseInfoService() {
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryTreeDtos);
    }
}
