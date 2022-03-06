package com.homework.web.controller;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Category;
import com.homework.web.pojo.Collection;
import com.homework.web.pojo.Novel;
import com.homework.web.pojo.User;
import com.homework.web.service.NovelService;
import com.homework.web.service.UserService;
import com.homework.web.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/approve")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
@Api(tags = "共同前缀：/api/approve", description = "ApproveController")
public class ApproveController {

    @Autowired
    NovelService novelService;
    @Autowired
    UserService userService;

    @GetMapping("/getUnapproved")
    @ApiOperation("查询没有审核的Novel")
    public ResponseObject getNovel() {
        log.info("查询Collection的Novel");
            return new ResponseObject("200", "操作成功", novelService.selectByApproved());
        }


    @ApiOperation("删除小说")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public ResponseObject delete(Integer novel_id, Integer user_id) {
        log.info("删除Collection");
        if (novel_id == null) {
            throw new ControllerException("novel_id不可为null");
        } else if (user_id == null) {
            throw new ControllerException("user_id不可为null");
        } else {
            Novel novel = novelService.selectById(novel_id);
            if (novel == null) {
                throw new ControllerException("无效的id");
            } else {
                User user = userService.selectByUsername(
                        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                if (user.getRole().equals("ADMIN")) {
                    novelService.deleteById(novel.getId());
                    return new ResponseObject("200", "操作成功", null);
                } else {
                    throw new ControllerException("该用户无权限删除");
                }
            }
        }
    }


    @ApiOperation("审核通过")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    public ResponseObject approve(Integer novel_id) {
        log.info("删除Collection");
        if (novel_id == null) {
            throw new ControllerException("novel_id不可为null");
        } else {
            Novel novel = novelService.selectById(novel_id);
            if (novel == null) {
                throw new ControllerException("无效的id");
            } else {
                User user = userService.selectByUsername(
                        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                if (user.getRole().equals("ADMIN")) {
                    novel.setApproved(true);
                    novelService.update(novel);
                    return new ResponseObject("200", "操作成功", null);
                } else {
                    throw new ControllerException("该用户无权限审核");
                }
            }
        }
    }
}
