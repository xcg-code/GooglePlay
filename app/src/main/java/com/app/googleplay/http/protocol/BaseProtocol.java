package com.app.googleplay.http.protocol;

import com.app.googleplay.http.HttpHelper;
import com.app.googleplay.utils.IOUtils;
import com.app.googleplay.utils.StringUtils;
import com.app.googleplay.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 访问网络的基类
 * Created by 14501_000 on 2016/10/16.
 */

public abstract class BaseProtocol<T> {
    //从第index项开始返回20条数据，用于数据分页
    public T getData(int index){
        //首先判断是否有缓存，有的话加载缓存数据
        String result=getCache(index);
        if(StringUtils.isEmpty(result)){
            //没有缓存或缓存失效，请求网络数据
            result=getDataFromServer(index);
        }

        //开始解析数据
        if(result!=null){
            T data=parseData(result);//T为解析后的数据类型
            return data;
        }
        return null;
    }



    private String getDataFromServer(int index) {
        // http://www.itheima.com/home?index=0&name=zhangsan&age=18
        System.out.println("数据请求地址="+HttpHelper.URL+getKey()+index+getParams());
        HttpHelper.HttpResult httpResult=HttpHelper.get(HttpHelper.URL+getKey()+index+getParams());
        if(httpResult!=null){
            String result=httpResult.getString();//获取请求的数据
            //System.out.println("访问数据结果="+result);
            //写缓存
            if(!StringUtils.isEmpty(result)){
                setCache(index,result);
            }
           return result;
        }
        return null;
    }
    //请求网络数据关键词，子类必须实现
    public abstract String getKey() ;

    //请求网络数据参数，子类必须实现
    public abstract String getParams() ;

    //解析JSON网络数据
    protected abstract T parseData(String result);


    //写缓存，存在本地文件中
    //以URL为文件名，以JSON数据为文件内容
    public void setCache(int index,String json){
        //本地应用的缓存文件夹
        File cacheDir= UIUtils.getContext().getCacheDir();
        //生成缓存文件
        String md5=StringUtils.md5(getKey()+index+getParams());//文件名MD5加密
        File cacheFile=new File(cacheDir,md5);
        FileWriter writer=null;
        try {
            writer=new FileWriter(cacheFile);
            //缓存失效截止时间
            long deadLine=System.currentTimeMillis()+30*60*1000;//半小时有效期
            writer.write(deadLine+"\n");//第一行写入缓存时间
            writer.write(json);//写入json
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(writer);
        }
    }

    //读缓存
    // 以url为文件名, 以json为文件内容,保存在本地
    public String getCache(int index){
        //本地应用的缓存文件夹
        File cacheDir= UIUtils.getContext().getCacheDir();
        //生成缓存文件
        String md5=StringUtils.md5(getKey()+index+getParams());
        File cacheFile=new File(cacheDir,md5);
        //判断缓存是否存在
        if(cacheFile.exists()){
            //判断缓存是否有效
            BufferedReader reader=null;
            try {
                reader=new BufferedReader(new FileReader(cacheFile));
                String deadLine=reader.readLine();//读取第一行数据
                long deadTime=Long.parseLong(deadLine);
                if(System.currentTimeMillis()<deadTime){
                    //缓存有效
                    StringBuffer sb=new StringBuffer();
                    String line;
                    while((line=reader.readLine())!=null){
                        sb.append(line);
                    }
                    return sb.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

}
