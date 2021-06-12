package com.travel.darktour_project;
// 음식점
import android.view.View;
public interface OnItemClickListener {
    public void onItemClick(FoodRecyclerAdapter.ItemViewHolder holder, View view, int position);

}
