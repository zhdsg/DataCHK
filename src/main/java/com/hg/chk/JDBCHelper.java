package com.hg.chk;

import java.sql.*;
import java.util.List;


/**
 * Created by Administrator on 2018/2/6/006.
 */
public class JDBCHelper {

    Connection conn;

    public JDBCHelper(String driverName,String url,String user,String passwd)  {



        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public int update(String sql ,Object[] params) throws SQLException {
        PreparedStatement pst=null;
        int result =0;

            pst =conn.prepareStatement(sql);
            for(int i = 0 ; i < params.length; i++){
                pst.setObject(i + 1,params[i]);
            }
             result = pst.executeUpdate();



        return result;
    }

    public void query(String sql, Object[] params,CallBack callBack) throws Exception{
        ResultSet rs =null;
        PreparedStatement pst =null;

            pst=conn.prepareStatement(sql);
            for(int i = 0 ; i < params.length ; i++){
                pst.setObject(i + 1 ,params[i]);
            }
            rs =pst.executeQuery();
            callBack.process(rs);


    }
    public int[] executeBatch(String sql ,List<Object[]> paramsList) throws SQLException {
        PreparedStatement pst =null;
        int[] rtn =null;


            pst=conn.prepareStatement(sql);
            for(Object[] objs :paramsList){
                for(int i =0 ;i<objs.length;i++){
                    pst.setObject(i+1,objs[i]);
                }
                pst.addBatch();
            }
            pst.executeBatch();


        return rtn;
    }
    public void close(){

        try {
            if(conn !=null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public interface  CallBack{
        public void process(ResultSet rs) throws Exception;
    }
}
