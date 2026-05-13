package com.mycompany.quanlyhocphi.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NganhControllerTest {

    private NganhController controller;

    @Before
    public void setUp() {
        controller = new NganhController(null);
    }

    // Giá trị đại diện
    private static final String MA_T = "CNPM";
    private static final String TEN_T = "Cong nghe phan mem";
    private static final String KHOA_T = "CNTT";
    private static final String VT_T = "PM";
    private static final String VT_S = "P1";

    // 1. N, N, N, N
    @Test
    public void testTH01() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", "", "", ""));
    }

    // 2. N, N, N, S
    @Test
    public void testTH02() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", "", VT_S, ""));
    }

    // 3. N, N, N, T
    @Test
    public void testTH03() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", "", VT_T, ""));
    }

    // 4. N, N, T, N
    @Test
    public void testTH04() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", "", "", KHOA_T));
    }

    // 5. N, N, T, S
    @Test
    public void testTH05() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", "", VT_S, KHOA_T));
    }

    // 6. N, N, T, T
    @Test
    public void testTH06() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", "", VT_T, KHOA_T));
    }

    // 7. N, T, N, N
    @Test
    public void testTH07() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", TEN_T, "", ""));
    }

    // 8. N, T, N, S
    @Test
    public void testTH08() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", TEN_T, VT_S, ""));
    }

    // 9. N, T, N, T
    @Test
    public void testTH09() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", TEN_T, VT_T, ""));
    }

    // 10. N, T, T, N
    @Test
    public void testTH10() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", TEN_T, "", KHOA_T));
    }

    // 11. N, T, T, S
    @Test
    public void testTH11() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", TEN_T, VT_S, KHOA_T));
    }

    // 12. N, T, T, T
    @Test
    public void testTH12() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh("", TEN_T, VT_T, KHOA_T));
    }

    // 13. T, N, N, N
    @Test
    public void testTH13() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, "", "", ""));
    }

    // 14. T, N, N, S
    @Test
    public void testTH14() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, "", VT_S, ""));
    }

    // 15. T, N, N, T
    @Test
    public void testTH15() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, "", VT_T, ""));
    }

    // 16. T, N, T, N
    @Test
    public void testTH16() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, "", "", KHOA_T));
    }

    // 17. T, N, T, S
    @Test
    public void testTH17() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, "", VT_S, KHOA_T));
    }

    // 18. T, N, T, T
    @Test
    public void testTH18() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, "", VT_T, KHOA_T));
    }

    // 19. T, T, N, N
    @Test
    public void testTH19() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, TEN_T, "", ""));
    }

    // 20. T, T, N, S
    @Test
    public void testTH20() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, TEN_T, VT_S, ""));
    }

    // 21. T, T, N, T
    @Test
    public void testTH21() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, TEN_T, VT_T, ""));
    }

    // 22. T, T, T, N
    @Test
    public void testTH22() {
        assertEquals("Các ô không được để trống!",
                controller.validateNganh(MA_T, TEN_T, "", KHOA_T));
    }

    // 23. T, T, T, S
    @Test
    public void testTH23() {
        assertEquals("Viết tắt phải đúng 2 chữ cái (A-Z)!",
                controller.validateNganh(MA_T, TEN_T, VT_S, KHOA_T));
    }

    // 24. T, T, T, T
    @Test
    public void testTH24() {
        assertEquals("Hợp lệ",
                controller.validateNganh(MA_T, TEN_T, VT_T, KHOA_T));
    }
}