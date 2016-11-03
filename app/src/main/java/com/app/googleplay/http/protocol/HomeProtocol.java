package com.app.googleplay.http.protocol;

import com.app.googleplay.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/16.
 */

public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {

    private ArrayList<String> picturesUrl;

    @Override
    public String getKey() {
        return "app/homelist";
    }

    @Override
    public String getParams() {
        return "";//没有参数则为空串
    }

    @Override
    public ArrayList<AppInfo> parseData(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            //解析应用列表数据
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            ArrayList<AppInfo> appInfoList=new ArrayList<AppInfo>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                AppInfo info=new AppInfo();
                info.des=jsonObject1.getString("des");
                info.downloadUrl=jsonObject1.getString("downloadUrl");
                info.iconUrl = jsonObject1.getString("iconUrl");
                info.id = jsonObject1.getString("id");
                info.name = jsonObject1.getString("name");
                info.packageName = jsonObject1.getString("packageName");
                info.size = jsonObject1.getLong("size");
                info.stars = (float) jsonObject1.getDouble("stars");

                appInfoList.add(info);
            }

            //解析轮播图数据
            JSONArray jsonArray1=jsonObject.getJSONArray("picture");
            picturesUrl = new ArrayList<String>();
            for(int i=0;i<jsonArray1.length();i++){
                String pic=jsonArray1.getString(i);
                picturesUrl.add(pic);
            }
            return appInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> getPicUrlData(){
        return  picturesUrl;
    }
}
