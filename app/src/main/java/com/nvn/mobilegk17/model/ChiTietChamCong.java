package com.nvn.mobilegk17.model;

import java.io.Serializable;

public class ChiTietChamCong implements Serializable {
    private String maChamCong;

    private String maSanPham;
    private String tenSanPham;
    private int soLuongThanhPham;
    private int soLuongPhePham;

    public ChiTietChamCong() {
    }

    public ChiTietChamCong(String maChamCong, String maSanPham, String tenSanPham, int soLuongThanhPham, int soLuongPhePham) {
        this.maChamCong = maChamCong;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuongThanhPham = soLuongThanhPham;
        this.soLuongPhePham = soLuongPhePham;
    }

    public String getMaChamCong() {
        return maChamCong;
    }

    public void setMaChamCong(String maChamCong) {
        this.maChamCong = maChamCong;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuongThanhPham() {
        return soLuongThanhPham;
    }

    public void setSoLuongThanhPham(int soLuongThanhPham) {
        this.soLuongThanhPham = soLuongThanhPham;
    }

    public int getSoLuongPhePham() {
        return soLuongPhePham;
    }

    public void setSoLuongPhePham(int soLuongPhePham) {
        this.soLuongPhePham = soLuongPhePham;
    }
}
