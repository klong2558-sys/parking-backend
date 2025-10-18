package edu.sdsu.parkingbackend;

public class Student extends User{

    String studentID;
    String carInfo;

    public Student(String studentID, String carInfo, String userID, String userEmail) {
        this.studentID = studentID;
        this.carInfo = carInfo;
    }

    public void viewLots(){

    }

}
