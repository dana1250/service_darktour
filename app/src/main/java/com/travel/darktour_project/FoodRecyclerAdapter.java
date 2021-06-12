package com.travel.darktour_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.ItemViewHolder> implements OnItemClickListener{

    // adapter에 들어갈 list 입니다.
    private ArrayList<ArroundData> listData = new ArrayList<>();
    OnItemClickListener listener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.

        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arround_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        ArroundData item = listData.get(position);
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }


    void addItem(ArroundData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    public void setOnItemClicklistener(OnItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onItemClick(ItemViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }


    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView place_name;
        private TextView phone_number;
        private TextView category_food;

        ItemViewHolder(View itemView) {
            super(itemView);

            place_name = itemView.findViewById(R.id.place_name);
            phone_number = itemView.findViewById(R.id.phone_number);
            category_food = itemView.findViewById(R.id.category_food);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ItemViewHolder.this, v, position);
                    }
                }
            });



        }

        void onBind(ArroundData data) {
            place_name.setText(data.getTitle());
            phone_number.setText(data.getContent());
            category_food.setText(data.getFood());

        }


    }
    public ArroundData getItem(int position){
        return listData.get(position); }
}


