package com.app.googleplay.http.protocol;

import com.app.googleplay.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/18.
 */

public class GameProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    @Override
    public String getKey() {
        return "app/gamelist";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    protected ArrayList<AppInfo> parseData(String result) {
        ArrayList<AppInfo> list=new ArrayList<AppInfo>();
        try {
            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                AppInfo info=new AppInfo();
                info.des=jsonObject.getString("des");
                info.downloadUrl=jsonObject.getString("downloadUrl");
                info.iconUrl = jsonObject.getString("iconUrl");
                info.id = jsonObject.getString("id");
                info.name = jsonObject.getString("name");
                info.packageName = jsonObject.getString("packageName");
                info.size = jsonObject.getLong("size");
                info.stars = (float) jsonObject.getDouble("stars");

                list.add(info);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
