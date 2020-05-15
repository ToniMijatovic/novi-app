package com.example.sdp1;

import android.os.Parcel;
import android.os.Parcelable;


public class Employee extends Person implements Parcelable {
    public String month;
    public Image image;
//
    public Employee(String name, String month){
        super(name);
        this.setMonth(month);
    }

    protected Employee(Parcel in) {
        super(in);
        month = in.readString();
        image = in.readParcelable(Image.class.getClassLoader());
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override()
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public void setImage(Image image){
        this.image = image;
    }
    public void setMonth(String month){
        this.month = month;
    }
    public Image getImage(){
        return this.image;
    }
    public String getMonth(){
        return this.month;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(month);
        dest.writeParcelable(image, flags);
    }
}
