package com.travel.darktour_project;
// 코스 탐색 유적지 화면 adpater
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.travel.darktour_project.R.layout.site_item;

public class SearchSiteRecyclerAdapter extends RecyclerView.Adapter<SearchSiteRecyclerAdapter.ItemViewHolder> implements OnSiteItemClickListener, Filterable {

    // adapter에 들어갈 list 입니다.
    private ArrayList<SiteData> listData = new ArrayList<>();
    private ArrayList<SiteData> filterData = new ArrayList<>();

    OnSiteItemClickListener listener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.

        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(site_item, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        //SiteData item = listData.get(position);
        holder.onBind(filterData.get(position));

        if(filterData.get(position) == listData.get(listData.indexOf(filterData.get(position)))){
            holder.background_change.setBackgroundResource(filterData.get(position).getLayout_());
            holder.background_change.setBackgroundResource(listData.get(listData.indexOf(filterData.get(position))).getLayout_());
        }

    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return filterData.size();
    }


    void addItem(SiteData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        //listData.add(data);
        listData.add(data);
        filterData = listData;

    }
    public void setOnItemClicklistener(OnSiteItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ItemViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if (str.isEmpty()) {
                    filterData = listData;
                } else {
                    ArrayList<SiteData> filteringList = new ArrayList<>();


                    for (SiteData item : listData) {
                        if(item.getTitle().contains(str))
                            filteringList.add(item);
                    }

                    filterData = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterData;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterData = (ArrayList<SiteData>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder  /*implements View.OnClickListener*/{

        private TextView desc;
        private TextView title; // 유적지 or 코스 이름
        private TextView total_like; // 따봉 숫자
        private ImageView image; // image
        private boolean press = true;  // 눌렸는가
        private LinearLayout background_change; // 배경 변경을 위한 레이아웃
        private TextView accident_; // 사건
        ItemViewHolder(View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
            total_like = itemView.findViewById(R.id.thumb_count);
            image = itemView.findViewById(R.id.image);
            background_change = itemView.findViewById(R.id.background_change);
            accident_ = itemView.findViewById(R.id.accident);
            
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        void onBind(SiteData data) {

            desc.setText(data.getDesc());
            title.setText(data.getTitle());
            total_like.setText(data.getLike());
            //image.setImageBitmap(data.getImage());// 이미지
            Glide.with(SearchCourse.mContext).asBitmap().load(data.getImage()).
                    into(image);

            background_change.setBackgroundResource(data.getLayout_()); // 눌렀을때 layout
            String incident = data.getAccident_text() ;

            accident_.setText(incident); // 사건 설정
            accident_.setSelected(true);
            accident_.setSingleLine();
            accident_.setMarqueeRepeatLimit(-1);
            accident_.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        }

    }
    public SiteData getItem(int position){
        return listData.get(listData.indexOf(filterData.get(position))); }

}


