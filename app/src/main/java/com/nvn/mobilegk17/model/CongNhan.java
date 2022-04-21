package com.nvn.mobilegk17.model;

import java.io.Serializable;

public class CongNhan implements Serializable {
    String maCN;
    String hoCN;
    String tenCN;
    String phanXuong;
    String ngaySinh;
    String ImageSrc;



    public CongNhan(String maCN, String hoCN, String tenCN, String phanXuong,  String ngaySinh,String ImageSrc) {
        this.maCN = maCN;
        this.hoCN = hoCN;
        this.tenCN = tenCN;
        this.phanXuong = phanXuong;

        this.ngaySinh = ngaySinh;
        this.ImageSrc=ImageSrc;
    }

    public CongNhan(String cNnew) {

    }

    public String getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(String imageSrc) {
        ImageSrc = imageSrc;
    }

    public String getMaCN() {
        return maCN;
    }

    public void setMaCN(String maCN) {
        this.maCN = maCN;
    }

    public String getHoCN() {
        return hoCN;
    }

    public void setHoCN(String hoCN) {
        this.hoCN = hoCN;
    }

    public String getTenCN() {
        return tenCN;
    }

    public void setTenCN(String tenCN) {
        this.tenCN = tenCN;
    }

    public String getPhanXuong() {
        return phanXuong;
    }

    public void setPhanXuong(String phanXuong) {
        this.phanXuong = phanXuong;
    }


    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
