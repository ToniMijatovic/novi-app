package com.example.sdp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GadgetActivity extends AppCompatActivity {
    private ImageView editImage;
    private Uri chosenImage;
    private ImageView outside_imageview;
    private RelativeLayout mergingFrame;
    private Gadgets gadgets = new Gadgets();
    private TextView employeeText;
    private int gadgetIndex = 0;
    private Employee employee;
    private static final int SAVE_TO_GALLERY = 909;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initGadgets();
        Intent intent = this.getIntent();
        this.employee = intent.getParcelableExtra("employee");
        setContentView(R.layout.activity_gadget);
        this.editImage = this.findViewById(R.id.inside_imageview);
        this.mergingFrame = this.findViewById(R.id.mergingFrame);
        this.outside_imageview = this.findViewById(R.id.outside_imageview);
        this.employeeText = this.findViewById(R.id.employeeText);
        this.employeeText.setText(this.employee.getName() + " is medewerker van de maand "+ this.employee.getMonth()+" bij Novi!" );

        Image image = this.employee.getImage();
        chosenImage = image.getImage();

        this.editImage.setImageURI(chosenImage);
        this.outside_imageview.setImageURI(this.gadgets.getGadgets().get(0).getGadgetUri());

    }
    public void previousGadget(View view){
        ArrayList<Gadget> gadgets = this.gadgets.getGadgets();
        if(this.gadgetIndex != 0){
            gadgetIndex--;
            this.outside_imageview.setImageURI(this.gadgets.getGadgets().get(gadgetIndex).getGadgetUri());
        }else if(this.gadgetIndex == 0){
            gadgetIndex = 4;
            this.outside_imageview.setImageURI(this.gadgets.getGadgets().get(gadgetIndex).getGadgetUri());
        }
    }
    public void nextGadget(View view){
        ArrayList<Gadget> gadgets = this.gadgets.getGadgets();
        if(this.gadgetIndex < gadgets.size() && this.gadgetIndex != (gadgets.size() - 1)){
            gadgetIndex++;
            this.outside_imageview.setImageURI(this.gadgets.getGadgets().get(gadgetIndex).getGadgetUri());
        }else if(this.gadgetIndex == (gadgets.size() - 1) ){
            gadgetIndex = 0;
            this.outside_imageview.setImageURI(this.gadgets.getGadgets().get(gadgetIndex).getGadgetUri());
        }
    }
    public void initGadgets(){
        for(int x = 0; x < 5; x++){
            Gadget gadget = new Gadget(Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/frame"+x));
            this.gadgets.setGadget(gadget);
        }
    }
    public void mergeImages(View view)
    {
        try {
            if (ActivityCompat.checkSelfPermission(GadgetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GadgetActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_TO_GALLERY);
            } else {
                Uri mergedImage = this.storeImage(this.getBitmapFromView(this.mergingFrame));
                Image image = new Image(mergedImage);
                this.employee.setImage(image);
                // start the SecondActivity
                Intent intent = new Intent(this, ImageActivity.class);
                intent.putExtra("employee", this.employee);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    public Uri storeImage(Bitmap image) {
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        // Save image to gallery
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                image,
                timeStamp,
                ""
        );
        return Uri.parse(savedImageURL);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SAVE_TO_GALLERY: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permissions are granted, re execute the click function.
                    this.mergeImages(getWindow().getDecorView().getRootView());
                }
                return;
            }
        }
    }
}
