package com.mycompany.quanlyhocphi;

import com.mycompany.quanlyhocphi.view.LoginView;
import com.mycompany.quanlyhocphi.controller.LoginController;
/**
 *
 * @author basrot
 */
public class Main {
    public static void main(String[] args) {
        LoginView v = new LoginView();
        new LoginController(v);
        v.setVisible(true);
    }
}

