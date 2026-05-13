package com.mycompany.quanlyhocphi.panel;

import com.mycompany.quanlyhocphi.controller.ThongKeController;
import com.mycompany.quanlyhocphi.model.ThongKeModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TraCuuCongNoPanel extends JPanel {

    private JTextField txtKeyword;
    private JTable tblSV;
    private JLabel lblTongHP, lblDaThu, lblConNo;

    public TraCuuCongNoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        add(createSearchPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSummaryPanel(), BorderLayout.SOUTH);

        loadData("");
    }

    /* ================= SEARCH ================= */
    private JPanel createSearchPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBorder(BorderFactory.createTitledBorder("🔍 Tra cứu công nợ"));

        txtKeyword = new JTextField();
        JButton btnSearch = new JButton("Tìm kiếm");

        btnSearch.addActionListener(e ->
                loadData(txtKeyword.getText().trim())
        );

        p.add(txtKeyword, BorderLayout.CENTER);
        p.add(btnSearch, BorderLayout.EAST);
        return p;
    }

    /* ================= TABLE ================= */
    private JScrollPane createTablePanel() {
        tblSV = new JTable();
        tblSV.setRowHeight(28);
        tblSV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblSV.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblSV.getSelectedRow() != -1) {
                String maSV = tblSV.getValueAt(tblSV.getSelectedRow(), 0).toString();
                new ChiTietCongNoDialog(maSV).setVisible(true);
            }
        });

        return new JScrollPane(tblSV);
    }

    /* ================= SUMMARY ================= */
    private JPanel createSummaryPanel() {
        JPanel p = new JPanel(new GridLayout(1, 3, 15, 0));
        p.setBorder(BorderFactory.createTitledBorder("📌 Tổng hợp"));

        lblTongHP = new JLabel();
        lblDaThu = new JLabel();
        lblConNo = new JLabel();

        styleLabel(lblTongHP);
        styleLabel(lblDaThu);
        styleLabel(lblConNo);

        p.add(lblTongHP);
        p.add(lblDaThu);
        p.add(lblConNo);

        return p;
    }

    private void styleLabel(JLabel lb) {
        lb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lb.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /* ================= LOAD DATA ================= */
    private void loadData(String keyword) {
        String dk = keyword.isEmpty()
                ? ""
                : "WHERE sv.MaSV LIKE '%" + keyword + "%' OR sv.HoTen LIKE '%" + keyword + "%'";

        List<ThongKeModel> list = ThongKeController.getDanhSach(dk);

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Mã SV", "Họ tên", "Lớp", "Khoa", "Tổng HP", "Đã thu", "Còn nợ"}, 0
        );

        double tong = 0, daThu = 0, conNo = 0;

        for (ThongKeModel t : list) {
            tong += t.tongHocPhi;
            daThu += t.daThu;
            conNo += t.conNo;

            m.addRow(new Object[]{
                    t.maSV, t.hoTen, t.lop, t.khoa,
                    vnd(t.tongHocPhi), vnd(t.daThu), vnd(t.conNo)
            });
        }

        tblSV.setModel(m);

        lblTongHP.setText("Tổng HP: " + vnd(tong));
        lblDaThu.setText("Đã thu: " + vnd(daThu));
        lblConNo.setText("Còn nợ: " + vnd(conNo));
    }

    private String vnd(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(v);
    }
}
