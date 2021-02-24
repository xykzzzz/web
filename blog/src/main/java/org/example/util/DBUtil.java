package org.example.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.example.exception.AppException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String URL="jdbc:mysql://localhost:3306/servlet_blog1?user=root&password=123456&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    //private static final DataSource ds=new MysqlDataSource();

    /**
     * 工具类 提供数据库jdbc操作
     * 不足：1.static代码块出现异常，NoClassDefFoundError表示类可以找到，但类加载失败
     *      classNotFoundException:找不到类
     *      3.工具类：设计上不是最优的，数据库框架OPM框架，都是模板模式设计Mybatis，都是模板设计
     */

   /* static {
        ((MysqlDataSource)ds).setURL(URL);
    }*/
    private static volatile DataSource ds=null;
    public static DataSource getDataSource(){
        if(ds==null){
            synchronized (DBUtil.class){
                if(ds==null){
                    ds=new MysqlDataSource();
                    ((MysqlDataSource)ds).setURL(URL);
                }
            }
        }
        return ds;
    }
    public static Connection getConnection(){
        try {
            DataSource dataSource = getDataSource();
            return dataSource.getConnection();
        } catch (SQLException e) {
            //抛自定义异常
            throw new AppException("DB001","获取数据库连接失败",e);
        }
    }
    public static void close(Connection c, Statement s, ResultSet r){
        try {
            if(c!=null){
                c.close();
            }
            if(s!=null){
                s.close();
            }
            if(r!=null){
                r.close();
            }
        } catch (SQLException e) {
           throw new AppException("DB002","数据库释放出错",e);
        }
    }
    public static void close(Connection c, Statement s){
        close(c,s,null);
    }
}
