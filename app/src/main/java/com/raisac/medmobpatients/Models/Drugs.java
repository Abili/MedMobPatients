package com.raisac.medmobpatients.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Drugs implements Parcelable {
   private String name;
   private String price;
   private int imageUrl;

    public Drugs() {
    }

    public Drugs(String name, String price, int imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    protected Drugs(Parcel in) {
        name = in.readString();
        price = in.readString();
        imageUrl = in.readInt();
    }

    public static final Creator<Drugs> CREATOR = new Creator<Drugs>() {
        @Override
        public Drugs createFromParcel(Parcel in) {
            return new Drugs(in);
        }

        @Override
        public Drugs[] newArray(int size) {
            return new Drugs[size];
        }
    };

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(imageUrl);
    }
}
