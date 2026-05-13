/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.Namhoc_panel;
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
/**
 *
 * @author Admin
 */
public class Namhoc_controller {
    private Namhoc_panel view;
    private Connection conn;
    
    public Namhoc_controller(Namhoc_panel view) {
        this.view = view;
        connectDB();
        loadTable();
        addEvents();
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
    private void loadTable() {
        DefaultTableModel model =
            (DefaultTableModel) view.table.getModel();
        model.setRowCount(0);

        String sql = "SELECT * FROM hocki";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaHocKi"),
                    rs.getString("TenHocKi"),
                    rs.getString("NamHoc")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void themHocKi() {
        String sql = "INSERT INTO hocki VALUES (?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, view.txtMHK.getText());
            ps.setString(2, view.cbHK.getSelectedItem().toString());
            ps.setString(3, view.cbNH.getSelectedItem().toString());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Thêm học kỳ thành công");
            loadTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Mã học kỳ đã tồn tại!");
        }
    }
    private void suaHocKi() {
        String sql = "UPDATE hocki SET TenHocKi=?, NamHoc=? WHERE MaHocKi=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, view.cbHK.getSelectedItem().toString());
            ps.setString(2, view.cbNH.getSelectedItem().toString());
            ps.setString(3, view.txtMHK.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Sửa thành công");
            loadTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void xoaHocKi() {
        int c = JOptionPane.showConfirmDialog(
            view,
            "Bạn có chắc muốn xóa?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );
        if (c != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM hocki WHERE MaHocKi=?"; 
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, view.txtMHK.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Xóa thành công");
            loadTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                view,
                "Không thể xóa vì học kỳ đang được sử dụng!"
            );
        }
    }
    private void addEvents() {

        view.btnThem.addActionListener(e -> themHocKi());
        view.btnSua.addActionListener(e -> suaHocKi());
        view.btnXoa.addActionListener(e -> xoaHocKi());
        view.btnXuat.addActionListener(e -> xuatExcelNamHoc());
        view.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.table.getSelectedRow();
                view.txtMHK.setText(
                    view.table.getValueAt(row, 0).toString()
                );
                view.cbHK.setSelectedItem(
                    view.table.getValueAt(row, 1).toString()
                );
                view.cbNH.setSelectedItem(
                    view.table.getValueAt(row, 2).toString()
                );
            }
        });
    }
    private void xuatExcelNamHoc() {
    try {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách năm học");

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
                + "/Desktop/DanhSachNamHoc.xlsx";
        File file = new File(path);

        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        workbook.close();

        Desktop.getDesktop().open(file);

        JOptionPane.showMessageDialog(view,
                "Xuất Excel năm học thành công và đã mở file!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(view,
                "Lỗi xuất Excel năm học!");
    }
}
    
}
