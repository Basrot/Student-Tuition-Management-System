package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.panel.PhanQuyenPanel;
import com.mycompany.quanlyhocphi.util.DBUtil;
import com.mycompany.quanlyhocphi.util.HashUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class PhanQuyenController {

    private PhanQuyenPanel view;

    public PhanQuyenController(PhanQuyenPanel view) {
        this.view = view;
        loadAccount();

        view.btnCreate.addActionListener(e -> createAccount());
    }

    /* ===== LOAD ACCOUNT ===== */
    private void loadAccount() {
        DefaultTableModel model = view.model;
        model.setRowCount(0);

        try (Connection c = DBUtil.getConnection()) {
            String sql = "SELECT username, email, password, role FROM account";
            ResultSet rs = c.prepareStatement(sql).executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===== CREATE ACCOUNT ===== */
    private void createAccount() {
        String user = view.txtUser.getText().trim();
        String email = view.txtEmail.getText().trim();
        String pass = new String(view.txtPass.getPassword()).trim();
        String role = view.cbRole.getSelectedItem().toString();

        if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không được để trống!");
            return;
        }

        String hash = HashUtil.sha256(pass).toLowerCase();

        try (Connection c = DBUtil.getConnection()) {
            String sql = "INSERT INTO account(username,email,password,role) VALUES (?,?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, email);
            ps.setString(3, hash);
            ps.setString(4, role);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(view, "Tạo tài khoản thành công!");
            loadAccount();

            // reset form
            view.txtUser.setText("");
            view.txtEmail.setText("");
            view.txtPass.setText("");
            view.cbRole.setSelectedIndex(0);

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(view, "Username đã tồn tại!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
