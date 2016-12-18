package com.example.naghmeh.mostreviewed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fion on 12/7/2016.
 */

public class BusinessAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Business> mDataSource;

    public BusinessAdapter(Activity context, List<Business> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.listview_row, parent, false);

            holder = new ViewHolder();
            holder.searchImg = (ImageView) convertView.findViewById(R.id.searchImg);
            holder.searchNameTextView = (TextView) convertView.findViewById(R.id.searchName);
            holder.searchAddressTextView = (TextView) convertView.findViewById(R.id.searchAddress);
            holder.searchMilesTextView = (TextView) convertView.findViewById(R.id.searchMiles);
            holder.searchDollarSignTextView = (TextView) convertView.findViewById(R.id.searchDollarSign);
            holder.searchNumReviewTextView = (TextView) convertView.findViewById(R.id.searchNumReviews);
            holder.searchRatingBar = (RatingBar) convertView.findViewById(R.id.searchRatingBar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView searchNameTextView = holder.searchNameTextView;
        TextView searchAddressTextView = holder.searchAddressTextView;
        TextView searchMilesTextView = holder.searchMilesTextView;
        TextView searchDollarSignTextView = holder.searchDollarSignTextView;
        TextView searchNumReviewTextView = holder.searchNumReviewTextView;
        ImageView searchImg = holder.searchImg;
        RatingBar searchRatingBar = holder.searchRatingBar;

        Business business = (Business) getItem(position);


        searchNameTextView.setText(business.name);
        searchAddressTextView.setText(business.address1+", "+business.city);
        searchMilesTextView.setText(business.getDistance()+" miles");
        searchDollarSignTextView.setText(business.price);
        searchNumReviewTextView.setText(business.review_count + " reviews");
        float floatRating = Float.valueOf(business.rating);
        searchRatingBar.setRating(floatRating);
        Picasso.with(mContext).load(business.image_url).placeholder(R.mipmap.ic_launcher).into(searchImg);
        return convertView;
    }

    private static class ViewHolder {
        public TextView searchNameTextView;
        public TextView searchAddressTextView;
        public TextView searchMilesTextView;
        public TextView searchDollarSignTextView;
        public TextView searchNumReviewTextView;
        public ImageView searchImg;
        public RatingBar searchRatingBar;
    }
}
