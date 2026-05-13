package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.SinhVienPanel;
import com.mycompany.quanlyhocphi.util.DBUtil;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Random;

public class SinhVienController {

    private SinhVienPanel view;

    public SinhVienController(SinhVienPanel view) {
        this.view = view;
        loadKhoa();
        loadNganhTheoKhoa();
        loadSinhVien();
        suKien();
    }

    // ================= SỰ KIỆN =================
    private void suKien() {

        view.cbKhoa.addActionListener(e -> loadNganhTheoKhoa());

        view.btnAdd.addActionListener(e -> addSinhVien());
        view.btnUpdate.addActionListener(e -> updateSinhVien());
        view.btnDelete.addActionListener(e -> deleteSinhVien());

        view.table.getSelectionModel().addListSelectionListener(e -> {
            int r = view.table.getSelectedRow();
            if (r == -1) return;
            view.txtMaSV.setText(view.table.getValueAt(r, 0).toString());
            view.txtHoTen.setText(view.table.getValueAt(r, 1).toString());
        });
    }

    // ================= LOAD =================
    private void loadKhoa() {
        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement()
                    .executeQuery("SELECT MaKhoa FROM khoa");
            view.cbKhoa.removeAllItems();
            while (rs.next()) {
                view.cbKhoa.addItem(rs.getString("MaKhoa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNganhTheoKhoa() {
        if (view.cbKhoa.getSelectedItem() == null) return;

        String maKhoa = view.cbKhoa.getSelectedItem().toString();

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "SELECT MaNganh FROM nganh WHERE MaKhoa=?"
            );
            ps.setString(1, maKhoa);
            ResultSet rs = ps.executeQuery();

            view.cbNganh.removeAllItems();
            while (rs.next()) {
                view.cbNganh.addItem(rs.getString("MaNganh"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSinhVien() {
        DefaultTableModel m = (DefaultTableModel) view.table.getModel();
        m.setRowCount(0);
        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery(
                    "SELECT MaSV,HoTen,GioiTinh,MaNganh,MaKhoa,MaLop FROM sinhvien"
            );
            while (rs.next()) {
                m.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ADD =================
    private void addSinhVien() {
        Connection c = null;
        try {
            c = DBUtil.getConnection();
            c.setAutoCommit(false);

            int khoaHoc = Integer.parseInt(view.txtKhoa.getText());
            String maKhoa = view.cbKhoa.getSelectedItem().toString();
            String maNganh = view.cbNganh.getSelectedItem().toString();

            // ===== VIẾT TẮT NGÀNH =====
            PreparedStatement p = c.prepareStatement(
                    "SELECT VietTat FROM nganh WHERE MaNganh=?"
            );
            p.setString(1, maNganh);
            ResultSet rs = p.executeQuery();
            rs.next();
            String vietTat = rs.getString("VietTat");

            // ===== MÃ SV =====
            Random rd = new Random();
            String maSV;
            while (true) {
                maSV = khoaHoc + "DC" + vietTat + (rd.nextInt(90000) + 10000);
                PreparedStatement ck = c.prepareStatement(
                        "SELECT 1 FROM account WHERE username=?"
                );
                ck.setString(1, maSV);
                if (!ck.executeQuery().next()) break;
            }

            String email = maSV + "@st.utt.edu.vn";

            // ===== TÌM LỚP =====
            String maLop = null;
            PreparedStatement find = c.prepareStatement(
                    "SELECT MaLop,SiSo FROM lop WHERE Khoa=? AND MaKhoa=? AND MaNganh=? ORDER BY MaLop ASC"
            );
            find.setInt(1, khoaHoc);
            find.setString(2, maKhoa);
            find.setString(3, maNganh);

            ResultSet rl = find.executeQuery();
            while (rl.next()) {
                if (rl.getInt("SiSo") < 60) {
                    maLop = rl.getString("MaLop");
                    break;
                }
            }

            // ===== TẠO LỚP =====
            if (maLop == null) {
                int stt = 21;
                PreparedStatement max = c.prepareStatement(
                        "SELECT MaLop FROM lop WHERE Khoa=? AND MaKhoa=? AND MaNganh=? ORDER BY MaLop DESC LIMIT 1"
                );
                max.setInt(1, khoaHoc);
                max.setString(2, maKhoa);
                max.setString(3, maNganh);

                ResultSet rm = max.executeQuery();
                if (rm.next()) {
                    stt = Integer.parseInt(
                            rm.getString("MaLop")
                                    .substring(rm.getString("MaLop").length() - 2)
                    ) + 1;
                }

                maLop = khoaHoc + "DC" + vietTat + stt;

                PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO lop(MaLop,TenLop,MaNganh,MaKhoa,Khoa,SiSo) VALUES (?,?,?,?,?,0)"
                );
                ins.setString(1, maLop);
                ins.setString(2, maLop);
                ins.setString(3, maNganh);
                ins.setString(4, maKhoa);
                ins.setInt(5, khoaHoc);
                ins.executeUpdate();
            }

            // ===== ACCOUNT =====
            PreparedStatement acc = c.prepareStatement(
                    "INSERT INTO account(username,password,role,email) VALUES (?,?,?,?)"
            );
            acc.setString(1, maSV);
            acc.setString(2, BCrypt.hashpw("utt@2005", BCrypt.gensalt()));
            acc.setString(3, "SINHVIEN");
            acc.setString(4, email);
            acc.executeUpdate();

            // ===== SINH VIÊN =====
            PreparedStatement sv = c.prepareStatement(
                    "INSERT INTO sinhvien(MaSV,Username,HoTen,NgaySinh,GioiTinh,QueQuan,MaLop,MaNganh,MaKhoa,Khoa,TrangThai) VALUES (?,?,?,?,?,?,?,?,?,?,?)"
            );
            sv.setString(1, maSV);
            sv.setString(2, maSV);
            sv.setString(3, view.txtHoTen.getText());
            sv.setDate(4, new java.sql.Date(((java.util.Date) view.spNgaySinh.getValue()).getTime()));
            sv.setString(5, view.cbGioiTinh.getSelectedItem().toString());
            sv.setString(6, view.txtQueQuan.getText());
            sv.setString(7, maLop);
            sv.setString(8, maNganh);
            sv.setString(9, maKhoa);
            sv.setInt(10, khoaHoc);
            sv.setString(11, "Đang học");
            sv.executeUpdate();

            PreparedStatement up = c.prepareStatement(
                    "UPDATE lop SET SiSo=SiSo+1 WHERE MaLop=?"
            );
            up.setString(1, maLop);
            up.executeUpdate();

            c.commit();
            loadSinhVien();
            JOptionPane.showMessageDialog(view, "Thêm sinh viên thành công!");

        } catch (Exception e) {
            try { if (c != null) c.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi thêm sinh viên");
        }
    }

    // ================= UPDATE =================
    private void updateSinhVien() {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE sinhvien SET HoTen=? WHERE MaSV=?"
            );
            ps.setString(1, view.txtHoTen.getText());
            ps.setString(2, view.txtMaSV.getText());
            ps.executeUpdate();
            loadSinhVien();
            JOptionPane.showMessageDialog(view, "Sửa thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    private void deleteSinhVien() {
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);

            PreparedStatement get = c.prepareStatement(
                    "SELECT MaLop FROM sinhvien WHERE MaSV=?"
            );
            get.setString(1, view.txtMaSV.getText());
            ResultSet rs = get.executeQuery();
            if (!rs.next()) return;

            String maLop = rs.getString(1);

            PreparedStatement del = c.prepareStatement(
                    "DELETE FROM sinhvien WHERE MaSV=?"
            );
            del.setString(1, view.txtMaSV.getText());
            del.executeUpdate();

            PreparedStatement up = c.prepareStatement(
                    "UPDATE lop SET SiSo=SiSo-1 WHERE MaLop=?"
            );
            up.setString(1, maLop);
            up.executeUpdate();

            c.commit();
            loadSinhVien();
            JOptionPane.showMessageDialog(view, "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
