package com.company.sql_maven_homeWork;

import java.sql.*;
import java.util.Scanner;

public class Uzd1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inputCommand;


        do {
            printCommandList();
            System.out.println("-+-+-+-+-+-+-+");
            inputCommand = sc.nextLine();
            System.out.println("-+-+-+-+-+-+-+");

            switch (inputCommand) {
                case "d" -> {
                    System.out.println("---------------");
                    getAndPrintNamesSurnames();
                    System.out.println("---------------");
                }
                case "p" -> {
                    System.out.println("---------------");
                    getAndPrintProjectNameSurname();
                    System.out.println("---------------");
                }
                case "n" -> {
                    System.out.println("You want to add new employee=) Its no way back, go forward");

                    System.out.println("Please input asmens kodas 11 numbers:");
                    Long ak;
                    do {
                        try {
                            ak = Long.valueOf(sc.nextLine());
                            if (ak.toString().length() <= 11) {
                                break;
                            } else {
                                System.out.println("to long ak! Must be 11 symbols");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Asmens kodas have to be numbers");
                        }
                    } while (true);

                    System.out.println("Please input name:");
                    String name = sc.nextLine();
                    System.out.println("Please input surname:");
                    String surname = sc.nextLine();


                    addNewEmployee(ak, name, surname);
                    System.out.println("New employee added!");
                    System.out.println("---------------");
                    getAndPrintNamesSurnames();
                    System.out.println("---------------");
                }
                case "cp" -> {
                    printEmployeesAndProjects();
                    System.out.println("---------------");

                    System.out.println("Input Id of project will be to your employee:");
                    int id;
                    do {
                        try {
                            id = Integer.parseInt(sc.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Id should be number");
                        }
                    } while (true);

                    System.out.println("Input asmens kodas of employee:");
                    Long ak;
                    do {
                        try {
                            ak = Long.parseLong(sc.nextLine());
                            if (ak.toString().length() <= 11) {
                                break;
                            } else {
                                System.out.println("to long ak");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Asmens kodas have to be numbers");
                        }
                    } while (true);
                    changeProjectIdInEmployeeTable(id, ak);
                }
                case "q" -> {
                    System.out.println("Viso gero! / gl and hf");
                }
                default -> {
                    System.out.println("Nope, another command needed!");
                }

            }
        } while (!"q".equals(inputCommand));

        sc.close();
    }

    private static void printCommandList() {
        System.out.println("Please put in some command: \n" +
                "d - print all names; \n" +
                "p - print all projects; \n" +
                "n - add new employee; \n" +
                "cp - change project; \n" +
                "q - quit system;");
    }

    private static void getAndPrintNamesSurnames() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test",
                    "sa",
                    "");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT VARDAS, PAVARDE FROM DARBUOTOJAS");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Name: " + resultSet.getString("vardas") +
                        "; Surname: " + resultSet.getString("pavarde"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getAndPrintProjectNameSurname() {
        int idForSecondTable;
        int nrOfEmployee = 0;

        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test",
                    "sa",
                    "");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PROJEKTAS");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("= Project name: " + resultSet.getString("PAVADINIMAS") + " =");
                idForSecondTable = resultSet.getInt("ID");
                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT VARDAS, PAVARDE FROM DARBUOTOJAS WHERE PROJEKTAS_ID = " + idForSecondTable + ";");
                ResultSet resultSet2 = preparedStatement2.executeQuery();

                while (resultSet2.next()) {
                    nrOfEmployee++;
                    System.out.println(nrOfEmployee + ") Name: " + resultSet2.getString("vardas") +
                            "; Surname: " + resultSet2.getString("pavarde"));
                }
                nrOfEmployee = 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addNewEmployee(Long ak, String name, String surname) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test",
                    "sa",
                    "");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DARBUOTOJAS (ASMENSKODAS, VARDAS, PAVARDE)\n" +
                    "VALUES(" + ak + ", '" + name + "', '" + surname + "');");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printEmployeesAndProjects() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test",
                    "sa",
                    "");
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT ASMENSKODAS, VARDAS, PAVARDE, PROJEKTAS_ID, PAVADINIMAS FROM DARBUOTOJAS JOIN PROJEKTAS WHERE PROJEKTAS_ID = ID");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("AK, Name, Surname, Project ID, ProjectName: ");
            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString("ASMENSKODAS") +
                                " - " + resultSet.getString("VARDAS") +
                                " - " + resultSet.getString("PAVARDE") +
                                " - " + resultSet.getString("PROJEKTAS_ID") +
                                " - " + resultSet.getString("PAVADINIMAS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void changeProjectIdInEmployeeTable(int projectId, long ak) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test",
                    "sa",
                    "");

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE DARBUOTOJAS  \n" +
                    "SET PROJEKTAS_ID  = '" + projectId + "' WHERE ASMENSKODAS  = '" + ak + "';");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


