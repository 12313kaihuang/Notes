package com.example.tests.ipc.serialization;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @auther hy
 * create on 2021/08/01 下午1:58
 */
public class ParcelableObject implements Parcelable {
    int id;
    String name;

    public ParcelableObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected ParcelableObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<ParcelableObject> CREATOR = new Creator<ParcelableObject>() {
        @Override
        public ParcelableObject createFromParcel(Parcel in) {
            return new ParcelableObject(in);
        }

        @Override
        public ParcelableObject[] newArray(int size) {
            return new ParcelableObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
