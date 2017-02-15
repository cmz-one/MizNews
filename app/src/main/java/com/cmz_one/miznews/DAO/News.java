package com.cmz_one.miznews.DAO;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by cmz_o on 2017/2/14.
 */
@Entity
public class News {

    @Id()
    private String id;
    private String title;
    private String date;
    private String category;
    private String author_name;
    private String url;
    private String thumbnail_pic_s;
    private String thumbnail_pic_s02;
    @Transient
    private int tempUsageCount;
    @Generated(hash = 1221039535)
    public News(String id, String title, String date, String category,
            String author_name, String url, String thumbnail_pic_s,
            String thumbnail_pic_s02) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.category = category;
        this.author_name = author_name;
        this.url = url;
        this.thumbnail_pic_s = thumbnail_pic_s;
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }
    @Generated(hash = 1579685679)
    public News() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getAuthor_name() {
        return this.author_name;
    }
    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getThumbnail_pic_s() {
        return this.thumbnail_pic_s;
    }
    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }
    public String getThumbnail_pic_s02() {
        return this.thumbnail_pic_s02;
    }
    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }
}
