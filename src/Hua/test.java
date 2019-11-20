package Hua;


import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes.Name;

import org.omg.CORBA.portable.InputStream;
import org.omg.PortableInterceptor.AdapterManagerIdHelper;


public class test {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub	
		  MySqlHelper ms = new MySqlHelper();
		  ms.connect();
		  //第二歩，向user表插入数据
		  ms.inseruser("ly", "123456");
		  ms.inseruser("liming", "345678");
		  ms.inseruser("test", "11111");
		  ms.inseruser("test1", "12345");
		  ms.printftab("person");//打印表
		  //第三步，向person表插入数据，使用可变参数列表，由于其需要特殊操作，故封装成函数以供使用
		  ms.insertperson("ly", "王五", null, null);
		  ms.insertperson("test2", "测试用户2", null,null);
		  ms.insertperson("test1", "测试用户1", 33,null);
		  ms.insertperson("test","张三",23,"1338844993");
		  ms.insertperson("admin", "admin",null,null);
		  ms.printftab("users");//打印表
		  //第四步，模糊查询删除数据
		  ms.executeNonQuery("DELETE FROM users where username like '%test%';" );
		  ms.printftab("users");//打印表
		  //最后，关闭操作不能忘
		  ms.close();
}
	
}
	
	
	
	