package com.esen.abistudy.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import com.esen.jdbc.ConnectionFactory;
import com.esen.jdbc.PoolConnectionFactory;
import com.esen.jdbc.dialect.DbDefiner;
import com.esen.util.FileFunc;


/**
 * @author xiongys
 * @since 2019年8月20日
 * jar包内容在jdbc和util项目中可见-.-
 */
public class jdbcwork {
	
	
	  //声明数据库连接池工厂 
	private ConnectionFactory Connf ;
		
	/**
	 * 获得数据库连接池工厂
	 * @throws IOException 
	 * @throws Exception 
	 */
	private  ConnectionFactory getConnectionFactory(String path) throws IOException {
		if (Connf == null) {
			//加载jdbc资源配置文件
			InputStream Is = jdbcwork.class.getResourceAsStream(path);
			try {
				Properties p = new Properties();
				//加载连接池属性
				p.load(Is);
				
				/**PoolConnnectionFactory实现ConnectionFactory
				 * 构造函数
				 * 根据属性列表props构造连接池；
				 * @param poolname 
				 *        连接池的名字，可以传null;
				 * @param props
				 *        连接池的属性集合；
				 */
				Connf = new PoolConnectionFactory(null, p);
				
			} finally {
				Is.close();
			}

		}
		return Connf;
	}
	
	
	/**
	 * 创建CourseScore表
   * @throws Exception
   */
	private void createTable(ConnectionFactory connf) {
			//定义表名
			String tableName = "COURSESCORE";
			/**
		   * 获得一个数据定义接口
		   * @return
		   */
			DbDefiner df = connf.getDbDefiner();
			
			//定义字段
			df.defineStringField("Xh",  20, null, false, false);
			df.defineStringField("Xm", 20, null, false, false);
			df.defineStringField("Bj", 20, null, false, false);
			df.defineStringField("Kc", 20, null, false, false);
			df.defineIntField("Cj", 12, null, false, false);
			
			try {
				Connection conn = connf.getConnection();
				try {
					//删除再创建  
					df.dropTable(conn, null, tableName);
					df.createTable(conn, null, tableName);
					
					//直接创建,自动更名
					df.createTable(conn, tableName, false);
				} finally {
					 if(conn!=null)
			        conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	/**
	 * 读文件方法
	 * @param cf
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private void ReadFile(ConnectionFactory connf) throws IOException, SQLException{
		//定义表名
		String tableName = "COURSESCORE";
		//资源释放规范
		FileInputStream Fis = new FileInputStream(new File("D:\\esendev\\gitrepos4.8\\abistudy\\file\\大文件.txt"));
		try {
			InputStreamReader Isr = new InputStreamReader(Fis);
			try {
				BufferedReader Br = new BufferedReader(Isr);
				try {
					//获取数据库连接
					Connection conn = connf.getConnection();
					try {
						String sql = "insert into " + tableName + " (xh,xm,bj,kc,cj) values (?,?,?,?,?)";
						//使用PreparedStatement,预编译和防止sql注入
						PreparedStatement pstmt = conn.prepareStatement(sql);
						try {
							String msg = null;
							//分批插入信息
							while ((msg = Br.readLine()) != null) {
								//按","分割文件行内容，存入数组
								String lmsg[] = msg.split(",");
								
								pstmt.setString(1, lmsg[0]);
								pstmt.setString(2, lmsg[1]);
								pstmt.setString(3, lmsg[2]);
								pstmt.setString(4, lmsg[3]);
								pstmt.setInt(5, Integer.parseInt(lmsg[4]));
								
								//使用addbatch批量执行
								pstmt.addBatch();
							}
							pstmt.executeBatch();
						}finally {
							pstmt.close();
						}
					}finally {
						conn.close();
					}	
				}finally {
					Br.close();
				}
			}finally {
				Isr.close();
			}			
		} finally {
			Fis.close();			
		}		
	}
	
	
	
	/**
	 * 写文件方法
	 * @param cf 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private void WriteFile(ConnectionFactory connf) throws IOException, SQLException {
		//定义表名
		String tableName = "COURSESCORE";
				
		//设置文件路径
		File path = new File("D:\\esenfile\\CourseScore.txt");
		try {
			//esen.Util.FileFunc工具类
			/**
		   * 判断文件或目录是否存在，如果不存在则创建它，如果创建不成功，则触发异常
		   * 如果已存在，但不是isdir并且isoverwrite=false时，触发异常
		   * @param path 
		   * @param isdir 是否是目录
		   * @param isoverwrite 当文件存在并与isdir不一致时是否覆盖
		   * @throws Exception 
		   * 
		   * 在文件创建不成功时抛出的异常要带上路径,否则无法知道是哪个文件创建不成功
		   */
			FileFunc.ensureExists(path, false, true);
						
		} catch (Exception e1) {
		// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		FileOutputStream Fos = new FileOutputStream(path);
		try {
			OutputStreamWriter Osw = new OutputStreamWriter(Fos);
			try {
				BufferedWriter Bw = new BufferedWriter(Osw);
				try {
				//写入头部信息
					Bw.write("+----------------------------------------------------------------------------------------------+");
					Bw.newLine();
					Bw.write("|   学号       |       姓名       |        班级        |       课程         |        成绩     |");
					Bw.newLine();
					Bw.write("+----------------------------------------------------------------------------------------------+");

					Connection conn = connf.getConnection();
						try {
						String sql = "select xh ,xm ,bj ,kc ,cj from " + tableName; 
						Statement stmt = conn.createStatement();
						try {
							ResultSet rs = stmt.executeQuery(sql);
							try {
								while(rs.next()) {
									Bw.newLine();
									//使用stringbuffer连接字符串
									StringBuffer Sb = new StringBuffer("| ");
									Sb.append(rs.getString(1)).append("      ");
									//判断姓名字符串长度，规范格式
									if(rs.getString(2).length() == 3) {
										Sb.append("|     ").append(rs.getString(2)).append("     |");
									}if(rs.getString(2).length() == 4) {
										Sb.append("|     ").append(rs.getString(2)).append("   |");
									}															
									Sb.append("        ").append(rs.getString(3)).append("        |");									
									Sb.append("        ").append(rs.getString(4)).append("        |");
									//判断成绩字符串长度，规范格式
									if(rs.getString(5).length() == 1) {
										Sb.append("        ").append(rs.getString(5)).append("          |");
									}if(rs.getString(5).length() == 2) {
										Sb.append("        ").append(rs.getString(5)).append("        |");
									}if(rs.getString(5).length() == 3) {
										Sb.append("        ").append(rs.getString(5)).append("      |");
									}
									//写入文件
									Bw.write(Sb.toString());
								}
								Bw.newLine();
								Bw.write("+----------------------------------------------------------------------------------------------+");
							}finally {
								rs.close();
							}
						}finally{
							stmt.close();
						}
					}finally {						
							conn.close();						
					}					
					}finally {
						Bw.close();
				}
			}finally {
				Osw.close();
			}
		}finally {
			Fos.close();
		}
	}
	
	
	/**
	 *main
	 */
	@Test
	public void main() {
		try {
			//项目jdbc.conf路径
			String path="jdbc.conf";
			ConnectionFactory connf = getConnectionFactory(path);
			createTable(connf);
			ReadFile(connf);
			WriteFile(connf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
