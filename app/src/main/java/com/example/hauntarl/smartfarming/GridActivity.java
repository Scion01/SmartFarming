package com.example.hauntarl.smartfarming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridActivity extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int images[];

    public GridActivity(Context context,  int[] images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {

        return (images.length);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.activity_grid,null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewForGrid);

        TextView textView =(TextView) convertView.findViewById(R.id.textViewForGrid);

        TextView desc =(TextView) convertView.findViewById(R.id.descForGrid);


        switch (position){
            //case 0:
              //  textView.setText(R.string.crop);
                //break;
            //case 1:
              //  textView.setText(R.string.active);
                //break;

            //case 3:
              //  textView.setText(R.string.redeem);
                //break;
            case 0:
                textView.setText(R.string.chat);
                desc.setText(R.string.desc_chat);
                break;

            case 1:
                textView.setText(R.string.plan);
                desc.setText(R.string.desc_plan);
                break;

            //case 6:
              //  textView.setText(R.string.share);
                //break;

            case 2:
                textView.setText("Recommendations");
                desc.setText("These are products that are recommended to you by\nMr. Navale");
                break;

            case 3:
                textView.setText("Product Enquiry");
                desc.setText("Here you can ask us about any specific product that you may need or tell us what you are looking for...");
                break;
            case 4:
                textView.setText(R.string.request_call);
                desc.setText(R.string.desc_call);

                break;
            case 5:

                textView.setText(R.string.account);
                desc.setText(R.string.desc_acc);
                break;
            case 6:
                textView.setText(R.string.about);
                desc.setText(R.string.desc_about);
                break;
        }

        imageView.setImageResource(images[position]);
        return convertView;
    }
}
