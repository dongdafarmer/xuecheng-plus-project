package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

/**
* @author yihan
* @description 针对表【course_category(课程分类)】的数据库操作Mapper
* @createDate 2024-10-28 17:15:21
* @Entity com.xuecheng.content.mapper.CourseCategory
*/
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    public List<CourseCategoryTreeDto> selectTreeNodes(String id);
}




