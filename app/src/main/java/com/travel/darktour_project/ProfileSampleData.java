package com.travel.darktour_project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProfileSampleData {
    String IP_ADDRESS = "113.198.236.105";
    String tablename = "";
    String userid = "";
    String res = "";
    ArrayList<Profile> items=new ArrayList<>();

    public ProfileSampleData(String userid) throws ExecutionException, InterruptedException {
        this.tablename = tablename;
        this.userid = userid;

        ListLikes listLikes = new ListLikes();
        res = listLikes.execute("http://" + IP_ADDRESS + "/select.php", "likehistoric", userid).get();

    }

    public ArrayList<Profile> getItems() {
        // 유적지 리사이클러뷰 목록 추가
        try {
            Log.d("profile1 ", "all" + res);

            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String content = item.getString("content");
                Profile profile = new Profile(content);
                items.add(profile);

            }
        } catch (JSONException e) {
            Log.d("profile1 ", "showResult : ", e);
        }
        Log.d("profile1 items : ", items.toString());
        return items;
    }
}
