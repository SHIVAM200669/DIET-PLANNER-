

# 🥗 Diet Planner (Java Swing + MySQL)

## 📋 Description

A **Diet Planner System** built using **Java Swing** and **MySQL (JDBC)**. It allows users to **sign up, log in, and manage personalized diet plans**. Admins can monitor and update user data through a separate dashboard. The system uses a secure database connection for data storage and retrieval.

## ⚙️ Features

* User **Login** and **Signup** with validation
* **Admin Dashboard** for managing users and diets
* **User Dashboard** for adding and tracking meals
* MySQL database connectivity via **JDBC**
* Simple, responsive, and professional UI using **Java Swing**

## 🧰 Technologies Used

* Java (Swing for GUI)
* MySQL (Database)
* JDBC (Database Connectivity)

## 🚀 How to Run

1. Open the project in any Java IDE (Eclipse, IntelliJ, or NetBeans).
2. Create a MySQL database and import the provided SQL file.
3. Update database credentials in `DBConnection.java`.
4. Run `LoginFrame.java` to start the application.

## 📂 Main Files

```
DietPlanner/
 ├── DBConnection.java
 ├── LoginFrame.java
 ├── SignupFrame.java
 ├── DashboardFrame.java
 ├── AdminDashboard.java
 ├── README.md
 └── dietplanner.sql
```

## 💾 Database Structure

```sql
CREATE DATABASE dietplanner;
USE dietplanner;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    username VARCHAR(50),
    password VARCHAR(100),
    role VARCHAR(10)
);
```



Would you like me to now include the **Java code files** for these (login, signup, dashboard, DB connection, and admin dashboard)?
