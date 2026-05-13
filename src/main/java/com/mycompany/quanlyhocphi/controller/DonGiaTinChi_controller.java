package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.DonGiaTinChi_panel;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DonGiaTinChi_controller {

    private Connection conn;
    private final DonGiaTinChi_panel panel;

    public DonGiaTinChi_controller(DonGiaTinChi_panel panel) {
        this.panel = panel;
        connect();
        loadComboMaMon();
        loadInfoByMaMon();  // đổ TenMon + SoTinChi + DonGia
        loadTableAll();     // show full 4 cột
        event();
    }

    private void connect() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quanlyhocphi?useSSL=false&serverTimezone=UTC",
                    "root", ""
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi CSDL: " + e.getMessage());
        }
    }

    private double parseMoney(String s) {
        if (s == null) return 0;
        String t = s.trim().replace(".", "").replace(",", "");
        if (t.isEmpty()) return 0;
        return Double.parseDouble(t);
    }

    // Load combo từ monhoc
    private void loadComboMaMon() {
        panel.cbMaMon.removeAllItems();
        String sql = "SELECT MaMon FROM monhoc ORDER BY MaMon";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                panel.cbMaMon.addItem(rs.getString("MaMon"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Khi chọn mã môn -> lấy TenMon, SoTinChi và DonGia (nếu đã có)
    private void loadInfoByMaMon() {
        Object sel = panel.cbMaMon.getSelectedItem();
        if (sel == null) {
            panel.txtTenMon.setText("");
            panel.txtSoTC.setText("");
            panel.txtDonGia.setText("");
            return;
        }

        String maMon = sel.toString();
        String sql = """
            SELECT m.TenMon, m.SoTinChi, dg.DonGia
            FROM monhoc m
            LEFT JOIN dongia_tinchi dg ON dg.MaMon = m.MaMon
            WHERE m.MaMon = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                panel.txtTenMon.setText(rs.getString("TenMon"));
                panel.txtSoTC.setText(String.valueOf(rs.getInt("SoTinChi")));

                Object dg = rs.getObject("DonGia");
                panel.txtDonGia.setText(dg == null ? "" : String.valueOf(rs.getDouble("DonGia")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            panel.txtTenMon.setText("");
            panel.txtSoTC.setText("");
            panel.txtDonGia.setText("");
        }
    }

    // Load bảng: hiển thị đủ 4 cột cho TẤT CẢ môn
    private void loadTableAll() {
        panel.model.setRowCount(0);

        String sql = """
            SELECT m.MaMon, m.TenMon, m.SoTinChi, dg.DonGia
            FROM monhoc m
            LEFT JOIN dongia_tinchi dg ON dg.MaMon = m.MaMon
            ORDER BY m.MaMon
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object donGia = rs.getObject("DonGia"); // có thể null
                panel.model.addRow(new Object[]{
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getInt("SoTinChi"),
                        donGia == null ? "" : rs.getDouble("DonGia")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lọc theo mã hoặc tên
    private void filterTable() {
        panel.model.setRowCount(0);
        String key = panel.txtTim.getText().trim();

        String sql = """
            SELECT m.MaMon, m.TenMon, m.SoTinChi, dg.DonGia
            FROM monhoc m
            LEFT JOIN dongia_tinchi dg ON dg.MaMon = m.MaMon
            WHERE m.MaMon LIKE ? OR m.TenMon LIKE ?
            ORDER BY m.MaMon
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + key + "%");
            ps.setString(2, "%" + key + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object donGia = rs.getObject("DonGia");
                panel.model.addRow(new Object[]{
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getInt("SoTinChi"),
                        donGia == null ? "" : rs.getDouble("DonGia")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lưu đơn giá: upsert vào dongia_tinchi(MaMon, DonGia)
    private void saveDonGia() {
        try {
            Object sel = panel.cbMaMon.getSelectedItem();
            if (sel == null) {
                JOptionPane.showMessageDialog(panel, "Chưa có mã môn!");
                return;
            }

            String maMon = sel.toString();
            double donGia = parseMoney(panel.txtDonGia.getText());

            if (donGia <= 0) {
                JOptionPane.showMessageDialog(panel, "Đơn giá phải > 0");
                return;
            }

            String sql = """
                INSERT INTO dongia_tinchi(MaMon, DonGia)
                VALUES(?, ?)
                ON DUPLICATE KEY UPDATE DonGia = VALUES(DonGia)
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maMon);
                ps.setDouble(2, donGia);
                ps.executeUpdate();
            }

            loadInfoByMaMon();
            loadTableAll();
            JOptionPane.showMessageDialog(panel, "Lưu đơn giá thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Lưu thất bại: " + ex.getMessage());
        }
    }

    // Xóa đơn giá: delete record trong dongia_tinchi
    private void deleteDonGia() {
        try {
            Object sel = panel.cbMaMon.getSelectedItem();
            if (sel == null) return;

            String maMon = sel.toString();

            int c = JOptionPane.showConfirmDialog(panel,
                    "Xóa đơn giá của môn " + maMon + " ?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM dongia_tinchi WHERE MaMon=?")) {
                ps.setString(1, maMon);
                int row = ps.executeUpdate();
                JOptionPane.showMessageDialog(panel,
                        row > 0 ? "Xóa đơn giá thành công!" : "Môn này chưa có đơn giá!");
            }

            panel.txtDonGia.setText("");
            loadTableAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Xóa thất bại: " + ex.getMessage());
        }
    }

    // Xuất Excel & mở file luôn
    private void exportExcel() {
        try {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("DonGiaTinChi");

            DefaultTableModel model = panel.model;

            // header
            Row header = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }

            // data
            for (int r = 0; r < model.getRowCount(); r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < model.getColumnCount(); c++) {
                    Object v = model.getValueAt(r, c);
                    row.createCell(c).setCellValue(v == null ? "" : v.toString());
                }
            }

            for (int i = 0; i < model.getColumnCount(); i++) sheet.autoSizeColumn(i);

            String path = System.getProperty("user.home") + "/Desktop/DonGiaTinChi.xlsx";
            File file = new File(path);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
            wb.close();

            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(panel, "Xuất Excel thành công và đã mở file!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Lỗi xuất Excel: " + e.getMessage());
        }
    }

    private void event() {
        panel.cbMaMon.addActionListener(e -> loadInfoByMaMon());

        panel.btnLuu.addActionListener(e -> saveDonGia());
        panel.btnXoa.addActionListener(e -> deleteDonGia());

        panel.btnTim.addActionListener(e -> filterTable());
        panel.btnXuat.addActionListener(e -> exportExcel());

        // click bảng -> set combo + đổ info
        panel.table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = panel.table.getSelectedRow();
            if (r >= 0) {
                String maMon = panel.model.getValueAt(r, 0).toString();
                panel.cbMaMon.setSelectedItem(maMon);
                Object dg = panel.model.getValueAt(r, 3);
                panel.txtDonGia.setText(dg == null ? "" : dg.toString());
            }
        });
    }
}

