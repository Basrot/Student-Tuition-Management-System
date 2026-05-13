package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.NganhPanel;
import com.mycompany.quanlyhocphi.util.DBUtil;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class NganhController {

    private NganhPanel view;

    public NganhController(NganhPanel v) {
        this.view = v;

        if (view != null) {
            loadKhoa();
            loadNganh();

            view.btnTim.addActionListener(e -> timKiem());
            view.btnThem.addActionListener(e -> them());
            view.btnSua.addActionListener(e -> sua());
            view.btnXoa.addActionListener(e -> xoa());
            view.btnXuat.addActionListener(e -> xuatExcel());
            view.table.getSelectionModel()
                    .addListSelectionListener(e -> clickTable());
        }
    }

    public String validateNganh(String ma, String ten, String vietTat, Object khoa) {
        String maStr = (ma == null) ? "" : ma.trim();
        String tenStr = (ten == null) ? "" : ten.trim();
        String vietTatStr = (vietTat == null) ? "" : vietTat.trim().toUpperCase();
        String khoaStr = (khoa == null) ? "" : khoa.toString().trim();

        // N: để trống
        if (maStr.isEmpty() || tenStr.isEmpty() || vietTatStr.isEmpty() || khoaStr.isEmpty()) {
            return "Các ô không được để trống!";
        }

        // S: sai định dạng
        if (!vietTatStr.matches("[A-Z]{2}")) {
            return "Viết tắt phải đúng 2 chữ cái (A-Z)!";
        }

        // T: hợp lệ
        return "Hợp lệ";
    }

    private void loadKhoa() {
        view.cbKhoa.removeAllItems();
        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement()
                    .executeQuery("SELECT MaKhoa FROM khoa");
            while (rs.next()) {
                view.cbKhoa.addItem(rs.getString("MaKhoa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNganh() {
        DefaultTableModel m =
                (DefaultTableModel) view.table.getModel();
        m.setRowCount(0);

        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement()
                    .executeQuery(
                        "SELECT MaNganh, TenNganh, VietTat, MaKhoa FROM nganh"
                    );

            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getString("MaNganh"),
                    rs.getString("TenNganh"),
                    rs.getString("VietTat"),
                    rs.getString("MaKhoa")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void them() {

        String check = validateNganh(
            view.txtMa.getText(),
            view.txtTen.getText(),
            view.txtVietTat.getText(),
            view.cbKhoa.getSelectedItem()
        );

        if (!check.equals("Hợp lệ")) {
            JOptionPane.showMessageDialog(view, check);
            return;
        }

        String ma = view.txtMa.getText().trim();
        String ten = view.txtTen.getText().trim();
        String vietTat = view.txtVietTat.getText().trim().toUpperCase();

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement p = c.prepareStatement(
                "INSERT INTO nganh VALUES (?,?,?,?)"
            );

            p.setString(1, ma);
            p.setString(2, ten);
            p.setString(3, vietTat);
            p.setString(4, view.cbKhoa.getSelectedItem().toString());

            p.executeUpdate();
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadNganh();
            clear();

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(view,
                    "Mã ngành đã tồn tại!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi thêm dữ liệu!");
        }
    }

    private void sua() {

        String check = validateNganh(
            view.txtMa.getText(),
            view.txtTen.getText(),
            view.txtVietTat.getText(),
            view.cbKhoa.getSelectedItem()
        );

        if (!check.equals("Hợp lệ")) {
            JOptionPane.showMessageDialog(view, check);
            return;
        }

        String ten = view.txtTen.getText().trim();
        String vietTat = view.txtVietTat.getText().trim().toUpperCase();

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement p = c.prepareStatement(
                "UPDATE nganh SET TenNganh=?, VietTat=?, MaKhoa=? WHERE MaNganh=?"
            );

            p.setString(1, ten);
            p.setString(2, vietTat);
            p.setString(3, view.cbKhoa.getSelectedItem().toString());
            p.setString(4, view.txtMa.getText().trim());

            p.executeUpdate();
            JOptionPane.showMessageDialog(view, "Sửa thành công!");
            loadNganh();
            clear();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi sửa dữ liệu!");
        }
    }

    private void xoa() {
        int r = view.table.getSelectedRow();
        if (r == -1) return;

        if (JOptionPane.showConfirmDialog(
                view,
                "Xóa ngành này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) return;

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement p = c.prepareStatement(
                "DELETE FROM nganh WHERE MaNganh=?"
            );
            p.setString(1, view.txtMa.getText());
            p.executeUpdate();

            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadNganh();
            clear();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể xóa (có ràng buộc)!");
        }
    }

    private void timKiem() {
        String key = view.txtTim.getText().trim();

        DefaultTableModel m =
                (DefaultTableModel) view.table.getModel();
        m.setRowCount(0);

        if (key.isEmpty()) {
            loadNganh();
            return;
        }

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement p = c.prepareStatement(
                "SELECT * FROM nganh WHERE MaNganh LIKE ?"
            );
            p.setString(1, "%" + key + "%");

            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getString("MaNganh"),
                    rs.getString("TenNganh"),
                    rs.getString("VietTat"),
                    rs.getString("MaKhoa")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi tìm kiếm!");
        }
    }

    private void clickTable() {
        int r = view.table.getSelectedRow();
        if (r == -1) return;

        view.txtMa.setText(view.table.getValueAt(r, 0).toString());
        view.txtTen.setText(view.table.getValueAt(r, 1).toString());
        view.txtVietTat.setText(view.table.getValueAt(r, 2).toString());
        view.cbKhoa.setSelectedItem(view.table.getValueAt(r, 3));

        view.txtMa.setEditable(false);
    }

    private void clear() {
        view.txtMa.setText("");
        view.txtTen.setText("");
        view.txtVietTat.setText("");
        view.txtMa.setEditable(true);
    }

    private void xuatExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách ngành");

            DefaultTableModel model =
                    (DefaultTableModel) view.table.getModel();

            Row header = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                header.createCell(i)
                        .setCellValue(model.getColumnName(i));
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object v = model.getValueAt(i, j);
                    row.createCell(j)
                       .setCellValue(v == null ? "" : v.toString());
                }
            }

            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            String path = System.getProperty("user.home")
                    + "/Desktop/DanhSachNganh.xlsx";
            File file = new File(path);

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

            Desktop.getDesktop().open(file);

            JOptionPane.showMessageDialog(view,
                    "Xuất Excel thành công!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi xuất Excel!");
        }
    }
}