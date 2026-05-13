package com.mycompany.quanlyhocphi.view;

import com.mycompany.quanlyhocphi.controller.Hocphi_controller;
import com.mycompany.quanlyhocphi.controller.SVDKTC_controller;
import com.mycompany.quanlyhocphi.controller.XemthongtinSV_controller;

import com.mycompany.quanlyhocphi.panel.Hocphi_panel;
import com.mycompany.quanlyhocphi.panel.SVDKTC_panel;
import com.mycompany.quanlyhocphi.panel.ThongTinSVPanel;
import com.mycompany.quanlyhocphi.panel.XemthongtinSV_panel;
import javax.swing.*;
import java.awt.*;


public class StudentView extends JFrame {

    private JPanel content;

    public StudentView(String username) {

        setTitle("SINH VIÊN - QUẢN LÝ HỌC PHÍ UTT");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color blue = new Color(153, 217, 234);

        /* ===== HEADER ===== */
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(blue);

        JLabel title = new JLabel(
            "TRƯỜNG ĐẠI HỌC CÔNG NGHỆ GIAO THÔNG VẬN TẢI",
            JLabel.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 20));

        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        /* ===== MENU ===== */
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBackground(blue);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        
        menu.add(createButton("XEM THÔNG TIN", () -> {
        XemthongtinSV_panel p = new XemthongtinSV_panel();
        new XemthongtinSV_controller(p, username);
        showPanel(p);
    }));

        menu.add(createButton("ĐĂNG KÝ TÍN CHỈ", () ->{
            SVDKTC_panel p = new SVDKTC_panel();
            new SVDKTC_controller(p, username);
            showPanel(p);
         }));

        menu.add(createButton("HỌC PHÍ", () ->{
            Hocphi_panel p = new Hocphi_panel();
            new Hocphi_controller(p);
            showPanel(p);
         }));

        menu.add(Box.createVerticalGlue());

        JButton btnLogout = createButton("ĐĂNG XUẤT", () -> {
            dispose();
            new LoginView().setVisible(true);
        });

        menu.add(btnLogout);
        add(menu, BorderLayout.WEST);

        /* ===== CONTENT ===== */
        content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        add(content, BorderLayout.CENTER);

        showPanel(new ThongTinSVPanel(username));
    }

    private void showPanel(JPanel panel) {
        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 35));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> action.run());
        return btn;
    }
}
