/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.controller;
import com.mycompany.quanlyhocphi.panel.KhoaPanel;
import com.mycompany.quanlyhocphi.util.DBUtil;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author basrot
 */
public class KhoaController {

   

    private KhoaPanel view;

    public KhoaController(KhoaPanel view) {
        this.view = view;
        loadKhoa();
        view.btnThem.addActionListener(e -> addKhoa());
        view.btnSua.addActionListener(e -> suaKhoa());
        view.btnXoa.addActionListener(e -> xoaKhoa());
        view.btnXuat.addActionListener(e -> xuatExcelKhoa());
        view.btnTim.addActionListener(e -> timKiemKhoa());
        view.table.getSelectionModel()
                .addListSelectionListener(e -> clickTable());

    }

    private void loadKhoa() {
        DefaultTableModel m = (DefaultTableModel) view.table.getModel();
        m.setRowCount(0);

        try (Connection c = DBUtil.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery(
                    "SELECT MaKhoa, TenKhoa FROM khoa"
            );
            while (rs.next()) {
                m.addRow(new Object[]{
                        rs.getString("MaKhoa"),
                        rs.getString("TenKhoa")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addKhoa() {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement p = c.prepareStatement(
                    "INSERT INTO khoa(MaKhoa, TenKhoa) VALUES(?,?)"
            );
            p.setString(1, view.txtMaKhoa.getText());
            p.setString(2, view.txtTenKhoa.getText());
            p.executeUpdate();

            JOptionPane.showMessageDialog(view, "Thêm khoa thành công!");
            loadKhoa();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi thêm khoa");
            e.printStackTrace();
        }
}
     private void suaKhoa() {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement p = c.prepareStatement(
                    "UPDATE khoa SET TenKhoa=? WHERE MaKhoa=?"
            );
            p.setString(1, view.txtTenKhoa.getText());
            p.setString(2, view.txtMaKhoa.getText());
            p.executeUpdate();

            JOptionPane.showMessageDialog(view, "Sửa thành công");
            loadKhoa();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi sửa khoa");
        }
    }

    /* ===== XOÁ ===== */
    private void xoaKhoa() {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement p = c.prepareStatement(
                    "DELETE FROM khoa WHERE MaKhoa=?"
            );
            p.setString(1, view.txtMaKhoa.getText());
            p.executeUpdate();

            JOptionPane.showMessageDialog(view, "Xóa thành công");
            loadKhoa();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Không thể xóa khoa này");
        }
    }

    /* ===== CLICK TABLE ===== */
    private void clickTable() {
        int r = view.table.getSelectedRow();
        if (r == -1) return;

        view.txtMaKhoa.setText(view.table.getValueAt(r, 0).toString());
        view.txtTenKhoa.setText(view.table.getValueAt(r, 1).toString());
    }
    private void xuatExcelKhoa() {
    try {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách khoa");

        DefaultTableModel model =
                (DefaultTableModel) view.table.getModel();

        // ===== HEADER =====
        Row header = sheet.createRow(0);
        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(model.getColumnName(i));
        }

        // ===== DATA =====
        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object value = model.getValueAt(i, j);
                row.createCell(j)
                   .setCellValue(value == null ? "" : value.toString());
            }
        }

        // ===== AUTO SIZE CỘT =====
        for (int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }

        // ===== LƯU + MỞ FILE (DESKTOP) =====
        String path = System.getProperty("user.home")
                + "/Desktop/DanhSachKhoa.xlsx";
        File file = new File(path);

        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        workbook.close();

        Desktop.getDesktop().open(file);

        JOptionPane.showMessageDialog(view,
                "Xuất Excel Khoa thành công và đã mở file!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(view,
                "Lỗi xuất Excel Khoa!");
    }
}
    private void timKiemKhoa() {
    String keyword = view.txtTim.getText().trim();

    // Nếu ô tìm rỗng → load lại toàn bộ khoa
    if (keyword.isEmpty()) {
        loadKhoa();
        return;
    }

    DefaultTableModel model =
            (DefaultTableModel) view.table.getModel();
    model.setRowCount(0);

    try (Connection c = DBUtil.getConnection()) {
        PreparedStatement p = c.prepareStatement(
            "SELECT MaKhoa, TenKhoa FROM khoa WHERE TenKhoa LIKE ?"
        );

        p.setString(1, "%" + keyword + "%");

        ResultSet rs = p.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("MaKhoa"),
                rs.getString("TenKhoa")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(view,
                "Lỗi tìm kiếm khoa");
        e.printStackTrace();
    }
}
   // "WHERE MaKhoa LIKE ? OR TenKhoa LIKE ?"
}

