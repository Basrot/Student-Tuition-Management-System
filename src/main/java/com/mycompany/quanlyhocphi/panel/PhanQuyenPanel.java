package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhanQuyenPanel extends JPanel {

    // ===== PUBLIC cho Controller =====
    public JTable table;
    public DefaultTableModel model;
    public JTextField txtUser, txtEmail;
    public JPasswordField txtPass;
    public JComboBox<String> cbRole;
    public JButton btnCreate;

    public PhanQuyenPanel() {
        setLayout(new BorderLayout(10, 10));

        /* ===== TITLE ===== */
        JLabel title = new JLabel("PHÂN QUYỀN - QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        /* ===== TABLE ===== */
        model = new DefaultTableModel(
                new String[]{"Username", "Email", "Password (Hash)", "Role"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        /* ===== FORM ===== */
        JPanel form = new JPanel(new GridLayout(2, 5, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtUser = new JTextField();
        txtEmail = new JTextField();
        txtPass = new JPasswordField();
        cbRole = new JComboBox<>(new String[]{"ADMIN", "SINHVIEN"});
        btnCreate = new JButton("Tạo tài khoản");

        form.add(new JLabel("Username"));
        form.add(txtUser);
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel(""));

        form.add(new JLabel("Password"));
        form.add(txtPass);
        form.add(new JLabel("Role"));
        form.add(cbRole);
        form.add(btnCreate);

        add(form, BorderLayout.SOUTH);
    }
}
