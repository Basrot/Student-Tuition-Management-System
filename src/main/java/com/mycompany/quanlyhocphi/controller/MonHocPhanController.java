package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.DK_MHP_Panel;
import java.awt.Desktop;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MonHocPhanController {

    private DK_MHP_Panel view;
    private Connection conn;

    public MonHocPhanController(DK_MHP_Panel view) {
        this.view = view;

        if (view != null) {
            connectDB();
            loadKhoa();

            // Chỉ cho nhập số ở ô Số tín chỉ
            ((AbstractDocument) view.txtSoTC.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                        throws BadLocationException {
                    if (string != null && string.matches("\\d+")) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    if (text == null || text.matches("\\d*")) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });

            view.cbKHOA.addActionListener(e -> loadNganh());
            view.cbNganh.addActionListener(e -> loadTableTheoNganh());
            view.btnXuat.addActionListener(e -> xuatExcelMonHoc());
            view.btnThem.addActionListener(new ThemAction());
            view.btnSua.addActionListener(new SuaAction());
            view.btnXoa.addActionListener(new XoaAction());
            view.btnTim.addActionListener(e -> timMonHoc());

            view.table.addMouseListener(new TableClick());
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
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MaKhoa FROM khoa")) {
            while (rs.next()) {
                view.cbKHOA.addItem(rs.getString("MaKhoa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNganh() {
        view.cbNganh.removeAllItems();
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

    private void loadTableTheoNganh() {
        if (view.cbKHOA.getSelectedItem() == null || view.cbNganh.getSelectedItem() == null) {
            return;
        }

        view.model.setRowCount(0);

        String sql = """
            SELECT k.TenKhoa, n.TenNganh, m.MaMon, m.TenMon, m.SoTinChi
            FROM monhoc m
            JOIN khoa k ON m.MaKhoa = k.MaKhoa
            JOIN nganh n ON m.MaNganh = n.MaNganh
            WHERE m.MaKhoa = ? AND m.MaNganh = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, view.cbKHOA.getSelectedItem().toString());
            ps.setString(2, view.cbNganh.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                view.model.addRow(new Object[]{
                    rs.getString(1), // Tên khoa
                    rs.getString(2), // Tên ngành
                    rs.getString(3), // Mã môn
                    rs.getString(4), // Tên môn
                    rs.getInt(5)     // Số tín chỉ
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetForm() {
        view.txtMa.setText("");
        view.txtTen.setText("");
        view.txtSoTC.setText("");
        view.table.clearSelection();
    }

    public String validateMonHoc(Object khoa, Object nganh, String maMon, String tenMon, String soTinChi) {
        String khoaStr = (khoa == null) ? "" : khoa.toString().trim();
        String nganhStr = (nganh == null) ? "" : nganh.toString().trim();
        String maMonStr = (maMon == null) ? "" : maMon.trim();
        String tenMonStr = (tenMon == null) ? "" : tenMon.trim();
        String soTinChiStr = (soTinChi == null) ? "" : soTinChi.trim();

        if (khoaStr.isEmpty() || nganhStr.isEmpty() || maMonStr.isEmpty()
                || tenMonStr.isEmpty() || soTinChiStr.isEmpty()) {
            return "Các ô không được để trống!";
        }

        if (!soTinChiStr.matches("\\d+")) {
            return "Số tín chỉ không quá 10 tín";
        }

        int stc = Integer.parseInt(soTinChiStr);
        if (stc > 10) {
            return "Số tín chỉ không quá 10 tín";
        }

        return "Hợp lệ";
    }

    private boolean isTrungMonHocKhiThem(String maMon, String tenMon) {
        String sql = "SELECT COUNT(*) FROM monhoc WHERE MaMon = ? OR TenMon = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon.trim());
            ps.setString(2, tenMon.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isTrungMonHocKhiSua(String maMon, String tenMon) {
        String sql = "SELECT COUNT(*) FROM monhoc WHERE (MaMon = ? OR TenMon = ?) AND MaMon <> ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon.trim());
            ps.setString(2, tenMon.trim());
            ps.setString(3, maMon.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    class ThemAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String check = validateMonHoc(
                view.cbKHOA.getSelectedItem(),
                view.cbNganh.getSelectedItem(),
                view.txtMa.getText(),
                view.txtTen.getText(),
                view.txtSoTC.getText()
            );

            if (!check.equals("Hợp lệ")) {
                JOptionPane.showMessageDialog(view, check);
                return;
            }

            String maMon = view.txtMa.getText().trim();
            String tenMon = view.txtTen.getText().trim();
            String soTinChi = view.txtSoTC.getText().trim();

            if (isTrungMonHocKhiThem(maMon, tenMon)) {
                JOptionPane.showMessageDialog(view, "Trùng môn học");
                return;
            }

            String sql = """
                INSERT INTO monhoc(MaMon, TenMon, SoTinChi, MaKhoa, MaNganh)
                VALUES (?, ?, ?, ?, ?)
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maMon);
                ps.setString(2, tenMon);
                ps.setInt(3, Integer.parseInt(soTinChi));
                ps.setString(4, view.cbKHOA.getSelectedItem().toString());
                ps.setString(5, view.cbNganh.getSelectedItem().toString());

                ps.executeUpdate();
                JOptionPane.showMessageDialog(view, "Thêm thành công");
                loadTableTheoNganh();
                resetForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi thêm môn học");
            }
        }
    }

    class SuaAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Chọn môn cần sửa");
                return;
            }

            String check = validateMonHoc(
                view.cbKHOA.getSelectedItem(),
                view.cbNganh.getSelectedItem(),
                view.txtMa.getText(),
                view.txtTen.getText(),
                view.txtSoTC.getText()
            );

            if (!check.equals("Hợp lệ")) {
                JOptionPane.showMessageDialog(view, check);
                return;
            }

            String maMon = view.txtMa.getText().trim();
            String tenMon = view.txtTen.getText().trim();
            String soTinChi = view.txtSoTC.getText().trim();

            if (isTrungMonHocKhiSua(maMon, tenMon)) {
                JOptionPane.showMessageDialog(view, "Trùng môn học");
                return;
            }

            String sql = "UPDATE monhoc SET TenMon = ?, SoTinChi = ? WHERE MaMon = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tenMon);
                ps.setInt(2, Integer.parseInt(soTinChi));
                ps.setString(3, maMon);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(view, "Sửa thành công");
                loadTableTheoNganh();
                resetForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi sửa môn học");
            }
        }
    }

    class XoaAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Chọn môn cần xóa");
                return;
            }

            int c = JOptionPane.showConfirmDialog(
                view,
                "Xóa môn này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
            );
            if (c != JOptionPane.YES_OPTION) {
                return;
            }

            String maMon = view.model.getValueAt(row, 2).toString();

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM monhoc WHERE MaMon = ?")) {
                ps.setString(1, maMon);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(view, "Xóa thành công");
                loadTableTheoNganh();
                resetForm();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Không thể xóa môn đang sử dụng");
            }
        }
    }

    class TableClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = view.table.getSelectedRow();
            if (row == -1) {
                return;
            }

            view.txtMa.setText(view.model.getValueAt(row, 2).toString());
            view.txtTen.setText(view.model.getValueAt(row, 3).toString());
            view.txtSoTC.setText(view.model.getValueAt(row, 4).toString());
        }
    }

    private void xuatExcelMonHoc() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách môn học phần");

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

            String path = System.getProperty("user.home") + "/Desktop/DanhSachMon.xlsx";
            File file = new File(path);

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

            Desktop.getDesktop().open(file);

            JOptionPane.showMessageDialog(view, "Xuất Excel Môn thành công và đã mở file!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi xuất Excel Môn!");
        }
    }

    private void timMonHoc() {
        if (view.cbKHOA.getSelectedItem() == null || view.cbNganh.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view, "Chọn Khoa và Ngành trước");
            return;
        }

        String key = view.txtTim.getText().trim();
        view.model.setRowCount(0);

        String sql = """
            SELECT k.TenKhoa, n.TenNganh, m.MaMon, m.TenMon, m.SoTinChi
            FROM monhoc m
            JOIN khoa k ON m.MaKhoa = k.MaKhoa
            JOIN nganh n ON m.MaNganh = n.MaNganh
            WHERE m.MaKhoa = ?
              AND m.MaNganh = ?
              AND (m.MaMon LIKE ? OR m.TenMon LIKE ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, view.cbKHOA.getSelectedItem().toString());
            ps.setString(2, view.cbNganh.getSelectedItem().toString());
            ps.setString(3, "%" + key + "%");
            ps.setString(4, "%" + key + "%");

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}