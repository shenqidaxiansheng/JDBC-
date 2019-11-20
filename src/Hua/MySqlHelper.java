package Hua;

import java.awt.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;import org.omg.PortableInterceptor.AdapterManagerIdHelper;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.x.protobuf.MysqlxConnection.Close;
import com.mysql.cj.xdevapi.PreparableStatement;

/**封装sql操作类，自动连接数据库
 * @author 123456
 *
 */
public class MySqlHelper {
	private  String driver;
	private  String url;
	private  String userName;
	private  String password;
	private  Connection conn;
	private  Statement st;
	private  PreparedStatement pst;
	private  ResultSet rs;

	// 静态块，当首次访问这个类的静态数据成员或者首次生成这个类的对象静态数据成员时，自动执行且仅执行一次
	// 加载驱动
	public void connect(){
		try {
			Properties prop = new Properties();
			prop.load(MySqlHelper.class.getResourceAsStream("/jdconfig.properties"));
			// 配置文件读取用户名密码
			// 读取配置文件中的用户信息
			url = prop.getProperty("URL");
			driver = prop.getProperty("DRIVER");
			userName = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			// 加载驱动
			Class.forName(driver);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void printftab(String tabname) {
		try {
		String sql = "select *from " + tabname;
		ResultSet result = executeQuery(sql);
		if(tabname == "person")
		{
			while(result.next()){
				System.out.println(result.getString("username") + " | "
			+result.getString("name")+" | "+result.getString("age")+" | "
						+result.getString("teleno"));
			}
		}
		else {
			while(result.next()){
				System.out.println(result.getString("username") + " | "
			+result.getString("pass"));
			}
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 创建连接对象，可在类外使用
	public  Connection getConnection() {
		try {
			if (conn != null) {
				return conn;
			}
			conn = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	// 创建SQL执行语句对象
	public Statement getStatement()  {// 先抛出异常，不用catch捕抓和处理

		try {
			if (st != null) {
				return st;
			}
			// 如果没有statement对象，则需要创建，但是要先验证有没有连接数据库，没有则先连接。
			if (conn == null) {
				getConnection();
			}
			st = conn.createStatement();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st;
	}

	// 创建可以对SQL预编译的执行语句对象，防止SQL注入攻击
	public PreparedStatement getPreparedStatement(String sql) {// 先抛出异常，不用catch捕抓和处理

		try {
			if (pst != null) {
				return pst;
			}
			// 如果没有statement对象，则需要创建，但是要先验证有没有连接数据库，没有则先连接。
			if (conn == null) {
				getConnection();
			}
			pst = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pst;
	}

	/**
	 * alt+shift +j 可以快速注释 执行增删改的操作，使用可变参数列表
	 * 
	 * @param sql
	 * @param parameters
	 * @throws SQLException
	 */
	/**
	 * @param sql
	 * @param parameters
	 */
	public void executeNonQuery(String sql, Object... parameters) {// 可变参数列表
		try {
			if (parameters.length <= 0) {
				getConnection();
				getStatement();
				st.execute(sql);
			} else {
				getConnection();
				getPreparedStatement(sql);
				for (int i = 0; i < parameters.length; i++) {
		//			System.out.println(i+","+parameters.length+","+parameters[i+1].toString());
					pst.setObject(i + 1, parameters[i]);// 接受可变列表的参数
				}
				pst.execute();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			System.out.println("sql语句："+sql+" 执行失败");
			System.out.println("原因：数据或者表已经存在！");
		}

		
	}
	

	/**
	 * 执行查询操作
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql, Object... parameters)  {
		try {
			if (parameters.length <= 0) {
				getConnection();
				getStatement();
				rs = st.executeQuery(sql);
			} else {
				getConnection();
				getPreparedStatement(sql);
				for (int i = 0; i < parameters.length; i++) {
					pst.setObject(i + 1, parameters[i]);// 接受可变列表的参数
				}
				rs = pst.executeQuery();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("查找失败");
			e.printStackTrace();
		}

		return rs;
	}
	public   void insertperson(String username,String name,Object... parameters) throws SQLException {
		MySqlHelper ms  = new MySqlHelper();
		ms.connect();
		//先查询是否有，有则更新，没有则先向user表插入
		ResultSet result = ms.executeQuery("select *from person where username = '"+username+"';");
		if(result.next()) {
			ms.executeNonQuery(
				//	"UPDATE  person SET  name= '"+name+"',age='"+parameters[0]+"',"
				//			+ "teleno='"+parameters[1]+"' where username = '"+username+"';");
					"UPDATE  person SET   age=?, teleno=?  where username = '"+username+"';",
					parameters[0],parameters[1]);
		}
		else {
			inseruser(username, "888888");
			ms.executeNonQuery(
			"insert into person values(?,?,?,?);",username,name,parameters[0],parameters[1]);
		}
		ms.close();
	}
	public  void inseruser(String username,String pass) throws SQLException{
		MySqlHelper ms  = new MySqlHelper();
		ms.connect();
		//先查询是否有，有则更新，没有则先插入
		ResultSet result = ms.executeQuery(
				"select *from users where username = '"+username+"';");
		if(result.next()){
			ms.executeNonQuery(
					"UPDATE  users SET  pass='"+pass+"' where username = '"+username+"';");
		}
		else {
			ms.executeNonQuery(
					"insert into users values('"+username+"','"+pass+"');");
		}	
		ms.close();
	}

	/**
	 * 关闭操作
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (st != null) {
			st.close();
		}
		if (pst != null) {
			pst.close();
		}
		if (conn != null) {
			conn.close();
		}
	}
	
}
