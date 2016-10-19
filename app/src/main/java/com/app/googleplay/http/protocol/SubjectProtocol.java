package com.app.googleplay.http.protocol;

import com.app.googleplay.domain.SubjectInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/18.
 */

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {
    @Override
    public String getKey() {
        return "app/applist";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<SubjectInfo> parseData(String result) {
        /**
         * 昨日分析问题至，json数据有问题
         */
        System.out.println("数据结果"+result);
        try {
            System.out.println("位置1");
            JSONArray ja = new JSONArray(result);
            System.out.println("位置2");
            ArrayList<SubjectInfo> list = new ArrayList<SubjectInfo>();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                SubjectInfo info = new SubjectInfo();
                info.des = jo.getString("des");
                info.url = jo.getString("url");

                list.add(info);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
