package com.example.studentsync.utils;

import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseFunctions {

    private static final String url = "jdbc:postgresql://localhost:5432/studentsync";
    private static final String user = "postgres";
    private static final String password = "03062003";

    public static String usernamePublic = "";
    public static String passwordPublic = "";

    public DatabaseFunctions() {
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean checkIfPhoneNumberExist(String phoneNumber) throws SQLException {
        Connection connection = connect();
        boolean var5;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE phone_number =?");
            try {
                statement.setString(1, phoneNumber);
                ResultSet resultSet = statement.executeQuery();
                var5 = resultSet.next();
                if(var5) System.out.println("Phone number already exists: " + phoneNumber);
            } catch (Throwable var8) {
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
        } catch (Throwable var9) {
            if (connection!= null)
                try {
                    connection.close();
                } catch (Throwable var6) {
                    var9.addSuppressed(var6);
                }
            throw var9;
        }
        return var5;
    }

    public static boolean checkIfEmailExists(String email) throws SQLException {
        Connection connection = connect();
        boolean var5;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE email =?");
            try {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                var5 = resultSet.next();
                if(var5) System.out.println("Email already exists: " + email);
            } catch (Throwable var8) {
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
        } catch (Throwable var9) {
            if (connection!= null)
                try {
                    connection.close();
                } catch (Throwable var6) {
                    var9.addSuppressed(var6);
                }
            throw var9;
        }
        return var5;
    }

    public static boolean checkIfUsernameExists(String username) throws SQLException {
        Connection connection = connect();
        boolean var5;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username =?");
            try {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                var5 = resultSet.next();
                if(var5) System.out.println("Username already exists: " + username);
            } catch (Throwable var8) {
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
        } catch (Throwable var9) {
            if (connection!= null)
                try {
                    connection.close();
                } catch (Throwable var6) {
                    var9.addSuppressed(var6);
                }
            throw var9;
        }
        return var5;
    }

    public static String checkIfAccountIsValid(String username, String email, String phoneNumber) throws SQLException {
        if(checkIfUsernameExists(username)) return "Username already exists";
        if(checkIfEmailExists(email)) return "Email already exists";
        if(checkIfPhoneNumberExist(phoneNumber)) return "Phone number already exists";
        return "OK";
    }

    public static boolean addUser(TextField firstName, TextField lastName, TextField username, TextField email, TextField phoneNumber, TextField address, TextField password, Date birthday, boolean student) {
        try {
            Connection connection = connect();
            boolean var5;
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (username, first_name, last_name, email, phone_number, address, birthday, password, student_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                try {
                    statement.setString(1, username.getText());
                    statement.setString(2, firstName.getText());
                    statement.setString(3, lastName.getText());
                    statement.setString(4, email.getText());
                    statement.setString(5, phoneNumber.getText());
                    statement.setString(6, address.getText());
                    statement.setDate(7, birthday);
                    statement.setString(8, password.getText());
                    statement.setBoolean(9, student);
                    int rowsAffected = statement.executeUpdate();
                    var5 = rowsAffected > 0;
                    System.out.println("rowsAffected = " + rowsAffected);
                } catch (Throwable var8) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }
                    throw var8;
                }
                statement.close();
            } catch (Throwable var9) {
                if (connection != null)
                    try {
                        connection.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                throw var9;
            }
            connection.close();
            return var5;
        } catch (SQLException var10) {
            System.out.println("Error adding new account: " + var10.getMessage());
            return false;
        }
    }

    public static boolean enterAccount(String username, String password) throws SQLException {
        Connection connection = connect();
        boolean var5;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username =? AND password =?");
            try {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                var5 = resultSet.next();
                System.out.println("var5 = " + var5);
            } catch (Throwable var8) {
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
            if(var5){
                usernamePublic = username;
                passwordPublic = password;
            }
            return var5;
        } catch (SQLException var8) {
            System.out.println("Incorrect account");
            return false;
        }
    }

    public static boolean isAccountTeacher(String username, String password) throws SQLException {
        Connection connection = connect();
        boolean var5;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username =? AND password =? AND student_status = false");
            try {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                var5 = resultSet.next();
                if(var5) System.out.println("User is a teacher");
            } catch (Throwable var8) {
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
            return var5;
        } catch (SQLException var8) {
            System.out.println("Incorrect account");
            return false;
        }
    }

    public static boolean doesTeacherHaveClasses(String username, String password) throws SQLException {
        Connection connection = connect();
        boolean var5;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username =? AND password =? AND student_status = false");
            try {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next());{
                    int teacherId = resultSet.getInt("id");
                    String classCheckSql = "SELECT * FROM classes WHERE teacher_id =?";
                    PreparedStatement classCheckStatement = connection.prepareStatement(classCheckSql);
                    try{
                        classCheckStatement.setInt(1, teacherId);
                        ResultSet classCheckResultSet = classCheckStatement.executeQuery();
                        var5 = classCheckResultSet.next();
                        if(var5) System.out.println("User has classes");
                    } finally {
                        classCheckStatement.close();
                    }
                }
            } catch (Throwable var8) {
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
            return var5;
        } catch (SQLException var8) {
            System.out.println("Incorrect account");
            return false;
        }
    }

    public static boolean doesClassroomNameExists(String className) throws SQLException {
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("Select * from classes where class_name = ?");
            try{
                statement.setString(1, className);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    return true;
                }
            } finally{
                statement.close();
            }
        } finally {
            conn.close();
        }
        return false;
    }

    public static Integer getClassID(String className) throws SQLException {
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("Select class_id from classes where class_name = ?");
            try{
                statement.setString(1,className);
                ResultSet rs = statement.executeQuery();
                if(rs.next())
                    return rs.getInt("class_id");
            } finally {
                statement.close();
            }
        } finally{
            conn.close();
        }
        return null;
    }

    public static boolean doesClassroomExists(String className, String password) throws SQLException {
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("Select * from classes where class_name = ? and class_password = ?");
            try{
                statement.setString(1, className);
                statement.setString(2,password);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    return true;
                }
            } finally{
                statement.close();
            }
        } finally {
            conn.close();
        }
        return false;
    }

    public static void joinClassroom(String className) throws SQLException{
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("insert into class_enrollments(student_id, class_id) values (?,?)");
            try{
                statement.setInt(1, getAccountID());
                statement.setInt(2, getClassID(className));
                statement.executeUpdate();
                System.out.println("Joined classroom");
            } finally{
                statement.close();
            }
        } finally{
            conn.close();
        }
    }

    public static boolean createClassroom(String className, String password, LocalDate startDate, LocalDate endDate) throws SQLException {
        Connection connection = connect();
        boolean var5 = false;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username =? AND password =? AND student_status = false");
            try {
                statement.setString(1, usernamePublic);
                statement.setString(2, passwordPublic);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int teacherId = resultSet.getInt("id");
                    String createClassroomSql = "INSERT INTO classes (class_name, teacher_id, class_password,start_date,end_date) VALUES (?,?,?,?,?)";
                    PreparedStatement createClassroomStatement = connection.prepareStatement(createClassroomSql);
                    try {
                        createClassroomStatement.setString(1, className);
                        createClassroomStatement.setInt(2, teacherId);
                        createClassroomStatement.setString(3, password);
                        createClassroomStatement.setDate(4, java.sql.Date.valueOf(startDate));
                        createClassroomStatement.setDate(5, java.sql.Date.valueOf(endDate));
                        int rowsAffected = createClassroomStatement.executeUpdate();
                        if (rowsAffected > 0) var5 = true;
                        System.out.println("rowsAffected = " + rowsAffected);
                    } finally {
                        createClassroomStatement.close();
                    }
                }
            } catch (Throwable var8) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
            return var5;
        } catch (SQLException var8) {
            System.out.println("Error in creating a classroom");
            return false;
        }
    }

    public static List<Integer> getTeacherClasses() throws SQLException {
        Connection connection = connect();
        List<Integer> varList = new ArrayList<>();
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT class_id FROM classes c JOIN accounts a ON c.teacher_id = a.id WHERE a.username =? AND a.password =?");
            try{
                statement.setString(1, usernamePublic);
                statement.setString(2, passwordPublic);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    varList.add(resultSet.getInt("class_id"));
                }
            } catch(Throwable var8){
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
            return varList;
        } catch(SQLException var8){
            System.out.println("Error getting classes");
            return varList;
        }
    }

    public static List<Integer> getStudentClasses() throws SQLException {
        Connection conn = connect();
        List<Integer> resultList = new ArrayList<>();
        try{
            PreparedStatement statement = conn.prepareStatement("Select class_id from class_enrollments c join accounts a on c.student_id = a.id where a.username = ? and a.password = ?");
            try{
                statement.setString(1, usernamePublic);
                statement.setString(2, passwordPublic);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    resultList.add(rs.getInt("class_id"));
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
        return resultList;
    }

    public static String getClassName(Integer class_id) throws SQLException{
        Connection connection = connect();
        String var5 = "";
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT class_name FROM classes WHERE class_id =?");
            try{
                statement.setInt(1, class_id);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    var5 = resultSet.getString("class_name");
                }
            } catch(Throwable var8){
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
            return var5;
        } catch(SQLException var8){
            System.out.println("Error getting classes");
            return var5;
        }
    }

    public static Integer getAccountID() throws SQLException{
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("Select id from accounts where username = ? and password = ?");
            try{
                statement.setString(1,usernamePublic);
                statement.setString(2,passwordPublic);
                ResultSet rs = statement.executeQuery();
                if(rs.next())
                    return rs.getInt("id");
            } finally {
                statement.close();
            }
        } finally{
            conn.close();
        }
        return null;
    }

    public static String getStudentName(Integer studentId) throws SQLException {
        Connection conn = connect();
        String name = null;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT first_name, last_name FROM accounts WHERE id = ?");
            try {
                statement.setInt(1, studentId);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    name = lastName + " " + firstName;
                }
            } catch (Throwable var8) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException var8) {
            System.out.println("Error getting name");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return name;
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // Value not found
    }

    public static int calculateDaysBetweenDates(Date date1, Date date2) {
        long time_difference = date2.getTime() - date1.getTime();
        return (int) (time_difference / (1000 * 60 * 60 * 24)) + 1;
    }

    public static int getNumberofDays(Integer class_id) throws SQLException {
        Connection conn = connect();
        Date startDate = null;
        Date endDate = null;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT start_date, end_date FROM classes where class_id = ?");
            try {
                statement.setInt(1, class_id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    startDate = resultSet.getDate("start_date");
                    endDate = resultSet.getDate("end_date");
                } else {
                    startDate = Date.valueOf(LocalDate.of(2022, 1, 1));
                    endDate = Date.valueOf(LocalDate.of(2022, 1, 1));
                }
            } catch (Throwable var8) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            statement.close();
            return calculateDaysBetweenDates(startDate, endDate);
        } catch (SQLException var8) {
            System.out.println("Error getting classes");
            return 0;
        }
    }

    public static Date getStartDate(Integer class_id) throws SQLException {
        Connection conn = connect();
        Date startDate = new Date(2022,1,1);
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT start_date FROM classes WHERE class_id = ?");
            try{
                statement.setInt(1, class_id);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()) {
                    startDate = resultSet.getDate("start_date");
                }
            } catch (Throwable e){
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        e.addSuppressed(var7);
                    }
                }
                throw e;
            }
            statement.close();
            return startDate;
        } catch (Exception e){
            System.out.println("Error getting classes");
            return null;
        }
    }

    public static Date getEndDate(Integer class_id) throws SQLException {
        Connection conn = connect();
        Date endDate = new Date(2022,1,1);
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT end_date FROM classes WHERE class_id = ?");
            try{
                statement.setInt(1, class_id);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()) {
                    endDate = resultSet.getDate("end_date");
                }
            } catch (Throwable e){
                if (statement!= null) {
                    try {
                        statement.close();
                    } catch (Throwable var7) {
                        e.addSuppressed(var7);
                    }
                }
                throw e;
            }
            statement.close();
            return endDate;
        } catch (Exception e){
            System.out.println("Error getting classes");
            return null;
        }
    }

    public static HashMap<Integer, List<Integer>> getAttendance(Integer class_id) throws SQLException{
        Connection conn = connect();
        HashMap<Integer, List<Integer>> attendanceMap = new HashMap<>();
        long days = getNumberofDays(class_id);
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT date, student_id FROM attendance WHERE class_id=? ORDER BY date ASC");
            try{
                statement.setInt(1, class_id);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    int dayNumber = calculateDaysBetweenDates(getStartDate(class_id),resultSet.getDate("date"));
                    int studentId = resultSet.getInt("student_id");
                    attendanceMap.computeIfAbsent(studentId,k -> new ArrayList<>()).add(dayNumber);
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
        return attendanceMap;
    }

    public static List<Integer> getStudentAttendance(Integer student_id, Integer class_id) throws SQLException {
        Connection conn = connect();
        List<Integer> attendance = new ArrayList<>();
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT date from attendance where student_id = ? and class_id = ?");
            try{
                statement.setInt(1, student_id);
                statement.setInt(2, class_id);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    attendance.add(calculateDaysBetweenDates(getStartDate(class_id),rs.getDate("date")));
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
        return attendance;
    }

    public static HashMap<Integer, String> getStudentList (Integer class_id) throws SQLException{
        Connection conn = connect();
        HashMap<Integer, String> studentList = new HashMap<>();
        String name = null;
        try{
            PreparedStatement statement = conn.prepareStatement("select id, first_name, last_name from accounts join class_enrollments on id = student_id where class_id = ? order by last_name");
            try{
                statement.setInt(1,class_id);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    Integer studentId = rs.getInt("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    name = lastName + " " + firstName;
                    studentList.put(studentId,name);
                }
            } finally{
                statement.close();
            }
        }
        finally{
            conn.close();
        }
        return studentList;
    }

    public static List<Integer> getStudentGrades(Integer student_id, Integer class_id) throws SQLException{
        Connection conn = connect();
        List<Integer> grades = new ArrayList<>();
        try{
            PreparedStatement statement = conn.prepareStatement("Select grade from grades where student_id = ? and class_id = ?");
            try{
                statement.setInt(1, student_id);
                statement.setInt(2, class_id);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    grades.add(rs.getInt("grade"));
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
        return grades;
    }

    public static void deleteGrade(Integer student_id, Integer class_id, Integer grade) throws SQLException{
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("DELETE FROM grades WHERE ctid = (SELECT ctid FROM grades WHERE student_id = ? AND class_id = ? AND grade = ? LIMIT 1)");
            try{
                statement.setInt(1,student_id);
                statement.setInt(2,class_id);
                statement.setInt(3,grade);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Deleted rows: " + rowsAffected);
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public static void addGrade(Integer student_id, Integer class_id, Integer grade) throws SQLException{
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("INSERT into grades (student_id, class_id, grade) values (?, ?, ?)");
            try{
                statement.setInt(1,student_id);
                statement.setInt(2,class_id);
                statement.setInt(3,grade);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Added rows: " + rowsAffected);
            } finally {
                statement.close();
            }
        }finally {
            conn.close();
        }
    }

    public static boolean doesAttendanceExist(Integer student_id, Integer class_id, Date date) throws SQLException{
        Connection conn = connect();
        boolean result = false;
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT * from attendance where student_id = ? and class_id = ? and date = ?");
            try{
                statement.setInt(1,student_id);
                statement.setInt(2,class_id);
                statement.setDate(3,date);
                ResultSet rs = statement.executeQuery();
                while(rs.next())
                    result = true;
            } finally {
                statement.close();
            }
        }finally {
            conn.close();
        }
        return result;
    }

    public static void addAttendance(Integer student_id, Integer class_id, Date date) throws SQLException{
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("INSERT into attendance (student_id, class_id, date) values (?, ?, ?)");
            try{
                statement.setInt(1,student_id);
                statement.setInt(2,class_id);
                statement.setDate(3,date);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Added rows: " + rowsAffected);
            } finally {
                statement.close();
            }
        }finally {
            conn.close();
        }
    }

    public static void sendMessage(Integer class_id, String message) throws SQLException{
        Connection conn = connect();
        try{
            PreparedStatement statement = conn.prepareStatement("insert into announcements (class_id, message, timestamp) values (?,?,NOW())");
            try{
                statement.setInt(1,class_id);
                statement.setString(2, message);
                statement.executeUpdate();
                System.out.println("Message sent!");
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public static HashMap<Timestamp, String> loadMessages(Integer class_id) throws SQLException{
        Connection conn = connect();
        Timestamp timestamp;
        String message;
        HashMap<Timestamp, String> messages = new HashMap<>();
        try{
            PreparedStatement statement = conn.prepareStatement("select message, timestamp from announcements where class_id = ?");
            try{
                statement.setInt(1,class_id);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    message = rs.getString("message");
                    timestamp = rs.getTimestamp("timestamp");
                    messages.put(timestamp, message);
                }
            } finally{
                statement.close();
            }
        } finally {
            conn.close();
        }
        return messages;
    }
}

