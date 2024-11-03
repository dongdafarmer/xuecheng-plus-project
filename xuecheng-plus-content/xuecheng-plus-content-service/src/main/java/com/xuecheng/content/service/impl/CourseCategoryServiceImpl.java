package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        //调用mapper递归查询课程分类
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        //list转Map（排除根结点），方便从map获取结点，key是id，value是CourseCategoryTreeDto
        Map<String, CourseCategoryTreeDto> map = courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId()))
                .collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key1));

        //定义最终用于返回的list
        List<CourseCategoryTreeDto> CourseCategoryList = new ArrayList<>();
        //从头遍历（排除根结点）找到每个结点的子结点，封装成List<CourseCategoryTreeDto>
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item->{
            //将第一级结点加入最终返回的list
            if(id.equals(item.getParentid())) {
                CourseCategoryList.add(item);
            } else {
                //找到父结点，将子结点加入父结点的children
                CourseCategoryTreeDto parent = map.get(item.getParentid());
                if(parent != null) {
                    if(parent.getChildrenTreeNodes() == null) {
                        parent.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                    }
                    parent.getChildrenTreeNodes().add(item);
                }
            }
        });

        return CourseCategoryList;
    }
}
