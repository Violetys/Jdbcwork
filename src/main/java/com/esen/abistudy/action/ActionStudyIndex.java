package com.esen.abistudy.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esen.util.JsonUtils;

/**
 * 这里写类注释，说明这个类是干嘛的
 * action的使用参考wiki：pageId=145719346
 *
 * @author admin
 * @since 2018年11月15日
 */
@Controller
@RequestMapping("/abistudy/studyindex")
public class ActionStudyIndex {

	/**
	 * 访问abistudy/studyindex.do进入的方法，
	 * 该方法返回abistudy文件夹下的testindex.ftl文件
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping()
	public String getIndexPage(HttpServletRequest req) {
		//向ftl传数据
		req.setAttribute("title", "列表");
		//调整ftl界面
		return "abistudy/testindex";
	}

	/**
	 * 前台ajax请求，前台可通过访问abistudy/index.do?action=getData进入该方法
	 * 1、@ResponseBody 是ajax请求需要添加的注解
	 * 2、注意  action=refresh 中间不要有空格
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(params = { "action=getData" })
	@ResponseBody
	public String refresh(HttpServletRequest req) {
		return getListDatas();
	}

	/**
	 * 获得列表数据
	 * @return
	 */
	private String getListDatas() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("name", "AAA");
		map1.put("resid", "ES$11$AAA");
		map1.put("jdbcpool", "链接池AAA");
		list.add(map1);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("name", "BBB");
		map2.put("resid", "ES$11$BBB");
		map2.put("jdbcpool", "链接池BBB");
		list.add(map2);
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("name", "CCC");
		map3.put("resid", "ES$11$CCC");
		map3.put("jdbcpool", "链接池CCC");
		list.add(map3);
		return JsonUtils.toJSONString(list);
	}

}
