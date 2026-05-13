package com.mycompany.quanlyhocphi.model;

public class ThongKeModel {
    public String maSV;
    public String hoTen;
    public String lop;
    public String khoa;

    public double tongHocPhi;
    public double daThu;
    public double conNo;

    public ThongKeModel(String maSV, String hoTen, String lop, String khoa,
                        double tongHocPhi, double daThu, double conNo) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.lop = lop;
        this.khoa = khoa;
        this.tongHocPhi = tongHocPhi;
        this.daThu = daThu;
        this.conNo = conNo;
    }
}
