package com.travel.darktour_project;
// 코스 선택 화면 adpater

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.travel.darktour_project.R.layout.favorite_item;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ItemViewHolder> implements OnFavoriteItemClickListener{

    // adapter에 들어갈 list 입니다.
    private ArrayList<FavoriteData> listData = new ArrayList<>();
    OnFavoriteItemClickListener listener;
    private boolean start_state = true; // 시작지 눌러졌는지 상태
    private boolean finish_state = true; // 도착지 눌러졌는지 상태
    private int button_click_count = 0; // 출발지 도착지 눌러졌는지 확인
    private int[] start_finish_arr = new int[2];

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.

        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(favorite_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        FavoriteData item = listData.get(position);
        holder.onBind(listData.get(position));
        holder.cancel.setTag(position);

        holder.cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                listData.remove(pos);
                notifyDataSetChanged();

                ((SearchCourse)SearchCourse.mContext).refresh(pos);
            }
        });


    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }


    void addItem(FavoriteData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    @Override
    public void onItemClick(ItemViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }
    public void setOnItemClicklistener(OnFavoriteItemClickListener listener){
        this.listener = listener;
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView desc;
        private TextView title; // 유적지
        private LinearLayout cancel; // 삭제버튼
        private Button press_start;
        private Button press_finish;

        ItemViewHolder(View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
            cancel = itemView.findViewById(R.id.cancel);
            press_start = itemView.findViewById(R.id.start_btn);
            press_finish = itemView.findViewById(R.id.finish_btn);
            press_start.setOnClickListener(this);
            press_finish.setOnClickListener(this);

        }

        void onBind(FavoriteData data) {

            desc.setText(data.getDesc());
            title.setText(data.getTitle());
            press_start.setBackgroundResource(data.getStart_back());
            press_finish.setBackgroundResource(data.getFinish_back());
            press_start.setTextColor(data.getStart_text());
            press_finish.setTextColor(data.getFinish_text());


        }
        public void enabled_start(boolean btn){ // 모든 시작지 버튼
            int count = getItemCount();
            if (btn){
                for(int i =0; i < count; i++){
                    listData.get(i).setStart_text(Color.parseColor("#D3E6F3"));
                    notifyItemChanged(i);
                }
            }
            else{
                for(int i =0; i < count; i++){
                    if(!listData.get(i).isPress_finish()){
                        listData.get(i).setStart_text(Color.parseColor("#647C8C"));
                        notifyItemChanged(i);
                    }

                }
            }
        }
        public void enabled_finish(boolean btn){ // 모든 도착지 버튼
            int count = getItemCount();
            if (btn){
                for(int i =0; i < count; i++){
                    listData.get(i).setFinish_text(Color.parseColor("#D3E6F3"));
                    notifyItemChanged(i);
                }
            }
            else{
                for(int i =0; i < count; i++){
                    if(!listData.get(i).isPress_start()) {
                        listData.get(i).setFinish_text(Color.parseColor("#647C8C"));
                        notifyItemChanged(i);
                    }
                }
            }
        }

        @Override
        public void onClick(View v) { // 시작지 도착지 클릭 시

            switch (v.getId()){

                case R.id.start_btn: { // 시작지 버튼 클릭

                    Boolean clickBefore_start_1 = listData.get(getAdapterPosition()).isPress_start();
                    Boolean clickBefore_finish_1 = listData.get(getAdapterPosition()).isPress_finish();

                    if (clickBefore_start_1 == false) { // 시작지 버튼 클릭 시
                        if(start_state && !clickBefore_finish_1){ // 시작지가 설정 되어 있지않고, 도착지 설정이 되어있지 않은 경우
                            enabled_start(true); // 모든 버튼 enabled
                            button_click_count ++; // 버튼 클릭 count 증가
                            start_finish_arr[0] = getAdapterPosition(); // 0번지에 현재 포지션 대입 (시작지)
                            listData.get(getAdapterPosition()).setPress_start(true); // setPress_start true
                            listData.get(getAdapterPosition()).setStart_back(R.drawable.ic_press_btn); // 시작지 버튼 눌림 배경으로 변경
                            listData.get(getAdapterPosition()).setStart_text(Color.parseColor("#647C8C")); // 시작지 색깔 변경
                            listData.get(getAdapterPosition()).setFinish_text(Color.parseColor("#D3E6F3")); // 도착지 색깔 변경
                            notifyItemChanged(getAdapterPosition()); // adapter에 변경 notify
                            start_state = false; // 시작 버튼 클릭됨 (false)
                        }
                    }
                    else {
                        if(!clickBefore_finish_1){ // 도착지 설정이 되어있지 않은 경우
                            enabled_start(false); // 모든 출발 버튼 unenabled
                            listData.get(getAdapterPosition()).setPress_start(false); // 시작버튼 활성화
                            listData.get(getAdapterPosition()).setStart_back(R.drawable.ic_not_press_btn); //시작지 버튼 취소 배경으로 변경
                            button_click_count --; // 버튼 클릭 count 감소
                            if(finish_state){ // 도착지상태 true
                                listData.get(getAdapterPosition()).setFinish_text(Color.parseColor("#647C8C")); // 도착지 글자색 변경
                            }
                            notifyItemChanged(getAdapterPosition()); // adapter에 변경 notify
                            start_state = true; // 시작 버튼 클릭됨 (true)
                        }
                    }
                    break;
                }


                case R.id.finish_btn: { // 도착지 버튼

                    Boolean clickBefore_start_2 = listData.get(getAdapterPosition()).isPress_start();
                    Boolean clickBefore_finish_2 = listData.get(getAdapterPosition()).isPress_finish();

                    if (clickBefore_finish_2 == false) { // 도착지 버튼 클릭 시
                        if(finish_state && !clickBefore_start_2){   // 도착지가 설정 되어 있지않고, 시작지 설정이 되어 있지 않은 경우
                            enabled_finish(true); // 모든 종료 버튼 enabled
                            button_click_count ++; // 버튼 클릭 count 증가
                            start_finish_arr[1] = getAdapterPosition(); // 1번지에 현재 포지션 대입 (도착지)
                            listData.get(getAdapterPosition()).setPress_finish(true); // setPress_false true
                            listData.get(getAdapterPosition()).setFinish_back(R.drawable.ic_press_btn); // 도착지 버튼 눌림 배경으로 변경
                            listData.get(getAdapterPosition()).setFinish_text(Color.parseColor("#647C8C")); // 도착지 색깔 변경
                            listData.get(getAdapterPosition()).setStart_text(Color.parseColor("#D3E6F3")); // 출발지 색깔 변경
                            notifyItemChanged(getAdapterPosition()); // adapter에 변경 notify
                            finish_state = false; // 도착 버튼 클릭됨 (false)
                        }
                    } else {
                        if(!clickBefore_start_2){ // 출발지 설정이 되어있지 않은 경우
                            enabled_finish(false); // 모든 출발 버튼 unenabled
                            listData.get(getAdapterPosition()).setPress_finish(false); // 도착 버튼 활성화
                            listData.get(getAdapterPosition()).setFinish_back(R.drawable.ic_not_press_btn); //도착지 버튼 취소 배경으로 변경
                            button_click_count--; // 버튼 클릭 count 감소
                            if(start_state){ // 출발지상태 true
                                listData.get(getAdapterPosition()).setStart_text(Color.parseColor("#647C8C")); // 출발지 글자색 변경
                            }
                            notifyItemChanged(getAdapterPosition()); // adapter에 변경 notify
                            finish_state = true; // 도착 버튼 클릭됨 (true)
                        }

                    }
                    break;
                }

            }

        }
    }
    public FavoriteData getItem(int position){
        return listData.get(position); }

    public int getButton_click_count(){
        return button_click_count;
    }
    public int[] getStart_finish_arr(){
        return start_finish_arr;
    } // 0번지는 시작지 1번지는 도착지
}



