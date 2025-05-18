package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

public interface CourseBaseInfoService {

    /**
     * 课程分页查询
     * @param pageParams 分页查询参数
     * @param queryCourseParamsDto 查询条件
     * @return 查询结果
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * 新增课程
     * @param companyId 机构id
     * @param addCourseDto 课程信息
     * @return 添加成功的课程详细信息
     */
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * 根据课程id查询课程信息
     * @param courseId 课程id
     * @return
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    /**
     * 修改课程
     * @param CompanyId 机构id
     * @param editCourseDto 课程信息
     * @return 修改后的课程信息
     */
    CourseBaseInfoDto updateCourseBase(Long CompanyId, EditCourseDto editCourseDto);
}
