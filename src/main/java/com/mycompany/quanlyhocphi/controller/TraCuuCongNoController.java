/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.controller;
import com.mycompany.quanlyhocphi.util.DBUtil;
import java.sql.*;
import java.util.*;
/**
 *
 * @author basrot
 */
public class TraCuuCongNoController {
    public static Map<String, Object> traCuu(String maSV) {
        Map<String, Object> data = new HashMap<>();

        String sql = """
            SELECT sv.MaSV, sv.HoTen, sv.MaLop, k.TenKhoa, n.TenNganh,
                   SUM(cn.TongHocPhi) TongHP,
                   SUM(cn.DaThu) DaThu,
                   SUM(cn.ConNo) ConNo
            FROM sinhvien sv
            JOIN congno cn ON sv.MaSV = cn.MaSV
            JOIN khoa k ON sv.MaKhoa = k.MaKhoa
            JOIN nganh n ON sv.MaNganh = n.MaNganh
            WHERE sv.MaSV = ?
            GROUP BY sv.MaSV
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                data.put("maSV", rs.getString("MaSV"));
                data.put("hoTen", rs.getString("HoTen"));
                data.put("lop", rs.getString("MaLop"));
                data.put("khoa", rs.getString("TenKhoa"));
                data.put("nganh", rs.getString("TenNganh"));
                data.put("tongHP", rs.getDouble("TongHP"));
                data.put("daThu", rs.getDouble("DaThu"));
                data.put("conNo", rs.getDouble("ConNo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
