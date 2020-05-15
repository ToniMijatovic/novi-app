package com.example.sdp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Employees employees = new Employees();
    /**
     * This string array is an example that this could be any data, such as a response from an API or a SQL result set.
     */
    private String[] employeeNames = {"Karel", "Henk", "Claudia", "Marloes", "Andre", "Peter", "Josefien"};
    private static final int CAMERA_REQUEST = 6969;
    private static final int CAMERA_REQUEST_PERMISSION_REQUEST = 030;
    private static final int PICK_FROM_GALLERY = 040;
    public  Employee selectedEmployee;
    private ImageView imageView;
    private Button nextButton;
    private TextView previewText;
    private Spinner employeeSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeDummyEmployees();
        setContentView(R.layout.activity_main);
        this.employeeSpinner = findViewById(R.id.employeeSpinner);
        ArrayAdapter<Employee> adapter =
                new ArrayAdapter<Employee>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, this.employees.getEmployees());
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        this.employeeSpinner.setAdapter(adapter);

        this.employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK); //Put default text color to black because its white on android 6.

                if(selectedEmployee != null){
                    Image image = selectedEmployee.getImage();
                    selectedEmployee = employees.getEmployees().get( employeeSpinner.getSelectedItemPosition() );
                    selectedEmployee.setImage(image);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.imageView = this.findViewById(R.id.imagePreview);
        this.nextButton = this.findViewById(R.id.nextButton);
        this.previewText = this.findViewById(R.id.previewText);
    }
    protected void initializeDummyEmployees(){

        Calendar mCalendar = Calendar.getInstance();
        String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        for(int x = 0; x < this.employeeNames.length; x++){
            Employee employee = new Employee( this.employeeNames[x], month);
            employees.setEmployee(employee);
        }

    }
    /** Called when the user touches the button */
    public void openCamera(View view)
    {
        Context context = getApplicationContext();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_REQUEST_PERMISSION_REQUEST);
        }else{
            Intent camera_intent
                    = new Intent(MediaStore
                    .ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, CAMERA_REQUEST);
        }
    }
    public void openGallery(View view){
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goToNextView(View view){
        // start the SecondActivity
        Intent intent = new Intent(this, GadgetActivity.class);
        intent.putExtra("employee", this.selectedEmployee);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permissions are granted, re execute the click function.
                    this.openCamera(getWindow().getDecorView().getRootView());
                }
                return;
            }
            case PICK_FROM_GALLERY: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.openGallery(getWindow().getDecorView().getRootView());
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Employee selectedEmployee = this.employees.getEmployees().get( employeeSpinner.getSelectedItemPosition() );

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri storedImage = this.storeImage(photo);
            Image image = new Image(storedImage);
            imageView.setImageBitmap(photo);
            selectedEmployee.setImage(image);
            imageView.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            previewText.setVisibility(View.VISIBLE);
            this.selectedEmployee = selectedEmployee;
        }else if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null){
            Uri imageUri=data.getData();
            try {
                Employee selectedEmployee = this.employees.getEmployees().get( employeeSpinner.getSelectedItemPosition() );
                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                Image image = new Image(imageUri);
                imageView.setImageBitmap(photo);
                selectedEmployee.setImage(image);
                imageView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                previewText.setVisibility(View.VISIBLE);
                this.selectedEmployee = selectedEmployee;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
}
