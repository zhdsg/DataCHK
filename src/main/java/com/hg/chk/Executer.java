package com.hg.chk;

import com.hg.chk.Utils.JSONUtils;
import com.hg.chk.Utils.PythonUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6/006.
 */
public class Executer {
    public static void main(String[] args){
        Param paramModel =new Param();

        if(args!=null &&args.length != 0)
        {
            paramModel.setRetry(JSONUtils.getInteger(args[0],Constants.PARAM_RETRY));
            paramModel.setSleep(JSONUtils.getInteger(args[0], Constants.PARAM_SLEEP));
            paramModel.setTime(JSONUtils.getInteger(args[0], Constants.PARAM_TIME));
            paramModel.setProjectId(JSONUtils.getString(args[0], Constants.PARAM_PROJECT_ID));
            paramModel.setSourceName(JSONUtils.getString(args[0], Constants.PARAM_SOURCE_NAME));
            paramModel.setSourceType(JSONUtils.getString(args[0], Constants.PARAM_SOURCE_TYPE));
        }
        System.out.println("运行时间为:"+paramModel.getTime()+" ,执行频率为:"+paramModel.getSleep()+", 重试次数为:"+paramModel.getRetry());

        String driver =paramModel.getSourceType()=="oracle"?Configuration.getProperty(Constants.ORACLE_DRIVER):Configuration.getProperty(Constants.RDS_DRIVER);
        JDBCHelper oracle =new JDBCHelper(
                driver,
                PythonUtils.getUrl(Configuration.getProperty(Constants.PYTHON_FILE_PATH),Configuration.getProperty(Constants.PYTHON_FILE_METHOD)),
                PythonUtils.getUser(Configuration.getProperty(Constants.PYTHON_FILE_PATH),Configuration.getProperty(Constants.PYTHON_FILE_METHOD)),
                PythonUtils.getPasswd(Configuration.getProperty(Constants.PYTHON_FILE_PATH),Configuration.getProperty(Constants.PYTHON_FILE_METHOD))
        );

//        JDBCHelper test= new JDBCHelper(
//                    Configuration.getProperty(Constants.TEST_DRIVER),
//                    PythonUtils.getUrl(Configuration.getProperty(Constants.PYTHON_FILE_PATH),Configuration.getProperty(Constants.PYTHON_FILE_METHOD)),
//                    PythonUtils.getUser(Configuration.getProperty(Constants.PYTHON_FILE_PATH), Configuration.getProperty(Constants.PYTHON_FILE_METHOD)),
//                    PythonUtils.getPasswd(Configuration.getProperty(Constants.PYTHON_FILE_PATH), Configuration.getProperty(Constants.PYTHON_FILE_METHOD))
//            );

        long start =System.currentTimeMillis();
        long end =0;
        while(end-start < paramModel.getTime()*60*1000*60){
            String sql ="SELECT * FROM "+Configuration.getProperty(Constants.ORACLE_TABLE);


           // Executer.query(test,sql,paramModel.getRetry());
            Executer.query(oracle,sql,paramModel.getRetry());
            end =System.currentTimeMillis();
            try {
                Thread.sleep(1000*60* paramModel.getSleep());
            } catch (InterruptedException e) {
                System.out.println("频率参数sleep设置错误！！！");
                e.printStackTrace();
            }

        }
        oracle.close();
        System.out.println(new Date()+": 执行时间到，任务完成。。。。");

    }
    public static void query(JDBCHelper helper,String sql ,int retry)  {
        try {
            helper.query(sql, new Object[]{}, new JDBCHelper.CallBack() {

                JDBCHelper rds = new JDBCHelper(
                        Configuration.getProperty(Constants.RDS_DRIVER),
                        Configuration.getProperty(Constants.RDS_URL),
                        Configuration.getProperty(Constants.RDS_USER),
                        Configuration.getProperty(Constants.RDS_PASSWD)
                );

                public void process(ResultSet rs) throws Exception {
                    int colCount = rs.getMetaData().getColumnCount();
                    rds.update("truncate "+ Configuration.getProperty(Constants.RDS_TABLE),new Object[]{});
                    //拼接完成"insert into <table>(column1,column2,...) values(?,?,...);"
                    StringBuffer buffer = new StringBuffer("insert into " + Configuration.getProperty(Constants.RDS_TABLE) + "( ");
                    StringBuffer params = new StringBuffer("(");
                    for (int i = 1; i <= colCount; i++) {
                        buffer.append(rs.getMetaData().getColumnName(i));
                        params.append("?");
                        if (i != colCount) {
                            buffer.append(",");

                            params.append(",");
                        } else {
                            buffer.append(")");
                            params.append(")");
                        }
                    }
                    buffer.append(" values");
                    buffer.append(params);
                    buffer.append(";");

                    //获取字段值，并存储到参数列表中
                    List<Object[]> paramsList = new ArrayList<Object[]>();
                    while (rs.next()) {
                        Object[] obj = new Object[colCount];
                        for (int i = 1; i <= colCount; i++) {
                            obj[i-1] = rs.getString(i);
                        }
                        paramsList.add(obj);

                    }

                    int[] rtn = rds.executeBatch(buffer.toString(), paramsList);
                    System.out.println(new Date()+": 操作执行成功。。。。");
                    rds.close();
                }
            });
        } catch (Exception e) {
            if(retry > 0)
            {
                e.printStackTrace();
                System.out.println(new Date() +":  正在重试。。。。。");
                try {
                    Thread.sleep(5*60*1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                query(helper, sql, retry - 1);
            }else{
                System.exit(-1);
            }
        }
    }


}
