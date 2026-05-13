package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Phieudangky_panel extends JPanel {

    // ===== SINH VIÊN =====
    public JTextField txtMaSV, txtHoTen, txtLop, txtNganh, txtKhoa;

    // ===== BẢNG LHP =====
    public JTable tblLopHocPhan;
    public DefaultTableModel modelLHP;

    // ===== TỔNG =====
    public JTextField txtTongTinChi, txtTongHocPhi;
    public JButton btnLuuPhieu;

    public Phieudangky_panel() {
        setLayout(new BorderLayout(10, 10));

        // ================= TOP =================
        JPanel pnlTop = new JPanel(new GridLayout(2, 5, 10, 5));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        txtMaSV = new JTextField(); txtMaSV.setEditable(false);
        txtHoTen = new JTextField(); txtHoTen.setEditable(false);
        txtLop = new JTextField(); txtLop.setEditable(false);
        txtNganh = new JTextField(); txtNganh.setEditable(false);
        txtKhoa = new JTextField(); txtKhoa.setEditable(false);

        pnlTop.add(new JLabel("Mã SV"));
        pnlTop.add(new JLabel("Họ tên"));
        pnlTop.add(new JLabel("Lớp"));
        pnlTop.add(new JLabel("Ngành"));
        pnlTop.add(new JLabel("Khoa"));

        pnlTop.add(txtMaSV);
        pnlTop.add(txtHoTen);
        pnlTop.add(txtLop);
        pnlTop.add(txtNganh);
        pnlTop.add(txtKhoa);

        add(pnlTop, BorderLayout.NORTH);

        // ================= CENTER =================
        modelLHP = new DefaultTableModel(
                new Object[]{"Chọn", "Mã LHP", "Tên môn", "GV", "TC", "Đơn giá", "Thành tiền"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                if (columnIndex == 4) return Integer.class;
                if (columnIndex >= 5) return Double.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0;
            }
        };

        tblLopHocPhan = new JTable(modelLHP);
        add(new JScrollPane(tblLopHocPhan), BorderLayout.CENTER);

        // ================= BOTTOM =================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBorder(BorderFactory.createTitledBorder("Tổng kết"));

        txtTongTinChi = new JTextField(5);
        txtTongHocPhi = new JTextField(10);
        txtTongTinChi.setEditable(false);
        txtTongHocPhi.setEditable(false);

        btnLuuPhieu = new JButton("Lưu phiếu đăng ký");

        pnlBottom.add(new JLabel("Tổng TC:"));
        pnlBottom.add(txtTongTinChi);
        pnlBottom.add(new JLabel("Tổng HP:"));
        pnlBottom.add(txtTongHocPhi);
        pnlBottom.add(btnLuuPhieu);

        add(pnlBottom, BorderLayout.SOUTH);
    }
}
