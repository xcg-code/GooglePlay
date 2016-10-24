package com.app.googleplay.http.protocol;

import com.app.googleplay.domain.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/23.
 */

public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {
    @Override
    public String getKey() {
        return "app/categorylist";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    protected ArrayList<CategoryInfo> parseData(String result) {
        ArrayList<CategoryInfo> list=new ArrayList<CategoryInfo>();
        try {
            JSONArray ja=new JSONArray(result);
            for(int i=0;i<ja.length();i++){
                JSONObject jo=ja.getJSONObject(i);
                if(jo.has("title")){
                    CategoryInfo info=new CategoryInfo();
                    info.isTitle=true;
                    info.title=jo.getString("title");
                    list.add(info);
                }
                if(jo.has("infos")){
                    JSONArray ja1=jo.getJSONArray("infos");
                    for(int j=0;j<ja1.length();j++){
                        System.out.println("j="+j);
                        JSONObject jo1=ja1.getJSONObject(j);
                        CategoryInfo info1=new CategoryInfo();
                        info1.name1=jo1.getString("name1");
                        info1.name2=jo1.getString("name2");
                        info1.name3=jo1.getString("name3");
                        info1.url1=jo1.getString("url1");
                        info1.url2=jo1.getString("url2");
                        info1.url3=jo1.getString("url3");
                        info1.isTitle=false;
                        System.out.println(info1.name1+info1.name2+info1.name3);
                        list.add(info1);
                    }
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
