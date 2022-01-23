package ru.netology.mode;


import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;


import java.sql.DriverManager;

import java.util.Locale;


public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getInvalidAuthInfo() {
        Faker faker = new Faker(new Locale("en"));
        return new AuthInfo("petya", faker.internet().password());
    }


    public static class VerificationCode {


        @SneakyThrows
        public static String getAuthCode() {
            var userSQL = "SELECT id, login, password FROM users WHERE login = ?;";
            var codeSQL = "SELECT code FROM auth_codes JOIN users ON auth_codes.user_id = users.id and login = ?;";
            String authCode = null;

            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/app-db", "app", "mypass"
                    );
                    var userStmt = conn.prepareStatement(userSQL);
                    var codeStmt = conn.prepareStatement(codeSQL);
            ) {
                userStmt.setString(1, "vasya");
                codeStmt.setString(1, "vasya");

                try (var rs = userStmt.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getString("id");
                        var login = rs.getString("login");
                        var password = rs.getString("password");
                        System.out.println(id + " | " + login + " | " + password);


                        try (var code = codeStmt.executeQuery()) {
                            while (code.next()) {
                                var verificationCode = code.getString("code");
                                authCode = verificationCode;
                                System.out.println(verificationCode);


                            }

                        }

                    }

                }


            }
            return authCode;
        }

    }
}
