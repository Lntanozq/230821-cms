package com.briup.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Slideshow;
import com.briup.cms.service.ISlideshowService;
import com.briup.cms.util.Result;
import com.briup.cms.util.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

/**
 * @author briup
 * @program: 230308-CMS
 * @description TODO
 * @create 2023/3/8 15:40
 **/
@Api(tags = "轮播图模块")
@RestController //  @ResponseBody + @Controller
@RequestMapping("/slideshow")
public class SlideshowController {
    @Autowired
    private ISlideshowService slideshowService;

    @ApiOperation(value = "保存或更新轮播图", notes = "slideshow参数包含id值则为更新，不包含i为新增")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Slideshow slideshow){
        slideshowService.saveOrUpdate(slideshow);
        return Result.success("操作成功");
    }

    //Restful风格
//    @GetMapping("/deleteById/{sid}")
    @ApiOperation(value = "按照id删除轮播图", notes = "id必须存在且有效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "轮播图id", required = true,
                dataType = "int", paramType = "path", defaultValue = "1")
    })
    @DeleteMapping("/deleteById/{sid}")
    public Result deleteById(@PathVariable("sid") Integer id) {
        slideshowService.deleteById(id);

        return Result.success("删除成功");
    }

    @ApiOperation(value = "批量删除轮播图",notes = "需要提供多个id值")
    @DeleteMapping("/deleteByIdAll")
    public Result deleteSlideshowInBatch(@RequestParam("ids") List<Integer> ids) {
        System.out.println("ids: " + ids);
        slideshowService.deleteInBatch(ids);

        return Result.success("删除成功");
    }

    @ApiOperation(value = "查询所有可用的轮播图")
    @GetMapping("/queryAllEnable")
    public Result queryAllEnable() {
        List<Slideshow> list = slideshowService.queryAllEnable();

        return Result.success(list);
    }

    //第几页 每页数量 status 描述
    @ApiOperation(value = "根据条件查询轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", dataType = "int", required = true, defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, defaultValue = "4", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态值", paramType = "query"),
            @ApiImplicitParam(name = "desc", value = "描述信息", paramType = "query")
    })
    @GetMapping("/query")
    public Result query(Integer page, Integer pageSize, String status, String desc) {
        IPage<Slideshow> p = slideshowService.query(page, pageSize, status, desc);

        return Result.success(p);
    }

}
