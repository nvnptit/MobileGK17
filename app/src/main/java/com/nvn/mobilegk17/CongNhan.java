package com.nvn.mobilegk17;

public class CongNhan {
    String maCN;
    String hoCN;
    String tenCN;
    String phanXuong;
    String HinhAnh;

    public CongNhan() {

    }

    public CongNhan(String maCN, String hoCN, String tenCN, String phanXuong, String hinhAnh) {
        this.maCN = maCN;
        this.hoCN = hoCN;
        this.tenCN = tenCN;
        this.phanXuong = phanXuong;
        this.HinhAnh = hinhAnh;
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

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.HinhAnh = hinhAnh;
    }
}
