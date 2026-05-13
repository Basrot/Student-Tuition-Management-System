/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.model;
import java.sql.*;

/**
 *
 * @author Admin
 */
public class SinhVienModel {
     private Connection conn;

    public SinhVienModel() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quanlyhocphi",
                "root",
                ""
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LẤY THÔNG TIN SINH VIÊN
    public ResultSet getSinhVien(String username) throws SQLException {
        String sql = """
            SELECT sv.MaSV, sv.HoTen, sv.NgaySinh, sv.GioiTinh,
                   sv.MaLop, k.TenKhoa, a.email
            FROM sinhvien sv
            JOIN khoa k ON sv.MaKhoa = k.MaKhoa
            LEFT JOIN account a ON sv.Username = a.username
            WHERE sv.Username = ?
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        return ps.executeQuery();
    }

    // CẬP NHẬT THÔNG TIN SINH VIÊN
    public void updateSinhVien(
        String username,
        String hoTen,
        Date ngaySinh,
        String gioiTinh,
        String maLop
) throws SQLException {

    String sql = """
        UPDATE sinhvien
        SET HoTen=?, NgaySinh=?, GioiTinh=?, MaLop=?
        WHERE Username=?
    """;

    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, hoTen);
    ps.setDate(2, ngaySinh);
    ps.setString(3, gioiTinh);
    ps.setString(4, maLop);
    ps.setString(5, username);
    ps.executeUpdate();
}

}
    

