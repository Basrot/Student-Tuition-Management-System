/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.SVDKTC_panel;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
/**
 *
 * @author Admin
 */
public class SVDKTC_controller {
     private SVDKTC_panel view;
    private Connection conn;
    private String maSV;

    // 🔑 constructor nhận MSV
    public SVDKTC_controller(SVDKTC_panel view, String maSV) {
//        this.view = view;
//        this.maSV = maSV;
        connectDB();
        loadData();
        addEvent();
}

    
    private void connectDB() {
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
    
    private void loadData() {

    // ===== RESET TABLE =====
    view.modelMonMo.setRowCount(0);
    view.modelDaDK.setRowCount(0);

    // ===== 1. LOAD MÔN HỌC MỞ (LỌC THEO SINH VIÊN) =====
    String sqlMonMo = """
        SELECT *
        FROM phieudangky p
        WHERE NOT EXISTS (
            SELECT 1
            FROM sv_dangky s
            WHERE s.MaSV = ?
              AND s.MaHocPhan = p.MaHocPhan
        )
    """;

    try (PreparedStatement ps = conn.prepareStatement(sqlMonMo)) {
        ps.setString(1, maSV);   // 🔑 BẮT BUỘC
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            view.modelMonMo.addRow(new Object[]{
                rs.getString("MaPhieu"),
                rs.getString("MaHocPhan"),
                rs.getString("TenHocPhan"),
                rs.getDate("NgayDangKy"),
                rs.getInt("HocKy"),
                rs.getInt("SoTinChi")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // ===== 2. LOAD MÔN ĐÃ ĐĂNG KÝ =====
    String sqlDaDK = "SELECT * FROM sv_dangky WHERE MaSV = ?";

    try (PreparedStatement ps = conn.prepareStatement(sqlDaDK)) {
        ps.setString(1, maSV);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            view.modelDaDK.addRow(new Object[]{
                rs.getString("MaPhieu"),
                rs.getString("MaHocPhan"),
                rs.getString("TenHocPhan"),
                rs.getDate("NgayDangKy"),
                rs.getInt("HocKy"),
                rs.getInt("SoTinChi")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void addEvent() {
        view.btnDK.addActionListener(e -> dangKyMon());
        view.btnHUY.addActionListener(e -> huyDangKy());
        view.btnTK.addActionListener(e -> timKiemDaDangKy());
    }
    
   private void timKiemDaDangKy() {

    String keyword = view.txtTK.getText().trim();

    // Nếu không nhập gì → load lại toàn bộ danh sách đã đăng ký
    if (keyword.isEmpty()) {
        loadData();   // load lại từ DB
        return;
    }

    // ❗ CHỈ XOÁ DỮ LIỆU BẢNG DƯỚI
    view.modelDaDK.setRowCount(0);

    String sql = """
        SELECT *
        FROM sv_dangky
        WHERE MaSV = ?
          AND (MaPhieu LIKE ? OR MaHocPhan LIKE ? OR TenHocPhan LIKE ?)
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maSV);
        ps.setString(2, "%" + keyword + "%");
        ps.setString(3, "%" + keyword + "%");
        ps.setString(4, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            view.modelDaDK.addRow(new Object[]{
                rs.getString("MaPhieu"),
                rs.getString("MaHocPhan"),
                rs.getString("TenHocPhan"),
                rs.getDate("NgayDangKy"),
                rs.getInt("HocKy"),
                rs.getInt("SoTinChi")
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


private void dangKyMon() {

        int row = view.table1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Chọn môn để đăng ký");
            return;
        }

        String maPhieu = view.modelMonMo.getValueAt(row, 0).toString();
        String maHP    = view.modelMonMo.getValueAt(row, 1).toString();
        String tenHP   = view.modelMonMo.getValueAt(row, 2).toString();
        Object ngayDK  = view.modelMonMo.getValueAt(row, 3);
        String hocKy   = view.modelMonMo.getValueAt(row, 4).toString();
        int soTC       = Integer.parseInt(
        view.modelMonMo.getValueAt(row, 5).toString()
        );

        // check trùng
        String check = "SELECT 1 FROM sv_dangky WHERE MaSV=? AND MaHocPhan=?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setString(1, maSV);
            ps.setString(2, maHP);
            if (ps.executeQuery().next()) {
                JOptionPane.showMessageDialog(view, "Môn này đã đăng ký");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // insert
        String sql = """
            
            INSERT INTO sv_dangky
            (MaSV, MaPhieu, MaHocPhan, TenHocPhan, NgayDangKy, HocKy, SoTinChi)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
        ps.setString(1, maSV);
        ps.setString(2, maPhieu);
        ps.setString(3, maHP);
        ps.setString(4, tenHP);
        ps.setDate  (5, java.sql.Date.valueOf(ngayDK.toString()));
        ps.setString(6, hocKy);
        ps.setInt   (7, soTC);
        ps.executeUpdate();

            // cập nhật giao diện
            view.modelDaDK.addRow(new Object[]{
            maPhieu,
            maHP,
            tenHP,
            ngayDK,
            hocKy,
            soTC
});

            view.modelMonMo.removeRow(row);

            JOptionPane.showMessageDialog(view, "Đăng ký thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void huyDangKy() {

    int row = view.table2.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(view, "Chọn môn để huỷ");
        return;
    }

    String maPhieu = view.modelDaDK.getValueAt(row, 0).toString();
    String maHP    = view.modelDaDK.getValueAt(row, 1).toString();
    String tenHP   = view.modelDaDK.getValueAt(row, 2).toString();
    Object ngayDK  = view.modelDaDK.getValueAt(row, 3);
    String hocKy   = view.modelDaDK.getValueAt(row, 4).toString();
    int soTC       = Integer.parseInt(
                        view.modelDaDK.getValueAt(row, 5).toString()
                    );

    String sql = "DELETE FROM sv_dangky WHERE MaSV=? AND MaHocPhan=?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maSV);
        ps.setString(2, maHP);

        int deleted = ps.executeUpdate();

        if (deleted == 0) {
            JOptionPane.showMessageDialog(view, "Huỷ thất bại (không tìm thấy dữ liệu)");
            return;
        }

        // Đưa lại môn vào bảng mở
        view.modelMonMo.addRow(new Object[]{
            maPhieu,
            maHP,
            tenHP,
            ngayDK,
            hocKy,
            soTC
        });

        // Xoá khỏi bảng đã đăng ký
        view.modelDaDK.removeRow(row);

        JOptionPane.showMessageDialog(view, "Huỷ đăng ký thành công");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
    

