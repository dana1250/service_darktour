package com.travel.darktour_project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class LocationItemView extends LinearLayout {

    TextView location_name;
    TextView des;
    ImageView imageView;

    // Generate > Constructor

    public LocationItemView(Context context) {
        super(context);

        init(context);
    }

    public LocationItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // singer_item.xml을 inflation
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_location_item, this, true);

        location_name = (TextView) findViewById(R.id.location_name);
        des = (TextView) findViewById(R.id.description);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void setTitle(String title) {
        location_name.setText(title);
    }

    public void setDescription(String description) {
        des.setText(description);
    }

    public void setImage_url(int image_url) {
        imageView.setImageResource(image_url);
    }

}