package com.example.sdp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {
    private Employee employee;
    private ImageView completedImage;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        this.employee = intent.getParcelableExtra("employee");
        setContentView(R.layout.activity_image);
        this.completedImage = this.findViewById(R.id.completedImage);
        Image image = this.employee.getImage();
        this.completedImage.setImageURI(image.getImage());
    }
    public void shareImage(View view){
        Image image = this.employee.getImage();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");

        intent.putExtra(Intent.EXTRA_STREAM, image.getImage());
        startActivity(Intent.createChooser(intent , "Share"));
    }
}
