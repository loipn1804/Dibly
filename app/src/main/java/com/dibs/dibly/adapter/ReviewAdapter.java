package com.dibs.dibly.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import greendao.Comment;

/**
 * Created by USER on 7/3/2015.
 */
public class ReviewAdapter extends BaseAdapter {

    private List<Comment> listComment;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private Typeface typeface;
    private Typeface typefaceBold;

    public ReviewAdapter(Activity activity, List<Comment> listComment) {
        this.listComment = new ArrayList<>();
        this.listComment.addAll(listComment);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;

        options = new DisplayImageOptions.Builder().cacheInMemory(false).displayer(new RoundedBitmapDisplayer(500)).showImageForEmptyUri(R.color.transparent).showImageOnLoading(R.color.transparent).cacheOnDisk(true).build();

        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
        typefaceBold = StaticFunction.bold(activity);
    }

    public void setListData(List<Comment> listComment) {
        this.listComment.clear();
        this.listComment.addAll(listComment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listComment == null) ? 0 : listComment.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_review, null);
            holder = new ViewHolder();

            holder.rltRoot = (RelativeLayout) convertView.findViewById(R.id.rltRoot);
            holder.imvAvatar = (ImageView) convertView.findViewById(R.id.imvAvatar);
            holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUsername);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtReView = (TextView) convertView.findViewById(R.id.txtReView);
            holder.txtReadmore = (TextView) convertView.findViewById(R.id.txtReadmore);
            holder.txtAvatarName = (TextView) convertView.findViewById(R.id.txtAvatarName);
            holder.txtDealTitle = (TextView) convertView.findViewById(R.id.txtDealTitle);

            holder.txtUsername.setTypeface(typeface);
            holder.txtDate.setTypeface(typeface);
            holder.txtReView.setTypeface(typeface);
            holder.txtReadmore.setTypeface(typeface);
            holder.txtAvatarName.setTypeface(typefaceBold);
            holder.txtDealTitle.setTypeface(typefaceBold);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        ((BaseActivity)activity).overrideFontsLight(holder.rltRoot);

        holder.rltRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        if (!listComment.get(position).getProfile_image().endsWith("/")) {
        String image = listComment.get(position).getProfile_image();
        Log.e("image", "adapter----" + image);
        imageLoader.displayImage(listComment.get(position).getProfile_image(), holder.imvAvatar, options);
//        }
        holder.txtUsername.setText(listComment.get(position).getFirst_name() + " " + listComment.get(position).getLast_name());
        holder.txtDate.setText(changeDateFormat(listComment.get(position).getCreated_at()));
        holder.txtReView.setText(listComment.get(position).getText());
        holder.txtReadmore.setVisibility(View.GONE);
//        holder.imvAvatar.setVisibility(View.GONE);

        if (listComment.get(position).getDeal_id() == 0l) {
            holder.txtDealTitle.setVisibility(View.GONE);
        } else {
            holder.txtDealTitle.setVisibility(View.VISIBLE);
            holder.txtDealTitle.setText(listComment.get(position).getTitle());
        }

        String avatarName;
        if (listComment.get(position).getFirst_name().length() > 0) {
            avatarName = listComment.get(position).getFirst_name().substring(0, 1);
        } else if (listComment.get(position).getLast_name().length() > 0) {
            avatarName = listComment.get(position).getLast_name().substring(0, 1);
        } else {
            avatarName = "";
        }
        avatarName = avatarName.toUpperCase();
        holder.txtAvatarName.setText(avatarName);
        if (avatarName.length() > 0) {
            holder.txtAvatarName.setBackgroundResource(StaticFunction.getBackgroundAvatarName(avatarName));
        }

        return convertView;
    }

    public class ViewHolder {
        RelativeLayout rltRoot;
        ImageView imvAvatar;
        TextView txtUsername;
        TextView txtDate;
        TextView txtReView;
        TextView txtReadmore;
        TextView txtAvatarName;
        TextView txtDealTitle;
    }

    private String changeDateFormat(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return day + "/" + month + "/" + year;
    }
}
