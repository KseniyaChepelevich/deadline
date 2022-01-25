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

    public static class DeleteInfo {
        @SneakyThrows
        public static void deletingData() {
            var deleteFromAuthCodes = "DELETE FROM auth_codes;";
            var deleteFromCards = "DELETE FROM cards;";
            var deleteFromUsers = "DELETE FROM users;";

            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/app-db", "app", "mypass"
                    );
                    var deleteStmt = conn.createStatement();
            ) {

                var authCodes = deleteStmt.executeUpdate(deleteFromAuthCodes);
                var cards = deleteStmt.executeUpdate(deleteFromCards);
                var users = deleteStmt.executeUpdate(deleteFromUsers);
                System.out.println("delete from auth_codes" + authCodes + "\n" + "delete from cards" + cards + "\n" + "delete from users" + users);
            }
        }
    }

    public static class VerificationCode {

        @SneakyThrows
        public static String getAuthCode(DataHelper.AuthInfo info) {
            var codeSQL = "SELECT code FROM auth_codes JOIN users ON auth_codes.user_id = users.id and login = ?;";
            String authCode = null;
            String login = info.getLogin();


            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/app-db", "app", "mypass"
                    );
                    var codeStmt = conn.prepareStatement(codeSQL);
            ) {
                codeStmt.setString(1, login);

                try (var code = codeStmt.executeQuery()) {
                    while (code.next()) {
                        var verificationCode = code.getString("code");
                        authCode = verificationCode;
                        System.out.println(verificationCode);
                    }
                }
            }
            return authCode;
        }
    }


}
