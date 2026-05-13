/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.quanlyhocphi.panel;
import com.mycompany.quanlyhocphi.model.SinhVien;
import com.mycompany.quanlyhocphi.model.SinhVienModel;
import java.sql.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class CapnhatTTSV_panel extends JFrame {
    XemthongtinSV_panel parentView;
    String username;
    SinhVienModel sv;

    public JButton btnCapnhat, btnThoat;
    public JTextField txtMSV, txtHT, txtNS, txtLOP, txtEMAIL;
    public JComboBox<String> cbGT;
    public JComboBox<String> cbKHOA;
    public ButtonGroup groupSex;
    public JTable table;
    public DefaultTableModel model;

    public CapnhatTTSV_panel(String username, XemthongtinSV_panel parentView) {
    this.username = username;
    this.parentView = parentView;
    sv = new SinhVienModel();
    
    
    
    setTitle("Cập nhật thông tin");
        setSize(1080, 720);
        setLocationRelativeTo(null); // ra giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        JLabel lbMSV = new JLabel("Mã sinh viên");
        lbMSV.setBounds(220, 30, 100, 25);
         add(lbMSV);

        txtMSV = new JTextField();              
        txtMSV.setBounds(350, 30, 200, 25);
        add(txtMSV);
        
        JLabel lbTSV = new JLabel("Tên sinh viên");
        lbTSV.setBounds(220, 70, 100, 25);
         add(lbTSV);

        txtHT = new JTextField();              
        txtHT.setBounds(350, 70, 200, 25);
        add(txtHT);
        
        JLabel lbNS = new JLabel("Ngày sinh");
        lbNS.setBounds(220, 110, 200, 25);
         add(lbNS);

        txtNS = new JTextField();              
        txtNS.setBounds(350, 110, 200, 25);
        add(txtNS);
        
        JLabel lbGT = new JLabel("Giới tính ");
        lbGT.setBounds(220, 160, 100, 25);
         add(lbGT);

        cbGT = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGT.setBounds(350, 160, 120, 25);
        add(cbGT);
        
        JLabel lbLOP = new JLabel("Lớp");
        lbLOP.setBounds(220, 210, 200, 25);
         add(lbLOP);

        txtLOP = new JTextField();              
        txtLOP.setBounds(350, 210, 200, 25);
        add(txtLOP);
        
        JLabel lbKHOA = new JLabel("Khoa ");
        lbKHOA.setBounds(220, 260, 100, 30);
        add(lbKHOA);
        
        cbKHOA = new JComboBox<>(new String[]{"CNTT", "HTT", "KT", "CT", "ATBM"});
        cbKHOA.setBounds(350,260,200,30);
        add(cbKHOA);
        
        
        
        
        
         btnCapnhat = new JButton("Cập nhật");
        btnCapnhat.setBounds(250, 450, 100, 30);
        add(btnCapnhat);
        
        btnThoat = new JButton("Thoát");
        btnThoat.setBounds(550, 450, 110, 30);
        add(btnThoat);
        
        btnThoat.addActionListener(e -> {
            dispose(); // đóng form
        });
        txtMSV.setText(parentView.txtMSV.getText());
        txtMSV.setEditable(false);
        txtHT.setText(parentView.txtHT.getText());
        txtNS.setText(parentView.txtNS.getText());
        txtLOP.setText(parentView.txtLOP.getText());
            
            cbGT.setSelectedItem(parentView.cbGT.getSelectedItem());


            // ❌ KHÔNG CHO SỬA KHOA
            cbKHOA.setSelectedItem(parentView.cbKHOA.getSelectedItem());
            cbKHOA.setEnabled(false);

        btnCapnhat.addActionListener(e -> capNhatThongTin());

        
    }
    private void capNhatThongTin() {
    try {
        // 1️⃣ LẤY DỮ LIỆU TỪ FORM
        String gioiTinh = cbGT.getSelectedItem().toString();

        sv.updateSinhVien(
    username,
    txtHT.getText(),
    Date.valueOf(txtNS.getText()),
    gioiTinh,
    txtLOP.getText()
);


        // 3️⃣ CẬP NHẬT NGƯỢC LẠI FORM XEM THÔNG TIN
        parentView.txtHT.setText(txtHT.getText());
        parentView.txtNS.setText(txtNS.getText());
        parentView.txtLOP.setText(txtLOP.getText());
        parentView.cbGT.setSelectedItem(cbGT.getSelectedItem());
        


        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        dispose(); // đóng form

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        ex.printStackTrace();
    }
}
}
    
