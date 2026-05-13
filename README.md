# 🎓 Student Tuition Management System

> A desktop-based tuition management application developed in Java using NetBeans and MySQL for managing students, classes, tuition fees, and educational statistics.

![Java](https://img.shields.io/badge/Java-JDK-orange)
![NetBeans](https://img.shields.io/badge/IDE-NetBeans-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1)
![XAMPP](https://img.shields.io/badge/Server-XAMPP-FB7A24)
![License](https://img.shields.io/badge/license-MIT-green)

---

# 📌 Introduction

Student Tuition Management System is a desktop application designed to support educational institutions in managing:

- Student information
- Tuition fee calculation
- Class organization
- Tuition payment tracking
- Academic statistics
- Student records management

The system helps reduce manual work, improve management efficiency, and ensure tuition data accuracy.

---

# 🚀 Main Features

## 👨‍🎓 Student Management
- Add / Edit / Delete students
- Automatic student ID generation
- Search students by ID or name
- Store student personal information

## 🏫 Class Management
- Create and manage classes
- Assign students to classes
- View class lists

## 💰 Tuition Management
- Automatic tuition calculation
- Tuition payment status
- Tuition debt management
- Payment history tracking

## 📊 Statistics & Reports
- Tuition revenue statistics
- Unpaid tuition reports
- Student statistics by class
- Export reports

## 🔐 Authentication System
- Admin login
- User authorization
- Secure access management

---

# 🏗️ System Architecture

```bash id="8f4x3z"
Application Type : Desktop Application
Language         : Java
IDE              : NetBeans
Database         : MySQL
Server           : XAMPP
Architecture     : MVC
🛠️ Technologies Used
Technology	Purpose
Java Swing	User Interface
Java JDBC	Database Connection
MySQL	Database Management
XAMPP	Local Server
NetBeans	Development IDE
📂 Project Structure
StudentTuitionManagement/
│
├── src/
│   ├── controller/
│   ├── model/
│   ├── view/
│   ├── database/
│   └── utils/
│
├── library/
├── database/
│   └── tuition_management.sql
│
├── build/
├── dist/
└── README.md
🗄️ Database Design
Main Tables
STUDENTS
StudentID
FullName
DateOfBirth
Gender
PhoneNumber
Address
ClassID
CLASSES
ClassID
ClassName
Major
AcademicYear
TUITION_FEES
TuitionID
StudentID
Semester
Amount
PaymentStatus
CreatedAt
USERS
UserID
Username
Password
Role
⚙️ Installation Guide
1️⃣ Requirements
JDK 8+
NetBeans IDE
XAMPP
MySQL Server
2️⃣ Clone Project
git clone https://github.com/Basrot/Tuition-Fee-Management
3️⃣ Import Database
Open XAMPP
Start:
Apache
MySQL
Open phpMyAdmin
Create database:
CREATE DATABASE tuition_management;
Import:
tuition_management.sql
4️⃣ Configure Database Connection
String url = "jdbc:mysql://localhost:3306/tuition_management";
String user = "root";
String password = "";
5️⃣ Run Project

Open project using NetBeans and run:

Shift + F6
🧠 Core Functionalities
🔢 Automatic Student ID Generation

Example:

SV001
SV002
SV003

The system automatically generates unique student IDs when creating new students.

💵 Tuition Calculation Logic
Tuition Fee = Number of Credits × Tuition per Credit

Additional features:

Semester-based tuition
Payment status checking
Tuition debt calculation
📈 Statistical Features
Total tuition revenue
Unpaid tuition students
Student count by class
Tuition payment statistics
🖥️ User Interface Modules
Module	Description
Dashboard	System overview
Student Management	Manage students
Class Management	Manage classes
Tuition Management	Tuition calculation
Reports	Statistics & reports
Authentication	Login system
🔒 Security Features
Login authentication
Role-based authorization
Input validation
SQL query protection using PreparedStatement
📷 Screenshots

Add project screenshots here

/images/login.png
/images/dashboard.png
/images/student-management.png
/images/tuition-management.png
🧪 Testing

The system has been tested for:

Student CRUD operations
Tuition calculations
Database connectivity
Login authentication
Statistical reports
🔮 Future Improvements
Online payment integration
Email notifications
QR student identification
Multi-user management
Cloud database deployment
Export Excel/PDF reports
👨‍💻 Developer Information
Role	Information
Developer	Your Name
Project Type	Software Engineering / Java Desktop Project
IDE	NetBeans
Database	MySQL (XAMPP)
📚 Educational Purpose

This project was developed for:

Learning Java Desktop Development
Database Management
MVC Architecture
JDBC Connectivity
Software Engineering Practices
📄 License

MIT License © 2026

⭐ Project Highlights
Automatic student ID generation
Tuition fee calculation system
MySQL database integration
Java Swing desktop application
MVC architecture implementation
Real-world educational management workflow
