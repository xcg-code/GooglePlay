package com.app.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/23.
 */

public class HotProtocol extends BaseProtocol<ArrayList<String>> {
    @Override
    public String getKey() {
        return "app/hotlist";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    protected ArrayList<String> parseData(String result) {
        try {
            JSONArray ja=new JSONArray(result);
            ArrayList<String> list=new ArrayList<String>();
            for(int i=0;i<ja.length();i++){
                String info= (String) ja.get(i);
                list.add(info);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
