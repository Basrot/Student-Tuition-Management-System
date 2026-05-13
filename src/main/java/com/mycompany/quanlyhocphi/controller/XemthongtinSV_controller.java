/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.model.SinhVien;
import com.mycompany.quanlyhocphi.model.SinhVienModel;
import com.mycompany.quanlyhocphi.panel.CapnhatTTSV_panel;
import com.mycompany.quanlyhocphi.panel.XemthongtinSV_panel;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.sql.*;

/**
 *
 * @author Admin
 */
public class XemthongtinSV_controller {
    
    XemthongtinSV_panel view;
    SinhVienModel model;
    String username;

     public XemthongtinSV_controller(XemthongtinSV_panel view, String username) {
         this.view = view;
        this.username = username;
        model = new SinhVienModel();

        loadData();

        view.btnSua.addActionListener(e -> {
            new CapnhatTTSV_panel(username, view).setVisible(true);
        });
    }
       private void loadData() {
        try {
            ResultSet rs = model.getSinhVien(username);
            if (rs.next()) {
                view.txtMSV.setText(rs.getString("MaSV"));
                view.txtHT.setText(rs.getString("HoTen"));
                view.txtNS.setText(rs.getDate("NgaySinh").toString());
                view.txtLOP.setText(rs.getString("MaLop"));
                view.cbGT.setSelectedItem(rs.getString("GioiTinh"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
    

