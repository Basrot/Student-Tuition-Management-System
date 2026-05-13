/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.panel;
import javax.swing.*;

/**
 *
 * @author Admin
 */
public class DONGHP_panel extends JFrame {
    public JTextField txtHP;
    public JComboBox<String> cbNH;
    public JButton btnDONG, btnThoat;
    
    public DONGHP_panel(){
        
        
        setTitle("Đóng học phí");
        setSize(400, 300);
        setLocationRelativeTo(null); // ra giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        JLabel lbHP = new JLabel("Học phí");
        lbHP.setBounds(50,50,100,30);
        add(lbHP);
        
        txtHP = new JTextField("");
        txtHP.setBounds(150, 50,100,30);
        add(txtHP);
        
        JLabel lbNH = new JLabel("Ngân hàng");
        lbNH.setBounds(50,110,100,30);
        add(lbNH);
        
        cbNH = new JComboBox<>(new String []{"AgrinkBank", "VietcomeBank", "VPBank"});
        cbNH.setBounds(150,110,100,30);
        add(cbNH);
        
        btnDONG = new JButton("Đóng học phí");
        btnDONG.setBounds(80,180,120,30);
        add(btnDONG);
        
        btnDONG.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Hoàn thành đóng học phí");
            dispose();
        });
        
        btnThoat = new JButton("Thoát");
        btnThoat.setBounds(220, 180, 80,30);
        add(btnThoat);
        
        btnThoat.addActionListener(e -> {
            dispose(); // đóng form
        });
        
      
  }
}
