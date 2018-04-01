package com.develop.filhan.filhandennis_1202150079_modul6;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 31/03/2018.
 */

@IgnoreExtraProperties
public class PhotoModel implements Parcelable {
    private String user, title, caption, id;
    private String imgUrl;
    private int like;

    public PhotoModel() {
    }

    public PhotoModel(String user, String title, String caption, String imgUrl, int like) {
        this.user = user;
        this.title = title;
        this.caption = caption;
        this.imgUrl = imgUrl;
        this.like = like;
        //this.comments = new ArrayList<>();
    }

    public static final Creator<PhotoModel> CREATOR = new Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel in) {
            return new PhotoModel(in);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getDisplayName() {
        String email = this.user;
        return email.substring(0, email.lastIndexOf("@"));
    }


    public int getCommentsCount() {
        return 0;
    }

    public PhotoModel(Parcel in) {
        this.id = in.readString();
        this.user = in.readString();
        this.title = in.readString();
        this.caption = in.readString();
        this.imgUrl = in.readString();
        this.like = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.user);
        parcel.writeString(this.title);
        parcel.writeString(this.caption);
        parcel.writeString(this.imgUrl);
        parcel.writeInt(this.like);
    }

}
