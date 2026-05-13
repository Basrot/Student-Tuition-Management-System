package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DonGiaTinChi_panel extends JPanel {

    public JComboBox<String> cbMaMon;
    public JTextField txtTenMon, txtSoTC, txtDonGia, txtTim;
    public JButton btnLuu, btnXoa, btnTim, btnXuat;

    public JTable table;
    public DefaultTableModel model;

    public DonGiaTinChi_panel() {
        setLayout(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Đơn giá tín chỉ"));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;

        // Mã môn
        g.gridx = 0; g.weightx = 0;
        form.add(new JLabel("Mã môn"), g);

        cbMaMon = new JComboBox<>();
        g.gridx = 1; g.weightx = 1.0;
        form.add(cbMaMon, g);

        // Tên môn
        g.gridx = 2; g.weightx = 0;
        form.add(new JLabel("Tên môn"), g);

        txtTenMon = new JTextField();
        txtTenMon.setEditable(false);
        g.gridx = 3; g.weightx = 2.0;
        form.add(txtTenMon, g);

        // Số tín chỉ
        g.gridx = 4; g.weightx = 0;
        form.add(new JLabel("Số tín chỉ"), g);

        txtSoTC = new JTextField();
        txtSoTC.setEditable(false);
        g.gridx = 5; g.weightx = 0.6;
        form.add(txtSoTC, g);

        // Đơn giá
        g.gridx = 6; g.weightx = 0;
        form.add(new JLabel("Đơn giá"), g);

        txtDonGia = new JTextField();
        g.gridx = 7; g.weightx = 0.8;
        form.add(txtDonGia, g);

        // Row buttons
        g.gridy = 1;
        g.gridx = 0; g.gridwidth = 5;
        form.add(new JLabel(""), g);

        btnLuu = new JButton("Lưu");
        g.gridx = 5; g.gridwidth = 1;
        form.add(btnLuu, g);

        btnXoa = new JButton("Xóa");
        g.gridx = 6;
        form.add(btnXoa, g);

        btnXuat = new JButton("Xuất bảng");
        g.gridx = 7;
        form.add(btnXuat, g);

        add(form, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"Mã môn","Tên môn","Số tín chỉ","Đơn giá"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Footer filter
        JPanel bot = new JPanel();
        txtTim = new JTextField(14);
        btnTim = new JButton("Lọc");

        bot.add(new JLabel("Tìm mã/tên"));
        bot.add(txtTim);
        bot.add(btnTim);

        add(bot, BorderLayout.SOUTH);
    }
}
