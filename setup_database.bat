@echo off
setlocal enabledelayedexpansion

echo ========================================================
echo Setting up MySQL Database for Student Management System
echo ========================================================
echo.

set "MYSQL_CMD=mysql"

:: 1. Check if mysql is directly in PATH
where mysql >nul 2>nul
if %errorlevel% equ 0 (
    set "MYSQL_CMD=mysql"
    goto :RUN_SQL
)

:: 2. Auto-detect common MySQL Server installation paths
if exist "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe"
    goto :RUN_SQL
)
if exist "C:\Program Files\MySQL\MySQL Server 8.3\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.3\bin\mysql.exe"
    goto :RUN_SQL
)
if exist "C:\Program Files\MySQL\MySQL Server 8.2\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.2\bin\mysql.exe"
    goto :RUN_SQL
)
if exist "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe"
    goto :RUN_SQL
)
if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    goto :RUN_SQL
)
if exist "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
    goto :RUN_SQL
)
if exist "C:\xampp\mysql\bin\mysql.exe" (
    set "MYSQL_CMD=C:\xampp\mysql\bin\mysql.exe"
    goto :RUN_SQL
)

echo Could not find mysql.exe automatically.
echo Please run the queries using "MySQL Command Line Client" from your Windows Start Menu.
echo.
pause
exit /b 1

:RUN_SQL
echo Found MySQL at: "!MYSQL_CMD!"
echo.
echo Please enter your MySQL root password when prompted below:
echo.

"!MYSQL_CMD!" -u root -p < database.sql

if %errorlevel% equ 0 (
    echo.
    echo ========================================================
    echo SUCCESS! Database 'student_management' and 'students' table created!
    echo Sample student records inserted successfully.
    echo ========================================================
) else (
    echo.
    echo ========================================================
    echo ERROR: Could not connect to MySQL.
    echo Verify your MySQL password and ensure MySQL service is running.
    echo ========================================================
)

pause
