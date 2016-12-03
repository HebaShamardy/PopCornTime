package com.example.heba_pc.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by HEBA-PC on 11/30/2016.
 */

public class DetailsAdapter extends BaseExpandableListAdapter {
    private List<String> headers;
    private HashMap<String, List<?>> childs;
    private Context context;
    private LayoutInflater headerLayout;

    public DetailsAdapter(List<String> headers, HashMap<String, List<?>> childs, Context context) {
        this.headers = headers;
        this.childs = childs;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String header = headers.get(groupPosition);
        return childs.get(header).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String header = headers.get(groupPosition);
        return childs.get(header).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String header = (String) getGroup(groupPosition);
        if(convertView == null)
        {
            LayoutInflater headerLayout = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = headerLayout.inflate(R.layout.expandable_parent_view,null);

        }
        TextView headerView = (TextView) convertView.findViewById(R.id.parent_title);

        headerView.setText(header);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Trailer trailer = null;
        Review review = null;
        if(convertView == null)
        {
            headerLayout = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            if(groupPosition==0)
            {
                trailer = (Trailer) getChild(groupPosition,childPosition);
                convertView = headerLayout.inflate(R.layout.expandable_trailer_view,null);
                ImageView playIcon = (ImageView) convertView.findViewById(R.id.play_vid);
                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.video_name);
                Picasso.with(context).load(trailer.getImageURL()).into(thumbnail);
                playIcon.setVisibility(View.VISIBLE);
                title.setText(trailer.getName());
            }
            if(groupPosition==1)
            {
                review = (Review) getChild(groupPosition,childPosition);
                convertView = headerLayout.inflate(R.layout.expandable_review_view,null);
                TextView author = (TextView) convertView.findViewById(R.id.review_author);
                TextView content = (TextView) convertView.findViewById(R.id.review_content);
                author.setText(review.getAuthor());
                content.setText(review.getContent());

            }


        }
        else
        {
            if(groupPosition==0)
            {
                trailer = (Trailer) getChild(groupPosition,childPosition);
                convertView = headerLayout.inflate(R.layout.expandable_trailer_view,null);
                ImageView playIcon = (ImageView) convertView.findViewById(R.id.play_vid);
                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.video_name);
                Picasso.with(context).load(trailer.getImageURL()).into(thumbnail);
                playIcon.setVisibility(View.VISIBLE);
                title.setText(trailer.getName());
            }
            if(groupPosition==1)
            {
                review = (Review) getChild(groupPosition,childPosition);
                convertView = headerLayout.inflate(R.layout.expandable_review_view,null);
                TextView author = (TextView) convertView.findViewById(R.id.review_author);
                TextView content = (TextView) convertView.findViewById(R.id.review_content);
                author.setText(review.getAuthor());
                content.setText(review.getContent());

            }
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        if(groupPosition ==0)
            return true;
        else
            return false;
    }
}
