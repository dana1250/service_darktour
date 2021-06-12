package com.travel.darktour_project;

// 윤지 fragment 코스 탐색 구현부
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class AddFragment extends Fragment {
    LocationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        // 어댑터 안에 데이터 담기
        adapter = new LocationAdapter();
        adapter.addItem(new LocationItem(R.drawable.blossoms,"전체 지역","AI 추천"));
        adapter.addItem(new LocationItem( R.drawable.seoul,"서울","3.1 운동 , 한국 전쟁"));
        adapter.addItem(new LocationItem( R.drawable.jeju,"제주","4.3 사건"));
        adapter.addItem(new LocationItem( R.drawable.busan,"부산","한국 전쟁 , 민주항쟁"));
        adapter.addItem(new LocationItem( R.drawable.tobecontinued," "," "));
        
        

        // 리스트 뷰에 어댑터 설정
        gridView.setAdapter(adapter);

        // 이벤트 처리 리스너 설정
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position){
                    case 0:
                        intent = new Intent(getActivity(),SearchCourse.class);
                        intent.putExtra("location","전체"); // 서울 선택한 것을 다음 화면에 넘김
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(),SearchCourse.class);
                        intent.putExtra("location","서울"); // 서울 선택한 것을 다음 화면에 넘김
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(),SearchCourse.class);
                        intent.putExtra("location","제주"); // 제주 선택한 것을 다음 화면에 넘김
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(),SearchCourse.class);
                        intent.putExtra("location","부산"); // 부산 선택한 것을 다음 화면에 넘김
                        startActivity(intent);
                        break;

                }


            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    class LocationAdapter extends BaseAdapter {
        ArrayList<LocationItem> items = new ArrayList<LocationItem>();


        // Generate > implement methods
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(LocationItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 뷰 객체 재사용
            LocationItemView view = null;
            if (convertView == null) {
                view = new LocationItemView(getActivity());
            } else {
                view = (LocationItemView) convertView;
            }

            LocationItem item = items.get(position);

            view.setImage_url(item.getImage_url());
            view.setTitle(item.getTitle());
            view.setDescription(item.getDescription());


            return view;
        }
    }
    // 데이터 담기
    public class LocationItem {
        int image_url;
        String title;
        String description;

        // Generate > Constructor
        public LocationItem(int image_url,String title, String description) {
            this.image_url = image_url;
            this.title = title;
            this.description = description;
        }

        // Generate > Getter and Setter

        public int getImage_url() {
            return image_url;
        }

        public void setImage_url(int image_url) {
            this.image_url = image_url;
        }

        public String getDescription() {
            return description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        // Generate > toString() : 아이템을 문자열로 출력

    }

}
