package HospitalManagementSystem;

import java.sql.*;
import java.util.Deque;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital"; //JDBC connection String
    private static final String username="root";
    private static final String password="Bingo@123";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");//load the drivers neccesary to connect to DB

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:patient.addPatient();break;
                    case 2:patient.viewPatients();break;
                    case 3:doctor.viewDoctors();break;
                    case 4:bookAppointements(patient,doctor,connection,scanner);break;
                    case 5:
                        System.out.println("Thank you for using HMS");
                        return;
                    default :
                    System.out.println("Enter valid choice!!!");
                    break;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointements(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointement date (YYYY-MM-DD): ");
        String appointementDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
         if(checkDoctorAvailability(doctorId,appointementDate,connection)){
             String appointementQuery = "Insert into appointements(patient_id,doctor_id,appointement_date) VALUES(?,?,?)";
             try{
                 PreparedStatement preparedStatement = connection.prepareStatement(appointementQuery);
                 preparedStatement.setInt(1,patientId);
                 preparedStatement.setInt(2,doctorId);
                 preparedStatement.setString(3,appointementDate);
                 int affectedRows = preparedStatement.executeUpdate();
                 if(affectedRows >0) System.out.println("Appointment Booked");
                 else System.out.println("Failed to book appointment");
             }catch (SQLException e){
                 e.printStackTrace();
             }

         }
         else System.out.println("Doctor not available on this date!!!");
        }
        else{
            System.out.println("Either patient or Doctor doesn't exists!!!");
        }

    }
    public static boolean checkDoctorAvailability(int id,String date,Connection connection){
        String query = "Select count(*) from appointements where doctor_id = ? and appointement_date=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count ==0) return true;
                else return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
