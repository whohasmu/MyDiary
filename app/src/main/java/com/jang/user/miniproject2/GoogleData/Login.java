package com.jang.user.miniproject2.GoogleData;

import com.google.firebase.auth.FirebaseUser;



public class Login {


    public static Login curr = null;
    public static Login getInstance() {
        if (curr == null) {
            curr = new Login();
        }

        return curr;
    }

    private FirebaseUser LoginUser;

    public Login() {
    }






    public static Login getCurr() {
        return curr;
    }

    public static void setCurr(Login curr) {
        Login.curr = curr;
    }

    public FirebaseUser getLoginUser() {
        return LoginUser;
    }

    public void setLoginUser(FirebaseUser loginUser) {
        LoginUser = loginUser;
    }
}
