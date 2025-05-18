package com.xuecheng.content.api;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeachplanController {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanService teachplanService;

    //查询课程计划 GET /teachplan/22/tree-nodes
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplanDto) {
        teachplanService.saveTeachplan(teachplanDto);
    }

}
