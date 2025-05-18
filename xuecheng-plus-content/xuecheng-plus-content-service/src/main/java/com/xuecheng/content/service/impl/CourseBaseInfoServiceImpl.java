package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称模糊查询,在sql中拼接 course_base.name like '%值%'
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()),CourseBase::getName,courseParamsDto.getCourseName());
        //根据课程审核状态查询 course_base.audit_status = ?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus,courseParamsDto.getAuditStatus());
        //按课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()), CourseBase::getStatus,courseParamsDto.getPublishStatus());

        //创建page分页参数对象，参数：当前页码，每页记录数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //开始进行分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //数据列表
        List<CourseBase> items = pageResult.getRecords();
        //总记录数
        long total = pageResult.getTotal();

        //List<T> items, long counts, long page, long pageSize
        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(items,total,pageParams.getPageNo(), pageParams.getPageSize());
        return courseBasePageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {

        //向课程基本信息表course_base中插入数据
        CourseBase newCourseBase = new CourseBase();
        BeanUtils.copyProperties(dto, newCourseBase);   //只要属性名称一致就可以拷
        newCourseBase.setCompanyId(companyId);
        newCourseBase.setCreateDate(LocalDateTime.now());
        newCourseBase.setAuditStatus("202002"); //审核状态默认未提交
        newCourseBase.setStatus("202001"); //发布状态默认未发布
        //插入数据库
        int insert = courseBaseMapper.insert(newCourseBase);
        if (insert <= 0) {
            throw new RuntimeException("插入课程基本信息失败");
        }
        //向课程营销信息表course_market中插入数据
        CourseMarket newCourseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, newCourseMarket);
        newCourseMarket.setId(newCourseBase.getId());
        //保存营销信息
        saveCourseMarket(newCourseMarket);

        //从数据库查询课程的详细信息
        return getCourseBaseInfo(newCourseBase.getId());
    }

    //查询课程信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        //查询课程基本信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null) {
            return null;
        }
        //查询课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //组装在一起
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        if(courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }
        //通过courseCatogoryMapper查询课程分类信息
        courseBaseInfoDto.setMtName(courseCategoryMapper.selectById(courseBase.getMt()).getName());
        courseBaseInfoDto.setStName(courseCategoryMapper.selectById(courseBase.getSt()).getName());
        return courseBaseInfoDto;
    }

    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        //数据合法性校验
        //修改前先查询
        CourseBase courseBase = courseBaseMapper.selectById(editCourseDto.getId());
        CourseMarket courseMarket = courseMarketMapper.selectById(editCourseDto.getId());
        if(courseBase == null) {
            throw new XueChengPlusException("课程不存在");
        }
        //本机构只能修改本机构的课程
        if(!companyId.equals(courseBase.getCompanyId())) {
            throw new XueChengPlusException("本机构只能修改本机构的课程");
        }

        //封装数据
        BeanUtils.copyProperties(editCourseDto, courseBase);
        BeanUtils.copyProperties(editCourseDto, courseMarket);
        courseBase.setChangeDate(LocalDateTime.now());

        //更新课程基本信息
        int i = courseBaseMapper.updateById(courseBase);
        if(i <= 0) {
            throw new XueChengPlusException("修改课程基本信息失败");
        }
        //更新课程营销信息
        i = courseMarketMapper.updateById(courseMarket);
        if(i <= 0) {
            throw new XueChengPlusException("修改课程营销信息失败");
        }
        //查询课程信息
        return getCourseBaseInfo(courseBase.getId());

    }

    //单独写一个方法保存营销信息，逻辑：先查询是否存在，如果存在则更新，不存在则插入
    private int saveCourseMarket(CourseMarket newCourseMarket) {
        //参数合法性校验
        String charge = newCourseMarket.getCharge();
        if(StringUtils.isEmpty(charge)){
            throw new RuntimeException("收费规则不能为空");
        }
        //如果收费，但价格为空也报异常
        if(charge.equals("201001")) {
            if(newCourseMarket.getPrice() == null || newCourseMarket.getPrice() <= 0){
                throw new RuntimeException("价格不能为空，且价格必须大于0");
            }
        }

        //从数据库查询课程营销信息，如果存在则更新，不存在则插入
        CourseMarket courseMarket = courseMarketMapper.selectById(newCourseMarket.getId());
        if(courseMarket == null) {
            //插入
            return courseMarketMapper.insert(newCourseMarket);
        } else {
            //更新
            return courseMarketMapper.updateById(newCourseMarket);
        }
    }
}
