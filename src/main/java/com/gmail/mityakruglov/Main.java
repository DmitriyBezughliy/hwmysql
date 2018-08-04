package com.gmail.mityakruglov;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/dbapartament?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "root";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                // create connection
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add apartament");
                    System.out.println("2: delete apartament");
                    System.out.println("3: view all apartaments");
                    System.out.println("4: view apartaments by parameters");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addApartament(sc);
                            break;
                        case "2":
                            deleteApartament(sc);
                            break;
                        case "3":
                            viewApartament();
                            break;
                        case "4":
                            viewForParameter(sc);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Apartament");
            st.execute("CREATE TABLE Apartament (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, district VARCHAR(20) NOT NULL,adrress VARCHAR(20) NOT NULL, m2 DOUBLE, numberOfRooms INT,price DOUBLE)");
        } finally {
            st.close();
        }
    }

    private static void addApartament(Scanner sc) throws SQLException {
        System.out.print("Enter district: ");
        String district = sc.nextLine();
        System.out.print("Enter adrress: ");
        String adrress = sc.nextLine();
        System.out.print("Enter M2: ");
        String sM2 = sc.nextLine();
        double m2 = Double.parseDouble(sM2);
        System.out.print("Enter  number of rooms: ");
        String sNumberOfRooms = sc.nextLine();
        int numberOfRooms = Integer.parseInt(sNumberOfRooms);
        System.out.print("Enter price: ");
        String sPrice = sc.nextLine();
        double price = Double.parseDouble(sPrice);

        PreparedStatement ps = conn.prepareStatement("INSERT INTO Apartament (district, adrress, m2, numberOfRooms,price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, adrress);
            ps.setDouble(3, m2);
            ps.setInt(4, numberOfRooms);
            ps.setDouble(5, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void deleteApartament(Scanner sc) throws SQLException {
        System.out.print("Enter address: ");
        String adrress = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM Apartament WHERE adrress = ?");
        try {
            ps.setString(1, adrress);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }




    private static void viewApartament() throws SQLException {

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Apartament");
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print(md.getColumnName(i) + "\t\t");
                }
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }

    }
    private static void viewForParameter(Scanner sc) throws SQLException {

        System.out.println("Selecet ");
        System.out.println("1: m2");
        System.out.println("2: number of rooms");
        System.out.println("3: price");
        String sel = sc.nextLine();
        System.out.println("Enter min <");
        String one = sc.nextLine();
        System.out.println("Enter max >");
        String two = sc.nextLine();
        String par="";
        switch (sel) {
            case "1":
                par="m2";
                break;
            case "2":
                par="numberOfRooms";
                break;
            case "3":
                par="price";
                break;
            default:
                return;
        }


String cc = "SELECT * FROM Apartament  WHERE " + par + " >= " + one + " AND " + par + " <= " + two;

        PreparedStatement ps = conn.prepareStatement(cc);
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }
}
