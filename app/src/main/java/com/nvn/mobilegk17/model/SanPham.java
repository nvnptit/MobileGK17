package com.nvn.mobilegk17.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    String maSP,tenSP,donGia,hinhSP;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String donGia, String hinhSP) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.hinhSP = hinhSP;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
    }

    public String getHinhSP() {
        return hinhSP;
    }

    public void setHinhSP(String hinhSP) {
        this.hinhSP = hinhSP;
    }
}
