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
		  //�ڶ��i����user���������
		  ms.inseruser("ly", "123456");
		  ms.inseruser("liming", "345678");
		  ms.inseruser("test", "11111");
		  ms.inseruser("test1", "12345");
		  ms.printftab("person");//��ӡ��
		  //����������person��������ݣ�ʹ�ÿɱ�����б���������Ҫ����������ʷ�װ�ɺ����Թ�ʹ��
		  ms.insertperson("ly", "����", null, null);
		  ms.insertperson("test2", "�����û�2", null,null);
		  ms.insertperson("test1", "�����û�1", 33,null);
		  ms.insertperson("test","����",23,"1338844993");
		  ms.insertperson("admin", "admin",null,null);
		  ms.printftab("users");//��ӡ��
		  //���Ĳ���ģ����ѯɾ������
		  ms.executeNonQuery("DELETE FROM users where username like '%test%';" );
		  ms.printftab("users");//��ӡ��
		  //��󣬹رղ���������
		  ms.close();
}
	
}
	
	
	
	