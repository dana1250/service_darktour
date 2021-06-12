package com.travel.darktour_project;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;



public class RoadFrag extends Fragment {
    String[] titleNumArr; // 유적지 이름 저장 arr
    ArrayList<String> x = new ArrayList<String>();; // 경도 -lon
    ArrayList<String> y = new ArrayList<String>();; // 위도 -lat
    int[] start_finish_arr; // 시작 도착지 좌표
    static MapPolyline polyline ;
    MapView mapView ;
    public ArrayList<MyLocationData> locationarray ;
    View view;
    TextView timeandkm;
    static String d = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.roadfragment_layout, container, false);
        Bundle bundle = getArguments();  //번들 받기. getArguments() 메소드로 받음.

        if(bundle != null){
            titleNumArr = bundle.getStringArray("title"); //유적지 이름
            Collections.addAll(x,bundle.getStringArray("x"));
            Collections.addAll(y,bundle.getStringArray("y"));
            start_finish_arr = bundle.getIntArray("start_finish_arr"); //start_finish_arr
        }

        locationarray = new ArrayList<>();

        locationarray.add(new MyLocationData(titleNumArr[start_finish_arr[0]],x.get(start_finish_arr[0]),y.get(start_finish_arr[0]))); // 출발지

        for(int i =0; i < titleNumArr.length; i++){
            if(!(x.get(i).equals(x.get(start_finish_arr[0]))) && !(x.get(i).equals(x.get(start_finish_arr[1])))){
                locationarray.add(new MyLocationData(titleNumArr[i],x.get(i),y.get(i)));
            }
        }
        locationarray.add(new MyLocationData(titleNumArr[start_finish_arr[1]],x.get(start_finish_arr[1]),y.get(start_finish_arr[1]))); // 도착지


        NetworkThread thread = new NetworkThread();
        thread.start();

        return view;
    }

    class NetworkThread extends Thread{

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            try{
                timeandkm = (TextView) view.findViewById(R.id.time_km); // 이동시간 km 값
                mapView = new MapView(getContext());// mapview 연결
                ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
                mapViewContainer.addView(mapView);
                URL url = new URL("https://api.openrouteservice.org/v2/directions/foot-walking/geojson");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                http.setRequestProperty("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
                http.setRequestProperty("Authorization", "5b3ce3597851110001cf6248455e16b2e6024e9c99ae3e10dac4e6a7");

                String coordinates_string = "";
                for(int i=0; i<titleNumArr.length;i++){
                    coordinates_string += "["+x.get(i)+","+y.get(i)+"]";
                    if(i != titleNumArr.length-1){
                        coordinates_string += ",";
                    }
                }
                String data = "{\"coordinates\":["+coordinates_string+"]}";

                byte[] out = data.getBytes(StandardCharsets.UTF_8);

                OutputStream stream = http.getOutputStream();
                stream.write(out);

                BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JSONObject jObject = new JSONObject(response.toString());
                JSONArray features = (JSONArray) jObject.get("features");
                JSONObject zero = (JSONObject) features.get(0);
                JSONObject geometry = (JSONObject) zero.get("geometry");
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                ArrayList<JSONArray> path = new ArrayList<JSONArray>(); // 세부
                for(int i=0; i < coordinates.length();i++){ //object 넣기
                    path.add((JSONArray) coordinates.get(i));
                }


                polyline = new MapPolyline();
                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(200, 255, 0, 0)); // Polyline 컬러 지정.

                for(int i=0; i<path.size(); i++){


                    JSONArray stop = (JSONArray) path.get(i);

                    polyline.addPoint(MapPoint.mapPointWithGeoCoord((Double) stop.get(1),(Double) stop.get(0)));


                }
                mapView.addPolyline(polyline);
                for(int i=0; i<titleNumArr.length;i++){ // 출발지 도착지 좌표 지정
                    MapPOIItem poiItem = new MapPOIItem();
                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(locationarray.get(i).getLat()),Double.parseDouble(locationarray.get(i).getLon())); // 좌표
                    poiItem.setItemName(locationarray.get(i).getName());
                    poiItem.setMapPoint(mapPoint);
                    if(i ==0){
                        poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        poiItem.setCustomImageResourceId(R.drawable.custom_poi_marker_start);
                        poiItem.setCustomImageAutoscale(true);
                        poiItem.setCustomImageAnchor(0.5f, 1.0f);


                    }else if(i == titleNumArr.length-1){
                        poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        poiItem.setCustomImageResourceId(R.drawable.custom_poi_marker_end);
                        poiItem.setCustomImageAutoscale(true);
                        poiItem.setCustomImageAnchor(0.5f, 1.0f);


                    }else{
                        poiItem.setMarkerType(MapPOIItem.MarkerType.RedPin);
                        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.


                    }
                    //mapView.addPolyline(polylines[i]);
                    mapView.addPOIItem(poiItem);
                }

                mapView.setZoomLevel(20, true);

                // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 200; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                JSONObject properties = (JSONObject) zero.get("properties"); // 시간 경로 받아오기
                JSONObject summary = (JSONObject) properties.get("summary"); // 시간 경로 받아오기
                double distance = (double) summary.get("distance"); // 총거리 m단위
                double duration = (double) summary.get("duration"); // 총시간 초단위
                String minutes = String.format("%d",(int)(duration / 60) % 60)+" 분"; // 분
                String time = "";


                if (duration / 3600 >= 1){ // 1시간 이상
                    time = String.format("%d",(int)duration / 3600) + " 시간 "+ minutes ;
                }
                else{
                    time = minutes;
                }   d = "이동거리: "+String.format("%.1f",distance/1000)+" KM "+"이동시간: "+time;
                (getActivity()).runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        timeandkm.setText(d);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }

}
