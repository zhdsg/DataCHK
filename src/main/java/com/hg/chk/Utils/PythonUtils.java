package com.hg.chk.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hg.chk.Configuration;
import com.hg.chk.Constants;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * Created by Administrator on 2018/2/6/006.
 */
public class PythonUtils {

    public static String getResult(String path,String method){
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(path);

        PyFunction pyFunction = interpreter.get(method, PyFunction.class); // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyObject pyObject = pyFunction.__call__(); // 调用函数

       // System.out.println(pyObject.toString());
        return pyObject.toString();
    }
    public static String getUrl(String path,String method){
        String json =getResult(path, method);
        return JSONUtils.getString(json, Constants.PYTHON_JSON_URL);
    }
    public static String getUser(String path,String method){
        String json =getResult(path,method);
        return JSONUtils.getString(json,Constants.PYTHON_JSON_USER);
    }
    public static String getPasswd(String path,String method){
        String json =getResult(path,method);
        return JSONUtils.getString(json,Constants.PYTHON_JOSN_PASSWD);
    }
//    public static void main(String[] args){
//        System.out.println(Constants.PYTHON_FILE_PATH);
//        System.out.println(Configuration.getProperty(Constants.PYTHON_FILE_PATH));
//       // System.out.println(PythonUtils.getUrl(Configuration.getProperty(Constants.PYTHON_FILE_PATH),Configuration.getProperty(Constants.PYTHON_FILE_METHOD)));
//    }

}
