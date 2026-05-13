package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.Diem_panel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Diem_controller {

    private Diem_panel view;
    private Connection conn;

    public Diem_controller(Diem_panel view) {
        this.view = view;
        connectDB();
        loadKhoa();
        addEvents();
        NhapDiem(view.txtCC);
        NhapDiem(view.txtGK);
        NhapDiem(view.txtCK);
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quanlyhocphi",
                "root", ""
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== LOAD KHOA =====
    private void loadKhoa() {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MaKhoa FROM khoa")) {
            while (rs.next()) {
                view.cbKHOA.addItem(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== LOAD NGÀNH =====
    private void loadNganh() {
        view.cbNganh.removeAllItems();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT MaNganh FROM nganh WHERE MaKhoa=?")) {
            ps.setString(1, view.cbKHOA.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                view.cbNganh.addItem(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean kiemTraDiem(String diem) {
    try {
        float d = Float.parseFloat(diem);
        return d >= 0 && d <= 10;
    } catch (NumberFormatException e) {
        return false;
    }
}


    // ===== TÌM KIẾM SINH VIÊN =====
    private void timKiem() {
        DefaultTableModel model = (DefaultTableModel) view.table.getModel();
        model.setRowCount(0);

        String sql = """
            SELECT sv.MaSV, sv.HoTen,
                   IFNULL(d.DiemCC,0),
                   IFNULL(d.DiemGK,0),
                   IFNULL(d.DiemCK,0),
                   IFNULL(d.DiemTB,0)
            FROM sinhvien sv
            LEFT JOIN diem d ON sv.MaSV = d.MaSV
            WHERE sv.MaKhoa=? AND sv.MaNganh=?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, view.cbKHOA.getSelectedItem().toString());
            ps.setString(2, view.cbNganh.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getFloat(3),
                    rs.getFloat(4),
                    rs.getFloat(5),
                    rs.getFloat(6)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== CLICK TABLE =====
    private void clickTable() {
        int r = view.table.getSelectedRow();
        if (r == -1) return;

        view.txtCC.setText(view.table.getValueAt(r, 2).toString());
        view.txtGK.setText(view.table.getValueAt(r, 3).toString());
        view.txtCK.setText(view.table.getValueAt(r, 4).toString());
    }


   // ===== LƯU ĐIỂM =====
    private void luu() {
        int r = view.table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(view, "Chọn sinh viên trong bảng");
            return;
        }

        try {
            String maSV = view.table.getValueAt(r, 0).toString();
            float cc = Float.parseFloat(view.txtCC.getText());
            float gk = Float.parseFloat(view.txtGK.getText());
            float ck = Float.parseFloat(view.txtCK.getText());
            float tb = cc * 0.1f + gk * 0.3f + ck * 0.6f;

            String sql = """
                INSERT INTO diem VALUES (?,?,?,?,?)
                ON DUPLICATE KEY UPDATE
                DiemCC=?, DiemGK=?, DiemCK=?, DiemTB=?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maSV);
            ps.setFloat(2, cc);
            ps.setFloat(3, gk);
            ps.setFloat(4, ck);
            ps.setFloat(5, tb);
            ps.setFloat(6, cc);
            ps.setFloat(7, gk);
            ps.setFloat(8, ck);
            ps.setFloat(9, tb);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Lưu điểm thành công");
            timKiem();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
private void NhapDiem(JTextField txt) {
    txt.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            String s = txt.getText();

            if (s.isEmpty()) return;

            try {
                float d = Float.parseFloat(s);

                if (d < 0 || d > 10) {
                    JOptionPane.showMessageDialog(
                        view,
                        "Điểm phải từ 0 đến 10",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );
                    txt.setText("");
                    txt.requestFocus();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    view,
                    "Chỉ được nhập số",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
                txt.setText("");
                txt.requestFocus();
            }
        }
    });
}

private void addEvents() {
        view.cbKHOA.addActionListener(e -> loadNganh());
        view.btnTK.addActionListener(e -> timKiem());
        view.table.getSelectionModel()
            .addListSelectionListener(e -> clickTable());
        view.btnLuu.addActionListener(e -> luu());
        view.btnXuat.addActionListener(e -> xuatExcelDiem());
    }

private void xuatExcelDiem() {
    try {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách điểm");

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
                + "/Desktop/DanhSachDiem.xlsx";
        File file = new File(path);

        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        workbook.close();

        Desktop.getDesktop().open(file);

        JOptionPane.showMessageDialog(view,
                "Xuất Excel điểm thành công và đã mở file!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(view,
                "Lỗi xuất Excel điểm!");
    }
}
}
