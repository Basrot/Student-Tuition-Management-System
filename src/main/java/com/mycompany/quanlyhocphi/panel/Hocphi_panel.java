package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Hocphi_panel extends JPanel {

    // ===== TOP =====
    public JTextField txtMaSV;
    public JButton btnTim;
    public JLabel lblTenSV;
    public JLabel lblDoiTuong;

    // ===== TABLE =====
    public JTable tblHocPhi;
    public DefaultTableModel model;

    // ===== BOTTOM =====
    public JTextField txtTongHocPhi;
    public JTextField txtTyLeGiam;
    public JTextField txtMienGiam;
    public JTextField txtPhaiDong;

    // NEW: thanh toán
    public JButton btnThanhToan;
    public JLabel lblTrangThai;

    public Hocphi_panel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // ===== TOP =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.add(new JLabel("Mã SV:"));

        txtMaSV = new JTextField(14);
        top.add(txtMaSV);

        btnTim = new JButton("Tìm");
        top.add(btnTim);

        lblTenSV = new JLabel("Họ tên: ");
        lblTenSV.setFont(lblTenSV.getFont().deriveFont(Font.BOLD));
        top.add(lblTenSV);

        lblDoiTuong = new JLabel("Đối tượng: ");
        top.add(lblDoiTuong);

        // trạng thái
        lblTrangThai = new JLabel("Trạng thái: CHƯA ĐÓNG");
        lblTrangThai.setFont(lblTrangThai.getFont().deriveFont(Font.BOLD));
        top.add(lblTrangThai);

        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new Object[]{"Mã LHP", "Tên môn", "Tín chỉ", "Đơn giá", "Thành tiền"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblHocPhi = new JTable(model);
        tblHocPhi.setRowHeight(24);
        add(new JScrollPane(tblHocPhi), BorderLayout.CENTER);

        // ===== BOTTOM (GIỐNG ẢNH) =====
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBorder(new EmptyBorder(10, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;

        gbc.gridx = 0; bottom.add(new JLabel("Tổng học phí:"), gbc);
        gbc.gridx = 1; txtTongHocPhi = makeReadOnlyField(18); bottom.add(txtTongHocPhi, gbc);

        gbc.gridx = 2; bottom.add(new JLabel("Tỷ lệ giảm:"), gbc);
        gbc.gridx = 3; txtTyLeGiam = makeReadOnlyField(18); bottom.add(txtTyLeGiam, gbc);

        gbc.gridy = 1;

        gbc.gridx = 0; bottom.add(new JLabel("Miễn giảm:"), gbc);
        gbc.gridx = 1; txtMienGiam = makeReadOnlyField(18); bottom.add(txtMienGiam, gbc);

        gbc.gridx = 2; bottom.add(new JLabel("Phải đóng:"), gbc);
        gbc.gridx = 3; txtPhaiDong = makeReadOnlyField(18); bottom.add(txtPhaiDong, gbc);

        // Nút thanh toán hàng 2
        gbc.gridy = 2;
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        btnThanhToan = new JButton("Thanh toán (chưa đóng)");
        bottom.add(btnThanhToan, gbc);

        add(bottom, BorderLayout.SOUTH);

        clearSummary();
    }

    private JTextField makeReadOnlyField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setEditable(false);
        tf.setBackground(new Color(245, 245, 245));
        return tf;
    }

    public void clearSummary() {
        txtTongHocPhi.setText("0");
        txtTyLeGiam.setText("0%");
        txtMienGiam.setText("0");
        txtPhaiDong.setText("0");
        lblTrangThai.setText("Trạng thái: CHƯA ĐÓNG");
        btnThanhToan.setText("Thanh toán (chưa đóng)");
        btnThanhToan.setEnabled(false); // chỉ bật sau khi tìm SV có học phí
    }
}
