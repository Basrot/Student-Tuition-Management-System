package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.util.DBUtil;
import com.mycompany.quanlyhocphi.util.HashUtil;
import com.mycompany.quanlyhocphi.view.*;

import javax.swing.*;
import java.sql.*;

public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        if (view != null) {
            view.btnLogin.addActionListener(e -> login());
            view.btnForget.addActionListener(e -> openForget());
        }
    }

    public String validateInput(String user, String pass) {
        if (user == null) user = "";
        if (pass == null) pass = "";

        user = user.trim();
        pass = pass.trim();

        // N: thiếu thông tin
        if (user.isEmpty() || pass.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin đăng nhập";
        }

        // F: username không hợp lệ
        if (!user.matches("^[A-Za-z][A-Za-z0-9_]*$")) {
            return "Username hoặc mật khẩu không hợp lệ";
        }

        // F: password không hợp lệ
        if (pass.length() < 6 || pass.length() > 12) {
            return "Username hoặc mật khẩu không hợp lệ";
        }

        // T
        return "Hợp lệ";
    }

    public void login() {
        String user = view.txtUser.getText();
        String pass = new String(view.txtPass.getPassword());

        String validateResult = validateInput(user, pass);
        if (!validateResult.equals("Hợp lệ")) {
            JOptionPane.showMessageDialog(view, validateResult);
            return;
        }

        user = user.trim();
        pass = pass.trim();

        try (Connection c = DBUtil.getConnection()) {
            String sql = """
                SELECT username, password, role
                FROM account
                WHERE username=?
            """;

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(view, "Username hoặc mật khẩu không hợp lệ");
                return;
            }

            String stored = rs.getString("password");
            String role = rs.getString("role");

            boolean ok = false;

            // CASE 1: BCrypt
            if (stored != null && stored.startsWith("$2")) {
                ok = org.mindrot.jbcrypt.BCrypt.checkpw(pass, stored);
            }

            // CASE 2: SHA256
            if (!ok) {
                String sha = HashUtil.sha256(pass).toLowerCase();
                if (sha.equalsIgnoreCase(stored)) {
                    ok = true;
                }
            }

            // CASE 3: Plain text
            if (!ok) {
                if (pass.equals(stored)) {
                    ok = true;
                }
            }

            if (!ok) {
                JOptionPane.showMessageDialog(view, "Username hoặc mật khẩu không hợp lệ");
                return;
            }

            JOptionPane.showMessageDialog(view, "Đăng nhập thành công!");
            view.dispose();

            if (role.equalsIgnoreCase("ADMIN")) {
                new MainView(user).setVisible(true);
            } else if (role.equalsIgnoreCase("SINHVIEN")) {
                new StudentView(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Role không hợp lệ!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi đăng nhập");
        }
    }

    private void openForget() {
        ForgetPasswordView v = new ForgetPasswordView();
        new ForgetPasswordController(v);
        v.setVisible(true);
    }
}