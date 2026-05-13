package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.Hocphi_panel;
import com.mycompany.quanlyhocphi.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Hocphi_controller {

    private final Hocphi_panel panel;
    private Connection conn;

    private double tyLeGiam = 0.0;
    private String maSVHienTai = null; // NEW

    public Hocphi_controller(Hocphi_panel panel) {
        this.panel = panel;

        try {
            conn = DBUtil.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Không kết nối được CSDL!");
            return;
        }

        ensurePaymentTable(); // NEW
        addEvent();
    }

    private void addEvent() {
        panel.btnTim.addActionListener(e -> timVaTinhHocPhi());
        panel.txtMaSV.addActionListener(e -> timVaTinhHocPhi());

        panel.btnThanhToan.addActionListener(e -> thanhToan());
    }

    // ===== Tạo bảng thanh toán đơn giản (không công nợ, không phiếu thu) =====
    private void ensurePaymentTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS thanhtoan_hocphi (
                MaSV VARCHAR(20) NOT NULL,
                HocKy VARCHAR(20) DEFAULT '',
                TongHocPhi BIGINT NOT NULL DEFAULT 0,
                TyLeGiam DOUBLE NOT NULL DEFAULT 0,
                TienGiam BIGINT NOT NULL DEFAULT 0,
                PhaiDong BIGINT NOT NULL DEFAULT 0,
                DaDong TINYINT(1) NOT NULL DEFAULT 0,
                NgayDong TIMESTAMP NULL DEFAULT NULL,
                PRIMARY KEY (MaSV, HocKy)
            )
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (Exception e) {
            // nếu quyền DB không cho create table thì bỏ qua (nhưng sẽ không lưu được)
            System.out.println("Không tạo được bảng thanhtoan_hocphi: " + e.getMessage());
        }
    }

    private void resetUI() {
        maSVHienTai = null;
        panel.lblTenSV.setText("Họ tên: ");
        panel.lblDoiTuong.setText("Đối tượng: ");
        tyLeGiam = 0.0;

        ((DefaultTableModel) panel.tblHocPhi.getModel()).setRowCount(0);
        panel.clearSummary();
    }

    private void timVaTinhHocPhi() {
        String maSV = panel.txtMaSV.getText().trim();
        if (maSV.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Vui lòng nhập mã sinh viên!");
            return;
        }

        resetUI();

        // ===== 1) Lấy SV + miễn giảm =====
        String sqlSV = """
            SELECT 
                sv.HoTen,
                dt.TenDT,
                IFNULL(dt.TyLeGiam, 0) AS TyLeGiam
            FROM sinhvien sv
            LEFT JOIN doituong_miengiam dt
              ON sv.MaDT COLLATE utf8mb4_general_ci
               = dt.MaDT COLLATE utf8mb4_general_ci
            WHERE sv.MaSV COLLATE utf8mb4_general_ci = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sqlSV)) {
            ps.setString(1, maSV);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(panel, "Không tìm thấy sinh viên!");
                    return;
                }

                String tenSV = rs.getString("HoTen");
                String tenDT = rs.getString("TenDT");
                tyLeGiam = rs.getDouble("TyLeGiam");

                if (tenDT == null || tenDT.isBlank()) tenDT = "Không miễn giảm";
                if (tyLeGiam < 0) tyLeGiam = 0;
                if (tyLeGiam > 1) tyLeGiam = 1;

                panel.lblTenSV.setText("Họ tên: " + tenSV);
                panel.lblDoiTuong.setText("Đối tượng: " + tenDT);

                maSVHienTai = maSV;
            }

            // ===== 2) Load học phí + update tổng =====
            long[] calc = loadHocPhiVaTinhTong(maSV);
            long tong = calc[0];
            long tienGiam = calc[1];
            long phaiDong = calc[2];

            // ===== 3) Kiểm tra đã đóng chưa =====
            boolean daDong = checkDaDong(maSV, ""); // hocKy để rỗng cho đơn giản
            updateTrangThai(daDong);

            // bật nút thanh toán nếu có học phí và chưa đóng
            panel.btnThanhToan.setEnabled(tong > 0 && !daDong);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }

    private long[] loadHocPhiVaTinhTong(String maSV) throws Exception {
        DefaultTableModel model = (DefaultTableModel) panel.tblHocPhi.getModel();
        model.setRowCount(0);

        long tongTien = 0;

        String sql = """
            SELECT 
                lhp.MaLHP,
                mh.TenMon,
                lhp.SoTinChi,
                IFNULL(dg.DonGia, 0) AS DonGia
            FROM dangkyhocphan dk
            JOIN lophocphan lhp
              ON dk.MaLHP COLLATE utf8mb4_general_ci
               = lhp.MaLHP COLLATE utf8mb4_general_ci
            JOIN monhoc mh ON lhp.MaMon = mh.MaMon
            LEFT JOIN dongia_tinchi dg ON dg.MaMon = mh.MaMon
            WHERE dk.MaSV COLLATE utf8mb4_general_ci = ?
            ORDER BY lhp.MaLHP
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int tinChi = rs.getInt("SoTinChi");
                    long donGia = rs.getLong("DonGia");
                    long thanhTien = (long) tinChi * donGia;

                    tongTien += thanhTien;

                    model.addRow(new Object[]{
                            rs.getString("MaLHP"),
                            rs.getString("TenMon"),
                            tinChi,
                            donGia,
                            thanhTien
                    });
                }
            }
        }

        long tienGiam = Math.round(tongTien * tyLeGiam);
        long phaiDong = tongTien - tienGiam;
        int percent = (int) Math.round(tyLeGiam * 100);

        panel.txtTongHocPhi.setText(String.format("%,d", tongTien));
        panel.txtTyLeGiam.setText(percent + "%");
        panel.txtMienGiam.setText(String.format("%,d", tienGiam));
        panel.txtPhaiDong.setText(String.format("%,d", phaiDong));

        return new long[]{tongTien, tienGiam, phaiDong};
    }

    private boolean checkDaDong(String maSV, String hocKy) {
        String sql = "SELECT DaDong FROM thanhtoan_hocphi WHERE MaSV = ? AND HocKy = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ps.setString(2, hocKy);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("DaDong") == 1;
                }
            }
        } catch (Exception e) {
            // nếu không có bảng thanhtoan_hocphi thì coi như chưa đóng
        }
        return false;
    }

    private void updateTrangThai(boolean daDong) {
        if (daDong) {
            panel.lblTrangThai.setText("Trạng thái: ĐÃ ĐÓNG");
            panel.btnThanhToan.setText("Thanh toán (đã đóng)");
            panel.btnThanhToan.setEnabled(false);
        } else {
            panel.lblTrangThai.setText("Trạng thái: CHƯA ĐÓNG");
            panel.btnThanhToan.setText("Thanh toán (chưa đóng)");
        }
    }

    private void thanhToan() {
        if (maSVHienTai == null) {
            JOptionPane.showMessageDialog(panel, "Vui lòng tìm sinh viên trước!");
            return;
        }

        long tong = parseMoney(panel.txtTongHocPhi.getText());
        long tienGiam = parseMoney(panel.txtMienGiam.getText());
        long phaiDong = parseMoney(panel.txtPhaiDong.getText());
        String hocKy = ""; // đơn giản: chưa tách học kỳ

        if (tong <= 0) {
            JOptionPane.showMessageDialog(panel, "Không có học phí để thanh toán!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                panel,
                "Xác nhận thanh toán học phí?\nPhải đóng: " + panel.txtPhaiDong.getText(),
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        // Upsert: nếu đã có record thì update, chưa có thì insert
        String sql = """
            INSERT INTO thanhtoan_hocphi (MaSV, HocKy, TongHocPhi, TyLeGiam, TienGiam, PhaiDong, DaDong, NgayDong)
            VALUES (?, ?, ?, ?, ?, ?, 1, NOW())
            ON DUPLICATE KEY UPDATE
                TongHocPhi = VALUES(TongHocPhi),
                TyLeGiam = VALUES(TyLeGiam),
                TienGiam = VALUES(TienGiam),
                PhaiDong = VALUES(PhaiDong),
                DaDong = 1,
                NgayDong = NOW()
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSVHienTai);
            ps.setString(2, hocKy);
            ps.setLong(3, tong);
            ps.setDouble(4, tyLeGiam);
            ps.setLong(5, tienGiam);
            ps.setLong(6, phaiDong);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(panel, "Thanh toán thành công!");
            updateTrangThai(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Không lưu được thanh toán!\n" + e.getMessage());
        }
    }

    private long parseMoney(String s) {
        try {
            return Long.parseLong(s.replace(",", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
