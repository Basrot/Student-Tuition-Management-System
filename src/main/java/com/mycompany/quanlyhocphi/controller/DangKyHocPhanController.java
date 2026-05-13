package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.DangKyHocPhanPanel;
import com.mycompany.quanlyhocphi.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DangKyHocPhanController {

    private DangKyHocPhanPanel view;

    public DangKyHocPhanController(DangKyHocPhanPanel view) {
        this.view = view;
        init();
        event();
    }

    // ================= INIT =================
    private void init() {
        loadKhoa();
        loadHocKy();
        loadKhoaHoc();

        // AUTO LOAD CHUỖI
        if (view.cbKhoa.getItemCount() > 0) {
            view.cbKhoa.setSelectedIndex(0);
            loadNganh();
        }
        if (view.cbNganh.getItemCount() > 0) {
            view.cbNganh.setSelectedIndex(0);
            loadLop();
        }

        loadTable();
    }

    // ================= EVENTS =================
    private void event() {
        view.cbKhoa.addActionListener(e -> loadNganh());
        view.cbNganh.addActionListener(e -> loadLop());
        view.cbMon.addActionListener(e -> loadSTC());
        view.btnTaoLHP.addActionListener(e -> taoVaDangKyLHP());
        view.btnXoa.addActionListener(e -> xoaLHP());
    }

    // ================= LOAD DATA =================
    private void loadKhoa() {
        loadCombo(view.cbKhoa, "SELECT MaKhoa FROM khoa");
    }

    private void loadNganh() {
        view.cbNganh.removeAllItems();

        if (view.cbKhoa.getSelectedItem() == null) return;

        String maKhoa = view.cbKhoa.getSelectedItem().toString();

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("SELECT MaNganh FROM nganh WHERE MaKhoa=?");
            ps.setString(1, maKhoa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                view.cbNganh.addItem(rs.getString("MaNganh"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (view.cbNganh.getItemCount() > 0) {
            view.cbNganh.setSelectedIndex(0);
            loadLop();
        }
    }

    private void loadLop() {
        view.cbLop.removeAllItems();

        if (view.cbNganh.getSelectedItem() == null) return;

        String maNganh = view.cbNganh.getSelectedItem().toString();

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("SELECT MaLop FROM lop WHERE MaNganh=?");
            ps.setString(1, maNganh);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                view.cbLop.addItem(rs.getString("MaLop"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHocKy() {
        loadCombo(view.cbHocKy, "SELECT MaHocKi FROM hocki");
    }

    private void loadKhoaHoc() {
        view.cbKhoaHoc.removeAllItems();
        view.cbKhoaHoc.addItem("74");
        view.cbKhoaHoc.addItem("75");
        view.cbKhoaHoc.addItem("76");
    }

    private void loadSTC() {
        if (view.cbMon.getSelectedItem() == null) return;

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("SELECT SoTinChi FROM monhoc WHERE MaMon=?");
            ps.setString(1, view.cbMon.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                view.txtSTC.setText(rs.getString("SoTinChi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CORE LOGIC =================
    private void taoVaDangKyLHP() {
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);

            String maLHP = view.txtMaLHP.getText();
            String maLop = view.cbLop.getSelectedItem().toString();

            // 1. TẠO LHP
            PreparedStatement psLHP = c.prepareStatement("""
                INSERT INTO lophocphan
                (MaLHP, MaMon, TenHP, MaHocKi, MaGV, SoTinChi)
                VALUES (?,?,?,?,?,?)
            """);
            psLHP.setString(1, maLHP);
            psLHP.setString(2, view.cbMon.getSelectedItem().toString());
            psLHP.setString(3, view.txtTenHP.getText());
            psLHP.setString(4, view.cbHocKy.getSelectedItem().toString());
            psLHP.setString(5, view.cbGV.getSelectedItem().toString());
            psLHP.setInt(6, Integer.parseInt(view.txtSTC.getText()));
            psLHP.executeUpdate();

            // 2. GẮN LỚP – HỌC PHẦN
            PreparedStatement psMap =
                c.prepareStatement("INSERT INTO lop_dkhocphan VALUES (?,?)");
            psMap.setString(1, maLop);
            psMap.setString(2, maLHP);
            psMap.executeUpdate();

            // 3. ĐĂNG KÝ CHỈ SV CHƯA CÓ
            PreparedStatement psDK = c.prepareStatement("""
                INSERT INTO dangkyhocphan (MaDK, MaSV, MaLHP, NgayDangKy)
                SELECT 
                    CONCAT('DK', sv.MaSV, UNIX_TIMESTAMP()),
                    sv.MaSV,
                    ?,
                    CURDATE()
                FROM sinhvien sv
                WHERE sv.MaLop=?
                AND NOT EXISTS (
                    SELECT 1 FROM dangkyhocphan dk
                    WHERE dk.MaSV=sv.MaSV AND dk.MaLHP=?
                )
            """);

            psDK.setString(1, maLHP);
            psDK.setString(2, maLop);
            psDK.setString(3, maLHP);

            int soSVMoi = psDK.executeUpdate();

            // 4. CẬP NHẬT SĨ SỐ
            PreparedStatement psSiSo = c.prepareStatement("""
                UPDATE lophocphan
                SET SiSoHienTai = SiSoHienTai + ?
                WHERE MaLHP=?
            """);
            psSiSo.setInt(1, soSVMoi);
            psSiSo.setString(2, maLHP);
            psSiSo.executeUpdate();

            c.commit();
            loadTable();

            JOptionPane.showMessageDialog(view,
                "Hoàn tất! Đã đăng ký thêm " + soSVMoi + " sinh viên");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi nghiệp vụ!");
        }
    }

    // ================= DELETE =================
    private void xoaLHP() {
        int r = view.table.getSelectedRow();
        if (r == -1) return;

        String ma = view.table.getValueAt(r, 0).toString();
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("DELETE FROM lophocphan WHERE MaLHP=?");
            ps.setString(1, ma);
            ps.executeUpdate();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= TABLE =================
    private void loadTable() {
        DefaultTableModel m = view.model;
        m.setRowCount(0);

        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery("""
                SELECT MaLHP, TenHP, MaHocKi, SiSoHienTai
                FROM lophocphan
            """);
            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getString("MaLHP"),
                    rs.getString("TenHP"),
                    "",
                    rs.getString("MaHocKi"),
                    "",
                    rs.getInt("SiSoHienTai")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCombo(JComboBox<String> cb, String sql) {
        cb.removeAllItems();
        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery(sql);
            while (rs.next()) cb.addItem(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
