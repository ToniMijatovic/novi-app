package com.example.sdp1;

import android.os.Parcel;
import android.os.Parcelable;

abstract class Person implements Parcelable {
    public String name;
    public Person(String name){
        this.setName(name);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
    protected Person(Parcel in){
        name = in.readString();
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
