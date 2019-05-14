package com.example.networkingflowersv5;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;

public class InsertUpdateActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;
    private  ImageView iv_ThumbNail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Insert Flower");
        setContentView(R.layout.activity_insert_update);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        iv_ThumbNail =findViewById(R.id.ivFlowerSelectImage);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Thank You For ", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Button button = findViewById(R.id.btnSelectImage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(InsertUpdateActivity.this);
            }
        });

    }



    public void SelectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Your Flower Picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Image"),15);

    }
    protected void onActivityResult(int requestCode, int resultCode,  Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (requestCode == 15 && resultCode == RESULT_OK) {
//            try {
//                if (intent != null) {
//                    Uri fileUri = intent.getData();
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), intent.getData());
//                    Log.i("BitmapSize", bitmap.getByteCount() + "");
//                    bitmap = getResizedBitmap(bitmap, 100);
//                    Log.i("BitmapSizeReduced", bitmap.getByteCount() + "");
//                    iv_ThumbNail.setImageBitmap(bitmap);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.v("CRUDActivity", e.getMessage());
//            }
//        }
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && intent != null) {
                        Bitmap selectedImage = (Bitmap) intent.getExtras().get("data");
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),intent.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i("BitmapSize",bitmap.getByteCount()+ "");
                        bitmap = getResizedBitmap(bitmap,100);
                        Log.i("BitmapSizeReduced", bitmap.getByteCount() + "");
                        iv_ThumbNail.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && intent != null) {
                        Uri selectedImage = intent.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), intent.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i("BitmapSize",bitmap.getByteCount()+ "");
                        bitmap = getResizedBitmap(bitmap,100);
                        Log.i("BitmapSizeReduced", bitmap.getByteCount() + "");
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                iv_ThumbNail.setImageBitmap(bitmap);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void SaveFlower(View view) {
        EditText et_FlowerName = findViewById(R.id.etFlowerName);
        EditText et_FlowerPrice = findViewById(R.id.etFlowerPrice);
        EditText et_FlowerInstructions = findViewById(R.id.et_Flower_Instruct);
        Spinner sp_FlowerCategory = findViewById(R.id.spFlowerCategory);

        String flower_name = et_FlowerName.getText().toString();
        String flower_price = et_FlowerPrice.getText().toString();
        String flower_instruction = et_FlowerInstructions.getText().toString();
        String flower_Category = sp_FlowerCategory.getSelectedItem().toString();


        FlowerModel flower = new FlowerModel();
        if (!flower_name.isEmpty()) {
            flower.setName(flower_name);
            flower.setPhoto(flower_name + ".jpg");
        }
        if (!flower_price.isEmpty())
        {
            flower.setPrice(Double.parseDouble(flower_price));
        }
        if (!flower_Category.isEmpty()) {
            flower.setCategory(flower_Category);
        }
        if (!flower_instruction.isEmpty()) {
            flower.setInstructions(flower_instruction);
        }
        if (bitmap != null) {
            flower.setBitmap(bitmap);
        }

        FlowerViewModel mFlowerViewModel = ViewModelProviders.of(this).get(FlowerViewModel.class);
        mFlowerViewModel.insert(flower);
        FileSaver.SaveFile(this,flower);
        finish();
    }


}




