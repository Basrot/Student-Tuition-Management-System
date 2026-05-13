package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.MienGiam_panel;

import javax.swing.*;
import java.sql.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;

public class MienGiam_controller {

    private Connection conn;
    private final MienGiam_panel panel;
    
    private void loadTenSVByMaSV() {
    String maSV = panel.txtMaSV.getText().trim();
    if (maSV.isEmpty()) {
        panel.txtTenSV.setText("");
        return;
    }

    try (PreparedStatement ps = conn.prepareStatement(
            "SELECT HoTen FROM sinhvien WHERE MaSV = ?")) {
        ps.setString(1, maSV);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            panel.txtTenSV.setText(rs.getString("HoTen"));
        } else {
            panel.txtTenSV.setText("");
            JOptionPane.showMessageDialog(panel, "Không tìm thấy sinh viên!");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Lỗi lấy họ tên: " + ex.getMessage());
    }
}
    
    private void exportAndOpenExcel(JTable table) {
    try {
        // Lưu ra Desktop cho dễ tìm
        String desktop = System.getProperty("user.home") + File.separator + "Desktop";
        File file = new File(desktop, "ds_mien_giam.xlsx");

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("DS Mien Giam");

            // Header
            Row header = sheet.createRow(0);
            for (int c = 0; c < table.getColumnCount(); c++) {
                header.createCell(c).setCellValue(table.getColumnName(c));
            }

            // Data
            for (int r = 0; r < table.getRowCount(); r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < table.getColumnCount(); c++) {
                    Object val = table.getValueAt(r, c);
                    row.createCell(c).setCellValue(val == null ? "" : val.toString());
                }
            }

            // Auto size
            for (int c = 0; c < table.getColumnCount(); c++) {
                sheet.autoSizeColumn(c);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }

        // Mở luôn bằng Excel (nếu máy có Excel / app mặc định mở .xlsx)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        }

        JOptionPane.showMessageDialog(panel,
                "Đã xuất và mở file:\n" + file.getAbsolutePath());

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Xuất/Mở Excel thất bại: " + ex.getMessage());
    }
}

    public MienGiam_controller(MienGiam_panel panel) {
        this.panel = panel;
        connectDB();
        loadDoiTuong();
        loadCombo();
        loadSinhVienMienGiam();
        event();
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quanlyhocphi?useSSL=false",
                    "root", ""
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL");
        }
    }

    /* ================== LOAD ================== */
    private void loadDoiTuong() {
        panel.modelDT.setRowCount(0);
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM doituong_miengiam");
            while (rs.next()) {
                panel.modelDT.addRow(new Object[]{
                        rs.getString("MaDT"),
                        rs.getString("TenDT"),
                        rs.getDouble("TyLeGiam")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCombo() {
        panel.cbDoiTuong.removeAllItems();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT MaDT FROM doituong_miengiam");
            while (rs.next()) {
                panel.cbDoiTuong.addItem(rs.getString("MaDT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void loadSinhVienMienGiam() {
    panel.modelSV.setRowCount(0);

    String sql = """
        SELECT sv.MaSV, sv.HoTen,
               dt.MaDT,
               dt.TyLeGiam
        FROM sinhvien sv
        LEFT JOIN doituong_miengiam dt
          ON sv.MaDT COLLATE utf8mb4_unicode_ci
           = dt.MaDT COLLATE utf8mb4_unicode_ci
        WHERE COALESCE(dt.TyLeGiam, 0) > 0
    """;

    try (Statement st = conn.createStatement()) {
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            panel.modelSV.addRow(new Object[]{
                    rs.getString("MaSV"),
                    rs.getString("HoTen"),
                    rs.getString("MaDT"),
                    rs.getDouble("TyLeGiam")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Lỗi load SV miễn giảm: " + e.getMessage());
    }
}
    private double getTyLeGiam(String maDT) {
    try (PreparedStatement ps = conn.prepareStatement(
            "SELECT TyLeGiam FROM doituong_miengiam WHERE MaDT = ?")) {
        ps.setString(1, maDT);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getDouble("TyLeGiam");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

    /* ================== EVENT ================== */
    private void event() {
        panel.tblSinhVien.getSelectionModel().addListSelectionListener(e -> {
    if (e.getValueIsAdjusting()) return;

    int r = panel.tblSinhVien.getSelectedRow();
    if (r >= 0) {
        String maSV = panel.modelSV.getValueAt(r, 0).toString();
        panel.txtMaSV.setText(maSV);

        // Load họ tên từ DB (chuẩn nhất) hoặc lấy luôn từ bảng
        loadTenSVByMaSV(); // sẽ set txtTenSV

        // set đối tượng từ cột MaDT của bảng (cột index 2)
        Object maDTObj = panel.modelSV.getValueAt(r, 2);
        if (maDTObj != null) {
            panel.cbDoiTuong.setSelectedItem(maDTObj.toString());
        }
    }
});
        
        panel.tblDoiTuong.getSelectionModel().addListSelectionListener(e -> {
    if (e.getValueIsAdjusting()) return;

    int r = panel.tblDoiTuong.getSelectedRow();
    if (r >= 0) {
        String maDT = panel.modelDT.getValueAt(r, 0).toString();
        panel.cbDoiTuong.setSelectedItem(maDT);

        // đồng thời đổ lên 3 ô bên trái (nếu bạn muốn sửa/xóa nhanh)
        panel.txtMaDT.setText(maDT);
        panel.txtTenDT.setText(panel.modelDT.getValueAt(r, 1).toString());
        panel.txtTyLeGiam.setText(panel.modelDT.getValueAt(r, 2).toString());
    }
});


        panel.txtMaSV.addActionListener(e -> loadTenSVByMaSV());


        panel.btnThem.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO doituong_miengiam VALUES (?,?,?)");
                ps.setString(1, panel.txtMaDT.getText());
                ps.setString(2, panel.txtTenDT.getText());
                ps.setDouble(3, Double.parseDouble(panel.txtTyLeGiam.getText()));
                ps.executeUpdate();
                loadDoiTuong();
                loadCombo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Thêm thất bại");
            }
        });

        panel.btnSua.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE doituong_miengiam SET TenDT=?, TyLeGiam=? WHERE MaDT=?");
                ps.setString(1, panel.txtTenDT.getText());
                ps.setDouble(2, Double.parseDouble(panel.txtTyLeGiam.getText()));
                ps.setString(3, panel.txtMaDT.getText());
                ps.executeUpdate();
                loadDoiTuong();
                loadCombo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Sửa thất bại");
            }
        });

        panel.btnXoa.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM doituong_miengiam WHERE MaDT=?");
                ps.setString(1, panel.txtMaDT.getText());
                ps.executeUpdate();
                loadDoiTuong();
                loadCombo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Không thể xóa (đang được sử dụng)");
            }
        });

        panel.btnGan.addActionListener(e -> {
    try {
        String maSV = panel.txtMaSV.getText().trim();
        if (maSV.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Nhập Mã SV!");
            return;
        }

        Object sel = panel.cbDoiTuong.getSelectedItem();
        if (sel == null) {
            JOptionPane.showMessageDialog(panel, "Chưa có đối tượng miễn giảm!");
            return;
        }
        String maDT = sel.toString().trim();

        PreparedStatement ps = conn.prepareStatement(
                "UPDATE sinhvien SET MaDT=? WHERE MaSV=?");
        ps.setString(1, maDT);
        ps.setString(2, maSV);

        int row = ps.executeUpdate();

        if (row > 0) {
    double tl = getTyLeGiam(maDT);

    JOptionPane.showMessageDialog(
            panel,
            "Sinh viên " + panel.txtTenSV.getText()
                    + " đã được áp dụng đối tượng miễn giảm "
                    + maDT + " (" + (int)(tl * 100) + "%).",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE
    );
} else {
    JOptionPane.showMessageDialog(
            panel,
            "Không có thay đổi (Mã SV không tồn tại hoặc dữ liệu giữ nguyên).",
            "Thông báo",
            JOptionPane.WARNING_MESSAGE
    );
}


        loadSinhVienMienGiam();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Gán thất bại: " + ex.getMessage());
    }
});

        panel.btnHuyGan.addActionListener(e -> {
    try {
        String maSV = panel.txtMaSV.getText().trim();
        if (maSV.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Nhập Mã SV!");
            return;
        }

        PreparedStatement ps = conn.prepareStatement(
                "UPDATE sinhvien SET MaDT='DT00' WHERE MaSV=?");
        ps.setString(1, maSV);

        int row = ps.executeUpdate();

        if (row > 0) {
            JOptionPane.showMessageDialog(
                    panel,
                    "Sinh viên " + panel.txtTenSV.getText()
                            + " đã được hủy áp dụng miễn giảm học phí.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    panel,
                    "Không tìm thấy sinh viên để hủy gán.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        loadSinhVienMienGiam();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Hủy gán thất bại: " + ex.getMessage());
    }
});


        panel.btnXuat.addActionListener(e -> exportAndOpenExcel(panel.tblSinhVien));
    }
}
