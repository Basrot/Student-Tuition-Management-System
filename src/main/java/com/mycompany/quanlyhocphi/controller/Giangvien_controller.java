package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.Giangvien_panel;
import java.awt.Desktop;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Giangvien_controller {

    private Giangvien_panel view;
    private Connection conn;

    public Giangvien_controller(Giangvien_panel view) {
        this.view = view;

        if (view != null) {
            connectDB();
            loadKhoa();
            loadTable();
            addEvents();
        }
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

    private void loadKhoa() {
        view.cbKHOA.removeAllItems();

        String sql = "SELECT MaKhoa FROM khoa";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                view.cbKHOA.addItem(rs.getString("MaKhoa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNganhTheoKhoa() {
        view.cbNganh.removeAllItems();
        view.cbMon.removeAllItems();

        if (view.cbKHOA.getSelectedItem() == null) {
            return;
        }

        String sql = "SELECT MaNganh FROM nganh WHERE MaKhoa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, view.cbKHOA.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                view.cbNganh.addItem(rs.getString("MaNganh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMonTheoNganh() {
        view.cbMon.removeAllItems();

        if (view.cbKHOA.getSelectedItem() == null || view.cbNganh.getSelectedItem() == null) {
            return;
        }

        String sql = """
            SELECT MaMon
            FROM monhoc
            WHERE MaKhoa = ? AND MaNganh = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, view.cbKHOA.getSelectedItem().toString());
            ps.setString(2, view.cbNganh.getSelectedItem().toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                view.cbMon.addItem(rs.getString("MaMon"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTable() {
        DefaultTableModel model = view.model;
        model.setRowCount(0);

        String sql = """
            SELECT MaGV, TenGV, MaKhoa, MaNganh, MaMon
            FROM giangvien
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaGV"),
                    rs.getString("TenGV"),
                    rs.getString("MaKhoa"),
                    rs.getString("MaNganh"),
                    rs.getString("MaMon")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String validateGiangVien(String maGV, String tenGV, Object khoa, Object nganh, Object mon) {
        String maGVStr = (maGV == null) ? "" : maGV.trim();
        String tenGVStr = (tenGV == null) ? "" : tenGV.trim();
        String khoaStr = (khoa == null) ? "" : khoa.toString().trim();
        String nganhStr = (nganh == null) ? "" : nganh.toString().trim();
        String monStr = (mon == null) ? "" : mon.toString().trim();

        if (maGVStr.isEmpty() || tenGVStr.isEmpty()
                || khoaStr.isEmpty() || nganhStr.isEmpty() || monStr.isEmpty()) {
            return "Các ô không được để trống!";
        }

        return "Hợp lệ";
    }

    public String validateGiangVienForTest(String maGV, String tenGV, Object khoa, Object nganh, Object mon, boolean isDuplicate) {
        String result = validateGiangVien(maGV, tenGV, khoa, nganh, mon);

        if (!result.equals("Hợp lệ")) {
            return result;
        }

        if (isDuplicate) {
            return "Mã giảng viên trùng";
        }

        return "Hợp lệ";
    }

    private boolean isTrungMaGiangVien(String maGV) {
        String sql = "SELECT COUNT(*) FROM giangvien WHERE MaGV = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGV.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void themGiangVien() {
        String check = validateGiangVien(
            view.txtMGV.getText(),
            view.txtTGV.getText(),
            view.cbKHOA.getSelectedItem(),
            view.cbNganh.getSelectedItem(),
            view.cbMon.getSelectedItem()
        );

        if (!check.equals("Hợp lệ")) {
            JOptionPane.showMessageDialog(view, check);
            return;
        }

        String maGV = view.txtMGV.getText().trim();

        if (isTrungMaGiangVien(maGV)) {
            JOptionPane.showMessageDialog(view, "Mã giảng viên trùng");
            return;
        }

        String sql = "INSERT INTO giangvien VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGV);
            ps.setString(2, view.txtTGV.getText().trim());
            ps.setString(3, view.cbKHOA.getSelectedItem().toString());
            ps.setString(4, view.cbNganh.getSelectedItem().toString());
            ps.setString(5, view.cbMon.getSelectedItem().toString());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Thêm giảng viên thành công");
            loadTable();
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi thêm giảng viên");
        }
    }

    private void suaGiangVien() {
        int row = view.table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Chọn giảng viên cần sửa");
            return;
        }

        String check = validateGiangVien(
            view.txtMGV.getText(),
            view.txtTGV.getText(),
            view.cbKHOA.getSelectedItem(),
            view.cbNganh.getSelectedItem(),
            view.cbMon.getSelectedItem()
        );

        if (!check.equals("Hợp lệ")) {
            JOptionPane.showMessageDialog(view, check);
            return;
        }

        String sql = """
            UPDATE giangvien
            SET TenGV = ?, MaKhoa = ?, MaNganh = ?, MaMon = ?
            WHERE MaGV = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, view.txtTGV.getText().trim());
            ps.setString(2, view.cbKHOA.getSelectedItem().toString());
            ps.setString(3, view.cbNganh.getSelectedItem().toString());
            ps.setString(4, view.cbMon.getSelectedItem().toString());
            ps.setString(5, view.txtMGV.getText().trim());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Sửa thành công");
            loadTable();
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi sửa giảng viên");
        }
    }

    private void xoaGiangVien() {
        int row = view.table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Chọn giảng viên cần xóa");
            return;
        }

        int c = JOptionPane.showConfirmDialog(
            view,
            "Bạn có chắc muốn xóa?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );
        if (c != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM giangvien WHERE MaGV = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, view.txtMGV.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Xóa thành công");
            loadTable();
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Không thể xóa do ràng buộc dữ liệu!");
        }
    }

    private void resetForm() {
        view.txtMGV.setText("");
        view.txtTGV.setText("");
        view.table.clearSelection();
    }

    private void addEvents() {
        view.btnThem.addActionListener(e -> themGiangVien());
        view.btnSua.addActionListener(e -> suaGiangVien());
        view.btnXoa.addActionListener(e -> xoaGiangVien());
        view.btnXuat.addActionListener(e -> xuatExcelGiangVien());
        view.cbKHOA.addActionListener(e -> loadNganhTheoKhoa());
        view.cbNganh.addActionListener(e -> loadMonTheoNganh());

        view.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.table.getSelectedRow();
                if (row == -1) {
                    return;
                }

                view.txtMGV.setText(view.model.getValueAt(row, 0).toString());
                view.txtTGV.setText(view.model.getValueAt(row, 1).toString());

                view.cbKHOA.setSelectedItem(view.model.getValueAt(row, 2));
                loadNganhTheoKhoa();

                view.cbNganh.setSelectedItem(view.model.getValueAt(row, 3));
                loadMonTheoNganh();

                view.cbMon.setSelectedItem(view.model.getValueAt(row, 4));
            }
        });
    }

    private void xuatExcelGiangVien() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách giảng viên");

            DefaultTableModel model = (DefaultTableModel) view.table.getModel();

            Row header = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    row.createCell(j).setCellValue(value == null ? "" : value.toString());
                }
            }

            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            String path = System.getProperty("user.home") + "/Desktop/DanhSachGiangVien.xlsx";
            File file = new File(path);

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

            Desktop.getDesktop().open(file);

            JOptionPane.showMessageDialog(view, "Xuất Excel giảng viên thành công và đã mở file!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi xuất Excel giảng viên!");
        }
    }
}