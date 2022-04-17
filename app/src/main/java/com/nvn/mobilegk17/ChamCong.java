package com.nvn.mobilegk17;

import java.io.Serializable;

public class ChamCong implements Serializable {
    private String maCongNhan;
    private String maChamCong;
    private String ngayChamCong;

    public ChamCong() {

    }

    public ChamCong(String maCongNhan, String maChamCong, String ngayChamCong) {
        this.maCongNhan = maCongNhan;
        this.maChamCong = maChamCong;
        this.ngayChamCong = ngayChamCong;
    }

    public String getMaCongNhan() {
        return maCongNhan;
    }

    public String getMaChamCong() {
        return maChamCong;
    }

    public String getNgayChamCong() {
        return ngayChamCong;
    }

    public void setMaCongNhan(String maCongNhan) {
        this.maCongNhan = maCongNhan;
    }

    public void setMaChamCong(String maChamCong) {
        this.maChamCong = maChamCong;
    }

    public void setNgayChamCong(String ngayChamCong) {
        this.ngayChamCong = ngayChamCong;
    }

    @Override
    public String toString() {
        return "ChamCongDB{" +
                "maCongNhan='" + maCongNhan + '\'' +
                ", maChamCong='" + maChamCong + '\'' +
                ", ngayChamCong='" + ngayChamCong + '\'' +
                '}';
    }
}
