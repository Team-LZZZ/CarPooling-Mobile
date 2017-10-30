package edu.wpi.cs528.lzzz.carpooling_mobile.database;

/**
 * Created by QQZhao on 10/28/17.
 */

public class Tables {
    public static final class UserTable{
        public static final String NAME = "users";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
        }
    }
}
