package com.mycompany.quanlyhocphi.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginControllerTest {

    private LoginController controller;

    @Before
    public void setUp() {
        controller = new LoginController(null);
    }

    // TH1: T - T
    @Test
    public void testTH1_UsernameDung_PasswordDung() {
        String result = controller.validateInput("basrot", "123456");
        assertEquals("Hợp lệ", result);
    }

    // TH2: T - N
    @Test
    public void testTH2_UsernameDung_PasswordTrong() {
        String result = controller.validateInput("basrot", "");
        assertEquals("Vui lòng nhập đầy đủ thông tin đăng nhập", result);
    }

    // TH3: T - F
    @Test
    public void testTH3_UsernameDung_PasswordSai() {
        String result = controller.validateInput("basrot", "123");
        assertEquals("Username hoặc mật khẩu không hợp lệ", result);
    }

    // TH4: N - T
    @Test
    public void testTH4_UsernameTrong_PasswordDung() {
        String result = controller.validateInput("", "123456");
        assertEquals("Vui lòng nhập đầy đủ thông tin đăng nhập", result);
    }

    // TH5: N - N
    @Test
    public void testTH5_UsernameTrong_PasswordTrong() {
        String result = controller.validateInput("", "");
        assertEquals("Vui lòng nhập đầy đủ thông tin đăng nhập", result);
    }

    // TH6: N - F
    @Test
    public void testTH6_UsernameTrong_PasswordSai() {
        String result = controller.validateInput("", "123");
        assertEquals("Vui lòng nhập đầy đủ thông tin đăng nhập", result);
    }

    // TH7: F - T
    @Test
    public void testTH7_UsernameSai_PasswordDung() {
        String result = controller.validateInput("1admin", "123456");
        assertEquals("Username hoặc mật khẩu không hợp lệ", result);
    }

    // TH8: F - N
    @Test
    public void testTH8_UsernameSai_PasswordTrong() {
        String result = controller.validateInput("abc@", "");
        assertEquals("Vui lòng nhập đầy đủ thông tin đăng nhập", result);
    }

    // TH9: F - F
    @Test
    public void testTH9_UsernameSai_PasswordSai() {
        String result = controller.validateInput("abc@", "123");
        assertEquals("Username hoặc mật khẩu không hợp lệ", result);
    }
}