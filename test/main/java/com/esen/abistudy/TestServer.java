package com.esen.abistudy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.esen.test.BaseSpringBootTest;
import com.esen.vfs2.Vfs2;

/**
 * 单元测试代码举例，因为Server用到了Spring的注入，所以，测试时需要extends BaseSpringBootTest
 * @author chenlan
 * @since 2018-10-19
 */
public class TestServer extends BaseSpringBootTest {
	@Autowired
	private Server server;

	@Test
	public void test() {
		// Server server = SpringContextHolder.getBean(Server.class);
		Vfs2 vfs = server.getVfs();
		// TODO 
	}

}
