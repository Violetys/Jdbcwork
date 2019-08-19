package com.esen.abistudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esen.ecore.vfs.VfsCreateFactory;
import com.esen.jdbc.ConnectFactoryManager;
import com.esen.jdbc.ConnectionFactory;
import com.esen.vfs2.Vfs2;
import com.esen.vfs2.VfsOperator;
import com.esen.vfs2.impl.VfsOperatorImpl;

/**
 * 通过该对象获取数据库连接池工厂和VFS实例
 * 获取方式：
 * 方式一：
 * private Server server = SpringContextHolder.getBean(Server.class);
 * 
 * 方式二：
 * 	@Autowired
 * private Server server;
 * 但用autowired注入方式的类，一定不能是new出来的，否则不能用此方式。
 * 
 * @author xiongys
 * @since 2019年8月2日
 */
@Component
public class Server {
	/**
	 * 可以访问多个连接池的接口
	 */
	@Autowired
	private ConnectFactoryManager cfm;

	/**
	 * 创建Vfs2实例的类,可以通过创建此类的一个对象来创建Vfs2对象
	 */
	@Autowired
	private VfsCreateFactory vfsFactory;

	/**
	 * vfs操作者
	 */
	private VfsOperator admin = null;

	/**
	 * 获取Admin身份的操作者
	 * @return vfs的admin身份操作者
	 */
	public final synchronized  VfsOperator getVfsOperatorAsAdmin() {
		if (admin == null) {
			admin = new VfsOperatorImpl("admin", true);
		}
		return admin;
	}

	/**
	 * @return VFS实例
	 */
	public Vfs2 getVfs() {
		return vfsFactory.getVfs();
	}

	/**
	 * @return 连接池工厂
	 */
	public ConnectionFactory getConnectionFactory() {
		return cfm.getDefaultConnectionFactory();
	}
}
