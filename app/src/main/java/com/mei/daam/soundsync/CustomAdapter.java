package com.mei.daam.soundsync;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filipa on 17-04-2018.
 */

public class CustomAdapter extends BaseAdapter {


        Context context;
        private List<String> values=new ArrayList<String>();
        private  List<String> numbers=new ArrayList<String>();
        private  List<String> images=new ArrayList<>();

        public CustomAdapter(Context context, List<String> values, List<String> numbers, List<String> images){
            //super(context, R.layout.single_list_app_item, utilsArrayList);
            this.context = context;
            this.values = values;
            this.numbers = numbers;
            this.images = images;
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(final int position,  View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_adapter,null);
            TextView txtName = (TextView) convertView.findViewById(R.id.aNametxt);
            TextView txtVersion = (TextView) convertView.findViewById(R.id.aVersiontxt);
            ImageView icon = (ImageView) convertView.findViewById(R.id.appIconIV);
            txtName.setText(values.get(position));
            txtVersion.setText(numbers.get(position));
            Picasso.get().load(images.get(position)).into(icon);

            return convertView;
        }


    }

