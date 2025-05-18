package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
* @author yihan
* @description 针对表【teachplan(课程计划)】的数据库操作Mapper
* @createDate 2024-10-28 17:15:21
* @Entity com.xuecheng.content.mapper.Teachplan
*/
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    public List<TeachplanDto> selectTreeNodes(Long courseId);

}




