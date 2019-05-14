package com.example.networkingflowersv5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.browse.MediaBrowser;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.networkingflowersv5.MainActivity.FLOWER_OBJECT;
import static com.example.networkingflowersv5.MainActivity.REQUEST_CODE;

public class RecycleViewFlowerAdapter extends RecyclerView.Adapter<RecycleViewFlowerAdapter.FlowerViewHolder> {
    private List<FlowerModel> mFlowerList = null;
    private Context context;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private View mItemView;

    public RecycleViewFlowerAdapter(List<FlowerModel> mFlowerList, Context context, Activity mActivity) {
        this.mFlowerList = mFlowerList;
        this.context = context;
        this.mActivity = mActivity;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FlowerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i ) {
        View view = mInflater.inflate(R.layout.layout_for_each_flower,viewGroup,false);
        mItemView = view;
        return new FlowerViewHolder(this, view);

    }

    @Override
    public void onBindViewHolder(@NonNull FlowerViewHolder flowerViewHolder, int position) {
        FlowerModel flower = mFlowerList.get(position);
        flowerViewHolder.tvFloweName.setText(flower.getName());
        /// get the image from the catch file
        Bitmap bitmap = FileSaver.GetFile(context,flower);
        if(bitmap !=null)
        {
            flower.setBitmap(bitmap);
            flowerViewHolder.ivFlowerImage.setImageBitmap(bitmap);
        }
        else {
            MainActivity.DataSender dataSender = new MainActivity.DataSender(flowerViewHolder.ivFlowerImage, flower);
            new MainActivity.ImageDownloader().execute(dataSender);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean checked = sharedPreferences.getBoolean(context.getString(R.string.Key_Enable_Default_Colors),true);
        if(!checked){
            mItemView.setBackgroundColor(Color.rgb(208,210,211));
        }
    }

    @Override
    public int getItemCount() {
        if(mFlowerList !=null && mFlowerList.size() > 0)
            return mFlowerList.size();
        else
            return 0;
    }

    public class FlowerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final RecycleViewFlowerAdapter mAdapter;
        private TextView tvFloweName;
        private ImageView ivFlowerImage;


        public FlowerViewHolder(RecycleViewFlowerAdapter mAdapter,  View itemView )  {
            super(itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
            tvFloweName = itemView.findViewById(R.id.tvFlowerName);
            ivFlowerImage = itemView.findViewById(R.id.ivFlowerImage);

        }

        @Override
        public void onClick(View v) {
            FlowerModel flower = mFlowerList.get(getLayoutPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(MainActivity.FLOWER_OBJECT, flower);
            mActivity.startActivityForResult(intent, MainActivity.REQUEST_CODE);
            mAdapter.notifyDataSetChanged();
        }
    }

    FlowerModel getFlowerPossition(int possition)
    {
        return mFlowerList.get(possition);
    }


}
