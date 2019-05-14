package com.example.networkingflowersv5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {


    public static final String FLOWER_NAME = "Flower Name";
    public static final int RESULT_CODE = 200;
    private String FlowerName = "";




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FlowerModel flower = getIntent().getParcelableExtra(MainActivity.FLOWER_OBJECT);

        TextView tvFlowerName = findViewById(R.id.tvFlowerNameDetail);
        TextView tvFlowerInstr = findViewById(R.id.tvFlowerInstructionsDetail);
        TextView tvFlowerPrice = findViewById(R.id.tvFlowerPriceDetail);
        TextView tvFlowerDesc = findViewById(R.id.tvDetailCategory);
        ImageView ivFlowerImage = findViewById(R.id.ivFlowerImageDetail);


        tvFlowerName.setText(flower.getName());
        tvFlowerDesc.setText(flower.getCategory());
        tvFlowerInstr.setText(flower.getInstructions());
        tvFlowerPrice.setText(String.format("%s", flower.getPrice()));
        ivFlowerImage.setImageBitmap(flower.getBitmap());

        FlowerName = flower.getName();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void BuyOnClickHandler(View view){

        Intent intent = getIntent().putExtra(FLOWER_NAME, FlowerName);
        setResult(RESULT_CODE, intent);
        finish();

    }
}
