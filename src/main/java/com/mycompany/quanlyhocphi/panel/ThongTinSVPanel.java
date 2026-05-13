/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.panel;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author basrot
 */
public class ThongTinSVPanel extends JPanel {
    public ThongTinSVPanel(String username) {
        setLayout(new BorderLayout());
        add(new JLabel("THÔNG TIN SINH VIÊN: " + username, JLabel.CENTER),
            BorderLayout.CENTER);
    }
}
