package com.mycompany.quanlyhocphi.controller;


import com.mycompany.quanlyhocphi.panel.Phieudangky_panel;
import com.mycompany.quanlyhocphi.util.DBUtil;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.sql.*;

public class Phieudangky_controller {

    private Phieudangky_panel view;

    public Phieudangky_controller(Phieudangky_panel view) {
        this.view = view;
        init();
    }

    private void init() {
        loadSinhVienDangChon();
        loadLopHocPhanTheoNganh();
        tinhTongTien();
        view.btnLuuPhieu.addActionListener(e -> luuPhieuDangKy());
    }

    // ================= LOAD SINH VIÊN =================
    private void loadSinhVienDangChon() {
        // giả sử bạn đã có MaSV đang chọn
        String maSV = "74DCHT21001";

        try (Connection con = DBUtil.getConnection()) {
            String sql = """
                SELECT sv.MaSV, sv.HoTen, l.TenLop, n.TenNganh, k.TenKhoa
                FROM sinhvien sv
                JOIN lop l ON sv.MaLop = l.MaLop
                JOIN nganh n ON l.MaNganh = n.MaNganh
                JOIN khoa k ON n.MaKhoa = k.MaKhoa
                WHERE sv.MaSV = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                view.txtMaSV.setText(rs.getString(1));
                view.txtHoTen.setText(rs.getString(2));
                view.txtLop.setText(rs.getString(3));
                view.txtNganh.setText(rs.getString(4));
                view.txtKhoa.setText(rs.getString(5));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= LOAD LHP =================
    private void loadLopHocPhanTheoNganh() {
        view.modelLHP.setRowCount(0);
        String nganh = view.txtNganh.getText();

        try (Connection con = DBUtil.getConnection()) {
            String sql = """
                SELECT MaLHP, TenMon, GiangVien, SoTinChi, DonGia
                FROM lophocphan
                WHERE TenNganh = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nganh);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int tc = rs.getInt("SoTinChi");
                double dg = rs.getDouble("DonGia");
                view.modelLHP.addRow(new Object[]{
                        false,
                        rs.getString("MaLHP"),
                        rs.getString("TenMon"),
                        rs.getString("GiangVien"),
                        tc,
                        dg,
                        tc * dg
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= TÍNH TỔNG =================
    private void tinhTongTien() {
        view.modelLHP.addTableModelListener(e -> {
            int tongTC = 0;
            double tongTien = 0;

            for (int i = 0; i < view.modelLHP.getRowCount(); i++) {
                Boolean chk = (Boolean) view.modelLHP.getValueAt(i, 0);
                if (chk != null && chk) {
                    tongTC += (int) view.modelLHP.getValueAt(i, 4);
                    tongTien += (double) view.modelLHP.getValueAt(i, 6);
                }
            }

            view.txtTongTinChi.setText(String.valueOf(tongTC));
            view.txtTongHocPhi.setText(String.format("%,.0f", tongTien));
        });
    }

    // ================= LƯU PHIẾU =================
    private void luuPhieuDangKy() {
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            String insertPhieu = """
                INSERT INTO phieudangky(MaSV, HocKy, TongTinChi, TongHocPhi)
                VALUES (?, ?, ?, ?)
            """;

            PreparedStatement psPhieu = con.prepareStatement(insertPhieu, Statement.RETURN_GENERATED_KEYS);
            psPhieu.setString(1, view.txtMaSV.getText());
            psPhieu.setString(2, "HK1");
            psPhieu.setInt(3, Integer.parseInt(view.txtTongTinChi.getText()));
            psPhieu.setDouble(4, Double.parseDouble(view.txtTongHocPhi.getText().replace(",", "")));
            psPhieu.executeUpdate();

            ResultSet rs = psPhieu.getGeneratedKeys();
            rs.next();
            int maPhieu = rs.getInt(1);

            String insertCT = """
                INSERT INTO ct_phieudangky(MaPhieu, MaLHP, SoTinChi, DonGia)
                VALUES (?, ?, ?, ?)
            """;

            PreparedStatement psCT = con.prepareStatement(insertCT);

            for (int i = 0; i < view.modelLHP.getRowCount(); i++) {
                if ((Boolean) view.modelLHP.getValueAt(i, 0)) {
                    psCT.setInt(1, maPhieu);
                    psCT.setString(2, view.modelLHP.getValueAt(i, 1).toString());
                    psCT.setInt(3, (int) view.modelLHP.getValueAt(i, 4));
                    psCT.setDouble(4, (double) view.modelLHP.getValueAt(i, 5));
                    psCT.addBatch();
                }
            }

            psCT.executeBatch();
            con.commit();
            JOptionPane.showMessageDialog(view, "Lưu phiếu đăng ký thành công");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi lưu phiếu");
        }
    }
}
