package com.homework.web.controller;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Collection;
import com.homework.web.pojo.History;
import com.homework.web.pojo.User;
import com.homework.web.service.CollectionService;
import com.homework.web.service.HistoryService;
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

@RestController
@RequestMapping("/api/history")
@Api(tags = "共同前缀：/api/collection", description = "CollectionController")
@Slf4j
public class HistoryController {
	@Autowired
	HistoryService historyService;
	@Autowired
	UserService userService;
	@Autowired
	NovelService novelService;

	@PostMapping
	@ApiOperation("新增Collection")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(History history) {
		log.info("新增浏览记录");
		if (history.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (history.getNovel_id() == null) {
			throw new ControllerException("novel_id不可为null");
		} else {
			history.setUser_id(userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId());
			if (historyService.selectByUser_idNovel_id(history.getUser_id(), history.getNovel_id()) != null) {
				return new ResponseObject("200", "操作成功", historyService.insert(history));
			} else {
				return new ResponseObject("200", "操作成功", historyService.insert(history));
			}
		}
	}

	@GetMapping
	@ApiOperation("查询Collection")
	public ResponseObject get(Integer novel_id, Integer user_id) {
		log.info("查询Collection");
		if (novel_id != null && user_id != null) {
			return new ResponseObject("200", "操作成功", historyService.selectByUser_idNovel_id(user_id, novel_id));
		} else if (novel_id != null && user_id == null) {
			return new ResponseObject("200", "操作成功", historyService.selectByNovel_id(novel_id));
		} else if (novel_id == null && user_id != null) {
			return new ResponseObject("200", "操作成功", historyService.selectByUser_id(user_id));
		} else {
			throw new ControllerException("novel_id与user_id不可同时为null");
		}
	}

	@GetMapping("/history")
	@ApiOperation("查询Collection的Novel")
	public ResponseObject getNovel(Integer user_id) {
		log.info("查询Collection的Novel");
		if (user_id == null) {
			throw new ControllerException("user_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", novelService.selectByApproved());
		}
	}



	@GetMapping("/count")
	@ApiOperation("查询Collection")
	public ResponseObject getCount(Integer novel_id) {
		log.info("查询Collection");
		if (novel_id == null) {
			throw new ControllerException("novel_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", historyService.selectByNovel_id(novel_id).size());
		}
	}
}
