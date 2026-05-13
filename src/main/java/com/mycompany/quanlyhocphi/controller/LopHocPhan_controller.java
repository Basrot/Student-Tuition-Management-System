package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.LopHocPhan_panel;
import com.mycompany.quanlyhocphi.util.DBUtil;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LopHocPhan_controller {

    private LopHocPhan_panel view;
    private boolean loading = false;

    public LopHocPhan_controller(LopHocPhan_panel v) {
        this.view = v;
        init();
        event();
        loadTable();
    }

    // ================= INIT =================
    private void init() {
        loading = true;

        loadCombo(view.cbKhoa, "SELECT MaKhoa FROM khoa");
        loadCombo(view.cbHocKy, "SELECT MaHocKi FROM hocki");
        loadCombo(view.cbGV, "SELECT MaGV FROM giangvien");

        loadKhoaHoc(); // LẤY ĐÚNG KHÓA TỪ DB

        loading = false;
    }

    // ================= EVENT =================
    private void event() {

        view.cbKhoa.addActionListener(e -> {
            if (!loading) loadNganh();
        });

        view.cbNganh.addActionListener(e -> {
            if (!loading) loadLopMon();
        });

        view.cbKhoaHoc.addActionListener(e -> {
            if (!loading) loadLopMon();
        });

        view.cbMon.addActionListener(e -> {
            if (!loading) loadSoTinChi();
        });

        view.btnTao.addActionListener(e -> taoLHP());
        view.btnXoa.addActionListener(e -> xoaLHP());
        view.btnTim.addActionListener(e -> timKiemLHP());
        view.btnXuat.addActionListener(e -> xuatExcelTheoLHP());

        view.table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSinhVien();
        });
    }

    // ================= LOAD NGÀNH =================
    private void loadNganh() {
        if (view.cbKhoa.getSelectedItem() == null) return;

        loadCombo(
            view.cbNganh,
            "SELECT MaNganh FROM nganh WHERE MaKhoa='" +
                    view.cbKhoa.getSelectedItem() + "'"
        );
    }

    // ================= LOAD LỚP + MÔN =================
    private void loadLopMon() {

        view.cbLop.removeAllItems();
        view.cbMon.removeAllItems();

        if (view.cbNganh.getSelectedItem() == null ||
            view.cbKhoaHoc.getSelectedItem() == null) return;

        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement psLop = c.prepareStatement(
                "SELECT MaLop FROM lop WHERE Khoa=? AND MaNganh=?"
            );
            psLop.setInt(1, Integer.parseInt(
                    view.cbKhoaHoc.getSelectedItem().toString()));
            psLop.setString(2,
                    view.cbNganh.getSelectedItem().toString());

            ResultSet r1 = psLop.executeQuery();
            while (r1.next()) view.cbLop.addItem(r1.getString(1));

            PreparedStatement psMon = c.prepareStatement(
                "SELECT MaMon FROM monhoc WHERE MaNganh=?"
            );
            psMon.setString(1,
                    view.cbNganh.getSelectedItem().toString());

            ResultSet r2 = psMon.executeQuery();
            while (r2.next()) view.cbMon.addItem(r2.getString(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            private void loadKhoaHoc() {
            view.cbKhoaHoc.removeAllItems();
            try (Connection c = DBUtil.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT DISTINCT Khoa FROM lop ORDER BY Khoa DESC"
                );
                while (rs.next()) {
                    view.cbKhoaHoc.addItem(rs.getString("Khoa"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    // ================= LOAD SỐ TC =================
    private void loadSoTinChi() {
        if (view.cbMon.getSelectedItem() == null) {
            view.txtSTC.setText("");
            return;
        }

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                "SELECT SoTinChi FROM monhoc WHERE MaMon=?"
            );
            ps.setString(1, view.cbMon.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) view.txtSTC.setText(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= TẠO LHP =================
    private void taoLHP() {

    if (view.cbLop.getSelectedItem() == null ||
        view.cbMon.getSelectedItem() == null ||
        view.cbHocKy.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(view, "Thiếu thông tin");
        return;
    }

    String maLop = view.cbLop.getSelectedItem().toString();
    String maMon = view.cbMon.getSelectedItem().toString();

    try (Connection c = DBUtil.getConnection()) {
        c.setAutoCommit(false);

        // ===== CHECK: LỚP ĐÃ HỌC MÔN NÀY CHƯA =====
        PreparedStatement checkMon = c.prepareStatement("""
            SELECT 1
            FROM lop_dkhocphan ldk
            JOIN lophocphan lhp ON ldk.MaLHP = lhp.MaLHP
            WHERE ldk.MaLop=? AND lhp.MaMon=?
        """);
        checkMon.setString(1, maLop);
        checkMon.setString(2, maMon);

        if (checkMon.executeQuery().next()) {
            JOptionPane.showMessageDialog(view,
                    "Lớp này đã đăng ký môn học phần này rồi");
            return;
        }

        // ===== SINH MÃ LHP MỚI =====
        ResultSet rs = c.createStatement().executeQuery("""
            SELECT CONCAT('LHP', LPAD(
                IFNULL(MAX(CAST(SUBSTRING(MaLHP,4) AS UNSIGNED)),0) + 1,
                3,'0'))
            FROM lophocphan
        """);
        rs.next();
        String maLHP = rs.getString(1);
        view.txtMaLHP.setText(maLHP);

        // ===== INSERT LHP =====
        PreparedStatement insertLHP = c.prepareStatement("""
            INSERT INTO lophocphan
            (MaLHP, MaMon, TenHP, MaHocKy, MaGV, SoTinChi,
             SiSoToiDa, SiSoHienTai, TrangThai)
            VALUES (?,?,?,?,?,?,60,0,'Mở')
        """);
        insertLHP.setString(1, maLHP);
        insertLHP.setString(2, maMon);
        insertLHP.setString(3, view.txtTenHP.getText());
        insertLHP.setString(4, view.cbHocKy.getSelectedItem().toString());
        insertLHP.setString(5, view.cbGV.getSelectedItem().toString());
        insertLHP.setInt(6, Integer.parseInt(view.txtSTC.getText()));
        insertLHP.executeUpdate();

        // ===== MAP LỚP – LHP (CHECK TRƯỚC KHI INSERT) =====
        PreparedStatement checkMap = c.prepareStatement("""
            SELECT 1 FROM lop_dkhocphan
            WHERE MaLop=? AND MaLHP=?
        """);
        checkMap.setString(1, maLop);
        checkMap.setString(2, maLHP);

        if (!checkMap.executeQuery().next()) {
            PreparedStatement map = c.prepareStatement("""
                INSERT INTO lop_dkhocphan(MaLop, MaLHP)
                VALUES (?,?)
            """);
            map.setString(1, maLop);
            map.setString(2, maLHP);
            map.executeUpdate();
        }

        // ===== ĐĂNG KÝ TOÀN BỘ SINH VIÊN =====
        PreparedStatement dk = c.prepareStatement("""
            INSERT INTO dangkyhocphan (MaSV, MaLHP, NgayDangKy, TrangThai)
            SELECT sv.MaSV, ?, NOW(), 'Đã đăng ký'
            FROM sinhvien sv
            WHERE sv.MaLop=?
        """);
        dk.setString(1, maLHP);
        dk.setString(2, maLop);
        int siSo = dk.executeUpdate();

        // ===== UPDATE SĨ SỐ =====
        PreparedStatement up = c.prepareStatement("""
            UPDATE lophocphan SET SiSoHienTai=? WHERE MaLHP=?
        """);
        up.setInt(1, siSo);
        up.setString(2, maLHP);
        up.executeUpdate();

        c.commit();
        loadTable();

        JOptionPane.showMessageDialog(view,
                "Tạo lớp học phần thành công (" + siSo + "/60)");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(view,
                "Lỗi tạo lớp học phần");
    }
}

    // ================= LOAD TABLE =================
    private void loadTable() {
        DefaultTableModel m = view.model;
        m.setRowCount(0);

        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery("""
                SELECT lhp.MaLHP, lhp.TenHP, ldk.MaLop, lhp.MaGV,
                       COUNT(DISTINCT dk.MaSV) AS SiSo
                FROM lophocphan lhp
                JOIN lop_dkhocphan ldk ON lhp.MaLHP = ldk.MaLHP
                LEFT JOIN dangkyhocphan dk ON dk.MaLHP = lhp.MaLHP
                GROUP BY lhp.MaLHP, ldk.MaLop, lhp.MaGV
            """);

            while (rs.next()) {
                m.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD SV =================
    private void loadSinhVien() {
        int r = view.table.getSelectedRow();
        if (r == -1) return;

        String maLHP = view.table.getValueAt(r, 0).toString();
        DefaultTableModel m = view.modelSV;
        m.setRowCount(0);

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement("""
                SELECT sv.MaSV, sv.HoTen, sv.MaLop,
                       dk.NgayDangKy, dk.TrangThai
                FROM dangkyhocphan dk
                JOIN sinhvien sv ON dk.MaSV = sv.MaSV
                WHERE dk.MaLHP=?
            """);
            ps.setString(1, maLHP);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                m.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getString(5)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= XÓA =================
    private void xoaLHP() {
        int r = view.table.getSelectedRow();
        if (r == -1) return;

        String maLHP = view.table.getValueAt(r, 0).toString();

        try (Connection c = DBUtil.getConnection()) {
            c.createStatement().execute(
                "DELETE FROM dangkyhocphan WHERE MaLHP='" + maLHP + "'");
            c.createStatement().execute(
                "DELETE FROM lop_dkhocphan WHERE MaLHP='" + maLHP + "'");
            c.createStatement().execute(
                "DELETE FROM lophocphan WHERE MaLHP='" + maLHP + "'");

            loadTable();
            view.modelSV.setRowCount(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "xoá thành công");
            e.printStackTrace();
            
        }
    }

    // ================= UTIL =================
    private void loadCombo(JComboBox<String> cb, String sql) {
        cb.removeAllItems();
        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery(sql);
            while (rs.next()) cb.addItem(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void timKiemLHP() {
    String key = view.txtTim.getText().trim();
    if (key.isEmpty()) {
        loadTable();   // quay lại toàn bộ
        return;
    }

    view.model.setRowCount(0);

    try (Connection c = DBUtil.getConnection()) {
        PreparedStatement ps = c.prepareStatement("""
            SELECT lhp.MaLHP, lhp.TenHP, ldk.MaLop, lhp.MaGV,
                   COUNT(dk.MaDK) AS SiSo
            FROM lophocphan lhp
            JOIN lop_dkhocphan ldk ON lhp.MaLHP = ldk.MaLHP
            LEFT JOIN dangkyhocphan dk ON dk.MaLHP = lhp.MaLHP
            WHERE ldk.MaLop LIKE ? OR lhp.TenHP LIKE ?
            GROUP BY lhp.MaLHP, ldk.MaLop
        """);

        ps.setString(1, "%" + key + "%");
        ps.setString(2, "%" + key + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            view.model.addRow(new Object[]{
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getInt(5)
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private void xuatExcelTheoLHP() {
    int r = view.table.getSelectedRow();
    if (r == -1) {
        JOptionPane.showMessageDialog(view, "Chọn 1 lớp học phần để xuất");
        return;
    }

    try {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Lop hoc phan");

        int rowIndex = 0;

        // ===== TIÊU ĐỀ LHP =====
        Row title1 = sheet.createRow(rowIndex++);
        title1.createCell(0).setCellValue("THÔNG TIN LỚP HỌC PHẦN");

        rowIndex++;

        // ===== HEADER LHP =====
        Row headerLHP = sheet.createRow(rowIndex++);
        for (int i = 0; i < view.model.getColumnCount(); i++) {
            headerLHP.createCell(i)
                     .setCellValue(view.model.getColumnName(i));
        }

        // ===== DATA LHP =====
        Row dataLHP = sheet.createRow(rowIndex++);
        for (int i = 0; i < view.model.getColumnCount(); i++) {
            dataLHP.createCell(i)
                   .setCellValue(view.model.getValueAt(r, i).toString());
        }

        rowIndex += 2;

        // ===== TIÊU ĐỀ SV =====
        Row title2 = sheet.createRow(rowIndex++);
        title2.createCell(0).setCellValue("DANH SÁCH SINH VIÊN");

        rowIndex++;

        // ===== HEADER SV =====
        Row headerSV = sheet.createRow(rowIndex++);
        for (int i = 0; i < view.modelSV.getColumnCount(); i++) {
            headerSV.createCell(i)
                    .setCellValue(view.modelSV.getColumnName(i));
        }

        // ===== DATA SV =====
        for (int i = 0; i < view.modelSV.getRowCount(); i++) {
            Row row = sheet.createRow(rowIndex++);
            for (int j = 0; j < view.modelSV.getColumnCount(); j++) {
                Object val = view.modelSV.getValueAt(i, j);
                row.createCell(j).setCellValue(val == null ? "" : val.toString());
            }
        }

        // ===== AUTO SIZE =====
        for (int i = 0; i < 10; i++) sheet.autoSizeColumn(i);

        // ===== LƯU FILE =====
        String path = System.getProperty("user.home")
                + "/Desktop/LopHocPhan.xlsx";
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        wb.write(fos);
        fos.close();
        wb.close();

        Desktop.getDesktop().open(file);
        JOptionPane.showMessageDialog(view, "Xuất Excel thành công!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(view, "Lỗi xuất Excel");
    }
}


}
