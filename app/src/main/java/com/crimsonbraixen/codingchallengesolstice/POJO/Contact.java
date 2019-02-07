package com.crimsonbraixen.codingchallengesolstice.POJO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import lombok.Data;

@Data
public class Contact implements Comparable<Contact>, Parcelable {

    public String name;
    public int id;
    public String companyName;
    public boolean isFavorite;
    public String smallImageURL;
    public String largeImageURL;
    public String emailAddress;
    public String birthdate;
    public Phone phones;
    public Address address;

    public Bitmap smallImage;
    public Bitmap largeImage;


    public Contact() {}

    private Contact(Parcel in) {
        name = in.readString();
        id = in.readInt();
        companyName = in.readString();
        isFavorite = in.readByte() != 0;
        smallImageURL = in.readString();
        largeImageURL = in.readString();
        emailAddress = in.readString();
        birthdate = in.readString();
        smallImage = in.readParcelable(Bitmap.class.getClassLoader());
        largeImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int compareTo(@NonNull Contact contact) {
        return this.getName().compareTo(contact.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeString(companyName);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeString(smallImageURL);
        parcel.writeString(largeImageURL);
        parcel.writeString(emailAddress);
        parcel.writeString(birthdate);
        parcel.writeParcelable(smallImage, i);
        parcel.writeParcelable(largeImage, i);
    }
}


