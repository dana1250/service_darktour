package com.travel.darktour_project;
// 윤지 상세페이지 식당 화면

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;


import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;


public class FoodMap extends AppCompatActivity {
    String getPhone []; //전화번호 저장용
    String getURL []; //place_url 저장용
    String getRoad_name []; //도로명 주소 저장용
    String getPlace_name []; // 음식점 이름
    String getX []; // x
    String getY [] ; // y
    MapView mapView; //지도
    String food_url;
    String food_place;
    String food_lat;
    String food_lon;
    String lon;
    String lat;
    String all;
    String center_place_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.arround_map);
        Intent intent = getIntent(); // intent 값 받아옴

        mapView = new MapView(this);// mapview 연결
        mapView.removeAllPOIItems(); // 이전 지도 화면에 추가된 모든 POI items 제거
        mapView.removeAllPolylines(); // 이전 지도 화면에 추가된 모든 Polylines 제거
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);

        lon = intent.getStringExtra("center_lon");
        lat = intent.getStringExtra("center_lat");
        all = intent.getStringExtra("is_all");
        center_place_name = intent.getStringExtra("center_name");

        //중심 마커 찍기
        double y = Double.parseDouble(lat); // 위도
        double x = Double.parseDouble(lon);

        mapView.setMapCenterPoint(mapPointWithGeoCoord(y, x), true);

        // 줌 레벨 변경

        mapView.setZoomLevel(2, true);


        MapPoint MARKER_POINT = mapPointWithGeoCoord(y,x);
        MapPOIItem marker = new MapPOIItem();

        marker.setItemName(center_place_name);
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        // 해당 위치 bluepin 주변 위치 초록pin 누르면 빨간핀
        mapView.addPOIItem(marker);




        if (intent.getStringExtra("is_all").equals("total")){

            NetworkThread thread = new NetworkThread();
            thread.start();
        }

        else{
            food_lon = intent.getStringExtra("food_lon");
            food_lat = intent.getStringExtra("food_lat");
            food_place = intent.getStringExtra("food_place");
            food_url = intent.getStringExtra("food_url");

            MapPOIItem poiItem = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(food_lat),Double.parseDouble(food_lon));
            poiItem.setItemName(food_place);
            poiItem.setTag(1);
            poiItem.setMapPoint(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            // 해당 위치 YellowPin 주변 위치 초록pin 누르면 빨간핀
            mapView.addPOIItem(poiItem);
        }
        // poiitem 설정을 위해
        mapView.setPOIItemEventListener(mvel);
    }
    // 말풍선 눌렀을 때
    MapView.POIItemEventListener mvel = new MapView.POIItemEventListener() {

        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        }

        // 일단은 해당 링크로 이동됨
        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
            int tag = mapPOIItem.getTag();

            Intent intent = new Intent(FoodMap.this,WebViewActivity.class); // 웹뷰
            if (all.equals("total")==true) { // 전체 화면
                intent.putExtra("url", getURL[tag - 1]); // url 다음 화면에 넘김
                startActivity(intent);

            }
            else{ // 두개화면
                if (tag == 0){
                    intent.putExtra("url", tag); // url 다음 화면에 넘김
                    startActivity(intent);

                }
                else{
                    intent.putExtra("url", food_url); // url 다음 화면에 넘김
                    startActivity(intent);

                }

            }


        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };
    class NetworkThread extends Thread{
        @Override
        public void run() {
            try{
                String keyword = "category_group_code=FD6&page=1&size=15&x="+lon+"&y="+lat+"&sort=distance";
                // x 경도 y 위도

                //String address = "https://dapi.kakao.com/v2/search/vclip?query="+keyword;
                String address = "https://dapi.kakao.com/v2/local/search/category.json?"+ keyword;

                URL url = new URL(address);
                //접속
                URLConnection conn = url.openConnection();
                //요청헤더 추가
                conn.setRequestProperty("Authorization","KakaoAK 7ce78d3c36644e24fc44fdc6afa0f7f2");

                //서버와 연결되어 있는 스트림을 추출한다.
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                StringBuffer buf = new StringBuffer();

                //읽어온다.
                do{
                    str = br.readLine();
                    if(str!=null){
                        buf.append(str);
                    }
                }while(str!=null);

                final String result = buf.toString();

                // 가장 큰 JSONObject를 가져옵니다.
                JSONObject jObject = new JSONObject(result);
                // 배열을 가져옵니다.
                JSONArray jArray = jObject.getJSONArray("documents");
                int list_cnt = jArray.length(); //Json 배열 내 JSON 데이터 개수를 가져옴
                //key의 value를 가져와 저장하기 위한 배열을 생성한다
                getPhone = new String[list_cnt]; //전화번호 저장용
                getURL = new String[list_cnt]; //place_url 저장용
                getRoad_name = new String[list_cnt]; //도로명 주소 저장용
                getPlace_name = new String[list_cnt]; // 음식점 이름
                getX = new String[list_cnt]; // x
                getY = new String[list_cnt]; // y


                // 배열의 모든 아이템
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    getPhone[i] = obj.getString("phone");
                    getURL[i] = obj.getString("place_url");
                    getRoad_name[i] = obj.getString("road_address_name");
                    getPlace_name[i] = obj.getString("place_name");
                    getX[i] = obj.getString("x");
                    getY[i] = obj.getString("y");

                }

                // 주변 마커 추가
                runOnUiThread (new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < 15; i++) {

                            MapPOIItem poiItem = new MapPOIItem();
                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(getY[i]),Double.parseDouble(getX[i]));
                            poiItem.setItemName(getPlace_name[i]);
                            poiItem.setTag(i+1);
                            poiItem.setMapPoint(mapPoint);
                            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                            // 해당 위치 YellowPin 주변 위치 초록pin 누르면 빨간핀
                            mapView.addPOIItem(poiItem);
                        }


                    }

                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}

