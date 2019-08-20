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
 */
public class jdbcwork {
	  //声明连接池工厂 
	private ConnectionFactory ConnectionFactory ;
		
	/**
	 * 获取数据库连接池工厂单例对象
	 * @throws Exception 
	 */
	private synchronized  ConnectionFactory getConnectionFactory() throws Exception {
		if (ConnectionFactory == null) {
			InputStream input = jdbcwork.class.getResourceAsStream("jdbc.conf");
			try {
				Properties p = new Properties();
				try {
					p.load(input);
				} catch (IOException e) {
					throw new RuntimeException("jdbc配置文件读取失败！");
				}
				ConnectionFactory = new PoolConnectionFactory(null, p);
			} finally {
				input.close();
			}

		}
		return ConnectionFactory;
	}
	
	/**
	 * 创建测试用表
   * @throws Exception
   */
	private void createTable(ConnectionFactory connf) {
			//定义表名
			String tableName = "COURSESCORE";
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
		FileInputStream Fis = new FileInputStream(new File("D:\\大文件.txt"));
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
				
		//確保d:\\out.txt 存在
		File file = new File("D:\\esenfile\\CourseScore.txt");
		try {
			FileFunc.ensureExists(file, false, true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		FileOutputStream Fos = new FileOutputStream(file);
		try {
			OutputStreamWriter Osw = new OutputStreamWriter(Fos);
			try {
				BufferedWriter Bw = new BufferedWriter(Osw);
				try {
				//写入头部信息
					Bw.write("+------------------------------------------------------------------------------+");
					Bw.newLine();
					Bw.write(" |学号          |        姓名         |        班级        |        课程        |        成绩        |");
					Bw.newLine();
					Bw.write("+------------------------------------------------------------------------------+");
					Connection conn = null;
					try {
						conn = connf.getConnection();
						String sql = "select xh,xm,bj,kc,cj from " + tableName; 
						Statement stmt = conn.createStatement();
						try {
							ResultSet rs = stmt.executeQuery(sql);
							try {
								while(rs.next()) {
									Bw.newLine();
									//每行的学号信息
									StringBuffer lineXhInfo = new StringBuffer("");
									lineXhInfo.append(" |").append(rs.getString(1)).append("    ");
									Bw.write(lineXhInfo.toString());
									//每行的姓名信息
									StringBuffer lineXmInfo = new StringBuffer("");
									lineXmInfo.append("|        ").append(rs.getString(2));
									if(rs.getString(2).length() == 3) {
										lineXmInfo.append("       |");
									}if(rs.getString(2).length() == 4) {
										lineXmInfo.append("     |");
									}	
									Bw.write(lineXmInfo.toString());
									//每行的班级信息
									StringBuffer lineBjInfo = new StringBuffer("");
									lineBjInfo.append("        ").append(rs.getString(3)).append("        |");
									Bw.write(lineBjInfo.toString());
									//每行的课程信息
									StringBuffer lineKcInfo = new StringBuffer("");
									lineKcInfo.append("        ").append(rs.getString(4)).append("        |");
									Bw.write(lineKcInfo.toString());
									//每行的成绩信息
									StringBuffer lineCjInfo = new StringBuffer("");
									lineCjInfo.append("        ").append(rs.getString(4)).append("        |");
									Bw.write(lineCjInfo.toString());
								}
								Bw.newLine();
								Bw.write("+------------------------------------------------------------------------------+");
							}finally {
								rs.close();
							}
						}finally{
							stmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
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
	public void testWeek01() {
		try {
			ConnectionFactory connf = getConnectionFactory();
			createTable(connf);
			ReadFile(connf);
			WriteFile(connf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	
	
}
