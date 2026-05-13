/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.controller;
import com.mycompany.quanlyhocphi.view.ForgetPasswordView;
import com.mycompany.quanlyhocphi.util.DBUtil;
import com.mycompany.quanlyhocphi.util.HashUtil;
import com.mycompany.quanlyhocphi.util.MailUtil;


import javax.swing.*;
import java.sql.*;
import java.util.Random;
/**
 *
 * @author basrot
 */
public class ForgetPasswordController {
    private ForgetPasswordView view;
    private String currentOTP;
    private String currentUser;

    public ForgetPasswordController(ForgetPasswordView view) {
        this.view = view;
        init();
    }

    private void init() {

        view.btnSendOTP.addActionListener(e -> sendOTP());
        view.btnVerifyOTP.addActionListener(e -> verifyOTP());
        view.btnUpdatePass.addActionListener(e -> updatePassword());
        view.btnBack.addActionListener(e -> view.dispose());
    }

    private void sendOTP() {
    try {
        String user = view.txtUser.getText().trim();
        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nhập tài khoản");
            return;
        }

        Connection conn = DBUtil.getConnection();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT email FROM account WHERE username=?");
        ps.setString(1, user);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(view, "Tài khoản không tồn tại");
            return;
        }

        String email = rs.getString("email");
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        // LƯU OTP VÀO DB
        PreparedStatement saveOtp = conn.prepareStatement(
            "UPDATE account SET otp=?, otp_created=NOW() WHERE username=?");
        saveOtp.setString(1, otp);
        saveOtp.setString(2, user);
        saveOtp.executeUpdate();

        MailUtil.sendOTP(email, otp);
        currentUser = user;

        JOptionPane.showMessageDialog(view, "Đã gửi OTP về email");

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(view, ex.getMessage());
    }
}

    private void verifyOTP() {
    try {
        String otpInput = view.txtOTP.getText().trim();

        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT otp FROM account WHERE username=?");
        ps.setString(1, currentUser);
        ResultSet rs = ps.executeQuery();

        if (!rs.next() || rs.getString("otp") == null) {
            JOptionPane.showMessageDialog(view, "Chưa gửi OTP");
            return;
        }

        if (otpInput.equals(rs.getString("otp"))) {
            JOptionPane.showMessageDialog(view, "Xác minh OTP thành công");
            view.txtNewPass.setEnabled(true);
            view.btnUpdatePass.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(view, "OTP không đúng");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(view, ex.getMessage());
    }
}

    private void updatePassword() {
    try {
        if (!view.txtNewPass.isEnabled()) {
            JOptionPane.showMessageDialog(view, "Chưa xác minh OTP");
            return;
        }

        String newPass = new String(view.txtNewPass.getPassword()).trim();
        if (newPass.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mật khẩu không được để trống");
            return;
        }

        String hashed = HashUtil.sha256(newPass);

        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE account SET password=?, otp=NULL WHERE username=?");

        ps.setString(1, hashed);
        ps.setString(2, currentUser);
        ps.executeUpdate();

        JOptionPane.showMessageDialog(view, "Đổi mật khẩu thành công");
        view.dispose();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(view, ex.getMessage());
    }
}
}
