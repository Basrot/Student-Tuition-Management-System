package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.Lop_panel;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.awt.Desktop;
import java.io.File;

public class Lop_controller {

    private Lop_panel view;
    private Connection conn;

    public Lop_controller(Lop_panel view) {
        this.view = view;
        ketNoiDB();
        loadKhoa();
        loadTable();
        suKien();
        view.txtSS.setEditable(false);
    }

    // ================= KẾT NỐI DB =================
    private void ketNoiDB() {
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

    // ================= SỰ KIỆN =================
    private void suKien() {

        view.cbKHOA.addActionListener(e -> {
            if (view.cbKHOA.getSelectedItem() != null) {
                String maKhoa = view.cbKHOA.getSelectedItem().toString().split(" - ")[0];
                loadNganh(maKhoa);
            }
        });

        view.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = view.table.getSelectedRow();
                if (r == -1) return;

                String maLop = view.table.getValueAt(r, 0).toString();
                String tenKhoa = view.table.getValueAt(r, 2).toString();
                String tenNganh = view.table.getValueAt(r, 3).toString();

                view.txtML.setText(maLop);
                view.txtTL.setText(view.table.getValueAt(r, 1).toString());
                view.txtSS.setText(view.table.getValueAt(r, 4).toString());

                setComboByName(view.cbKHOA, tenKhoa);
                String maKhoa = view.cbKHOA.getSelectedItem().toString().split(" - ")[0];
                loadNganh(maKhoa);
                setComboByName(view.cbNganh, tenNganh);

                loadSinhVienTheoLop(maLop);
            }
        });

        view.btnThem.addActionListener(e -> themLop());
        view.btnSua.addActionListener(e -> suaLop());
        view.btnXoa.addActionListener(e -> xoaLop());
        view.btnXuat.addActionListener(e -> xuatExcel());
    }

    // ================= HỖ TRỢ SET COMBO =================
    private void setComboByName(JComboBox<String> cb, String ten) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).contains(ten)) {
                cb.setSelectedIndex(i);
                return;
            }
        }
    }

    // ================= LOAD TABLE =================
    private void loadTable() {
        try {
            DefaultTableModel model = view.model;
            model.setRowCount(0);

            String sql = """
                SELECT l.MaLop, l.TenLop, k.TenKhoa, n.TenNganh,
                       COUNT(s.MaSV) AS SiSo
                FROM lop l
                JOIN khoa k ON l.MaKhoa = k.MaKhoa
                JOIN nganh n ON l.MaNganh = n.MaNganh
                LEFT JOIN sinhvien s ON l.MaLop = s.MaLop
                GROUP BY l.MaLop, l.TenLop, k.TenKhoa, n.TenNganh
            """;

            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
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

    // ================= LOAD SINH VIÊN =================
    private void loadSinhVienTheoLop(String maLop) {
        try {
            DefaultTableModel modelSV = view.modelSV;
            modelSV.setRowCount(0);

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT MaSV, HoTen, GioiTinh, Email FROM sinhvien WHERE MaLop=?"
            );
            ps.setString(1, maLop);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelSV.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD KHOA / NGÀNH =================
    private void loadKhoa() {
        try {
            view.cbKHOA.removeAllItems();
            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT MaKhoa, TenKhoa FROM khoa");

            while (rs.next()) {
                view.cbKHOA.addItem(rs.getString(1) + " - " + rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNganh(String maKhoa) {
        try {
            view.cbNganh.removeAllItems();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT MaNganh, TenNganh FROM nganh WHERE MaKhoa=?");
            ps.setString(1, maKhoa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                view.cbNganh.addItem(rs.getString(1) + " - " + rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= THÊM / SỬA / XÓA =================
    private void themLop() {
        try {
            if (view.cbKHOA.getSelectedItem() == null || view.cbNganh.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(view, "Chọn khoa và ngành!");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO lop(MaLop,TenLop,MaKhoa,MaNganh,Khoa)
                VALUES (?,?,?,?,?)
            """);

            int khoa = Integer.parseInt(view.txtML.getText().substring(0, 2));

            ps.setString(1, view.txtML.getText());
            ps.setString(2, view.txtTL.getText());
            ps.setString(3, view.cbKHOA.getSelectedItem().toString().split(" - ")[0]);
            ps.setString(4, view.cbNganh.getSelectedItem().toString().split(" - ")[0]);
            ps.setInt(5, khoa);

            ps.executeUpdate();
            loadTable();
            JOptionPane.showMessageDialog(view, "Thêm lớp thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void suaLop() {
        try {
            if (view.cbKHOA.getSelectedItem() == null || view.cbNganh.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(view, "Chưa chọn khoa / ngành");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("""
                UPDATE lop SET TenLop=?,MaKhoa=?,MaNganh=?,Khoa=? WHERE MaLop=?
            """);

            int khoa = Integer.parseInt(view.txtML.getText().substring(0, 2));

            ps.setString(1, view.txtTL.getText());
            ps.setString(2, view.cbKHOA.getSelectedItem().toString().split(" - ")[0]);
            ps.setString(3, view.cbNganh.getSelectedItem().toString().split(" - ")[0]);
            ps.setInt(4, khoa);
            ps.setString(5, view.txtML.getText());

            ps.executeUpdate();
            loadTable();
            JOptionPane.showMessageDialog(view, "Sửa thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xoaLop() {
        try {
            int r = view.table.getSelectedRow();
            if (r == -1) return;

            String maLop = view.table.getValueAt(r, 0).toString();

            PreparedStatement check = conn.prepareStatement(
                    "SELECT COUNT(*) FROM sinhvien WHERE MaLop=?");
            check.setString(1, maLop);
            ResultSet rs = check.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(view, "Lớp đang có sinh viên");
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM lop WHERE MaLop=?");
            ps.setString(1, maLop);
            ps.executeUpdate();

            loadTable();
            JOptionPane.showMessageDialog(view, "Xóa thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= XUẤT EXCEL =================
    private void xuatExcel() {
        try {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("DanhSachLop");

            DefaultTableModel model = view.model;
            Row header = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                header.createCell(i).setCellValue(model.getColumnName(i));
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                Row r = sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    r.createCell(j).setCellValue(model.getValueAt(i, j).toString());
                }
            }

            String path = System.getProperty("user.home") + "/Desktop/DanhSachLop.xlsx";
            FileOutputStream fos = new FileOutputStream(path);
            wb.write(fos);
            fos.close();
            wb.close();

            Desktop.getDesktop().open(new File(path));
            JOptionPane.showMessageDialog(view, "Xuất Excel thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
