package com.example.sdp1;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private Uri imagePath;

    public Image(Uri image){
        setImage(image);
    }

    protected Image(Parcel in) {
        imagePath = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public void setImage(Uri imageString){
        this.imagePath = imageString;
    }
    public Uri getImage(){
        return this.imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imagePath,flags);
    }
}
