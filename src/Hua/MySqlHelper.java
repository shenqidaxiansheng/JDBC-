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

/**��װsql�����࣬�Զ��������ݿ�
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

	// ��̬�飬���״η��������ľ�̬���ݳ�Ա�����״����������Ķ���̬���ݳ�Աʱ���Զ�ִ���ҽ�ִ��һ��
	// ��������
	public void connect(){
		try {
			Properties prop = new Properties();
			prop.load(MySqlHelper.class.getResourceAsStream("/jdconfig.properties"));
			// �����ļ���ȡ�û�������
			// ��ȡ�����ļ��е��û���Ϣ
			url = prop.getProperty("URL");
			driver = prop.getProperty("DRIVER");
			userName = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			// ��������
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

	// �������Ӷ��󣬿�������ʹ��
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

	// ����SQLִ��������
	public Statement getStatement()  {// ���׳��쳣������catch��ץ�ʹ���

		try {
			if (st != null) {
				return st;
			}
			// ���û��statement��������Ҫ����������Ҫ����֤��û���������ݿ⣬û���������ӡ�
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

	// �������Զ�SQLԤ�����ִ�������󣬷�ֹSQLע�빥��
	public PreparedStatement getPreparedStatement(String sql) {// ���׳��쳣������catch��ץ�ʹ���

		try {
			if (pst != null) {
				return pst;
			}
			// ���û��statement��������Ҫ����������Ҫ����֤��û���������ݿ⣬û���������ӡ�
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
	 * alt+shift +j ���Կ���ע�� ִ����ɾ�ĵĲ�����ʹ�ÿɱ�����б�
	 * 
	 * @param sql
	 * @param parameters
	 * @throws SQLException
	 */
	/**
	 * @param sql
	 * @param parameters
	 */
	public void executeNonQuery(String sql, Object... parameters) {// �ɱ�����б�
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
					pst.setObject(i + 1, parameters[i]);// ���ܿɱ��б�Ĳ���
				}
				pst.execute();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			System.out.println("sql��䣺"+sql+" ִ��ʧ��");
			System.out.println("ԭ�����ݻ��߱��Ѿ����ڣ�");
		}

		
	}
	

	/**
	 * ִ�в�ѯ����
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
					pst.setObject(i + 1, parameters[i]);// ���ܿɱ��б�Ĳ���
				}
				rs = pst.executeQuery();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("����ʧ��");
			e.printStackTrace();
		}

		return rs;
	}
	public   void insertperson(String username,String name,Object... parameters) throws SQLException {
		MySqlHelper ms  = new MySqlHelper();
		ms.connect();
		//�Ȳ�ѯ�Ƿ��У�������£�û��������user�����
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
		//�Ȳ�ѯ�Ƿ��У�������£�û�����Ȳ���
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
	 * �رղ���
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
