package com.mycompany.quanlyhocphi.controller;

import com.mycompany.quanlyhocphi.model.ThongKeModel;
import com.mycompany.quanlyhocphi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThongKeController {

    // ================== GIÁ TRỊ ĐƠN (DÙNG CHO TỔNG QUAN + BIỂU ĐỒ) ==================
    public static double getGiaTri(String sql) {
        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================== DANH SÁCH SINH VIÊN (BẢNG CHÍNH) ==================
    public static List<ThongKeModel> getDanhSach(String dieuKien) {
        List<ThongKeModel> list = new ArrayList<>();

        String sql = """
            SELECT sv.MaSV, sv.HoTen, sv.MaLop, k.TenKhoa,
                   cn.TongHocPhi, cn.DaThu, cn.ConNo
            FROM congno cn
            JOIN sinhvien sv ON cn.MaSV = sv.MaSV
            LEFT JOIN khoa k ON sv.MaKhoa = k.MaKhoa
        """ + " " + dieuKien;

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ThongKeModel(
                        rs.getString("MaSV"),
                        rs.getString("HoTen"),
                        rs.getString("MaLop"),
                        rs.getString("TenKhoa"),
                        rs.getDouble("TongHocPhi"),
                        rs.getDouble("DaThu"),
                        rs.getDouble("ConNo")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================== CHI TIẾT CÔNG NỢ THEO SINH VIÊN ==================
    // DÙNG CHO ChiTietCongNoDialog
    public static List<Object[]> chiTietDangKy(String maSV) {
        List<Object[]> list = new ArrayList<>();

        String sql = """
            SELECT hk.TenHocKi,
                   cn.TongHocPhi,
                   cn.DaThu,
                   cn.ConNo,
                   cn.HanDong
            FROM congno cn
            JOIN hocki hk ON cn.MaHocKi = hk.MaHocKi
            WHERE cn.MaSV = ?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString(1),
                        rs.getDouble(2),
                        rs.getDouble(3),
                        rs.getDouble(4),
                        rs.getDate(5)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================== THỐNG KÊ THEO KHOA ==================
    public static List<Object[]> getTheoKhoa() {
        List<Object[]> list = new ArrayList<>();

        String sql = """
            SELECT k.TenKhoa,
                   COUNT(DISTINCT sv.MaSV),
                   IFNULL(SUM(cn.ConNo), 0)
            FROM sinhvien sv
            JOIN khoa k ON sv.MaKhoa = k.MaKhoa
            LEFT JOIN congno cn ON sv.MaSV = cn.MaSV
            GROUP BY k.TenKhoa
        """;

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getDouble(3)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================== THỐNG KÊ THEO NGÀNH ==================
    public static List<Object[]> getTheoNganh() {
        List<Object[]> list = new ArrayList<>();

        String sql = """
            SELECT n.TenNganh,
                   COUNT(DISTINCT sv.MaSV)
            FROM sinhvien sv
            JOIN nganh n ON sv.MaNganh = n.MaNganh
            GROUP BY n.TenNganh
        """;

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString(1),
                        rs.getInt(2)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}