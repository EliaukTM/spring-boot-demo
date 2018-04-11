package com.xiamu.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xiamu.bean.Response;
import com.xiamu.dao.RecommendDao;
import com.xiamu.entity.tables.records.RecommendRecord;
import com.xiamu.util.Times;

import io.swagger.annotations.ApiOperation;

/**
 * Description:
 *
 * @author haoyuan.yang
 * @version 1.0
 * @date: 2017/3/11
 * @since JDK 1.8
 */
@RestController
@Api("弹幕")
@RequestMapping("/yuanma")
public class RecommendController {

    @Autowired
	private RecommendDao recommendDao;

	@PostMapping("/save")
	@ApiOperation(value = "保存弹幕")
	public Response getAllUser(@RequestParam String name, @RequestParam String recommend) {
		RecommendRecord record = recommendDao.newRecord();
		record.setName(name);
		record.setRecommend(recommend);
		record.setCreateTime(Times.nowUnixTime());
		recommendDao.insert(record);
		return Response.ok();
	}

}
