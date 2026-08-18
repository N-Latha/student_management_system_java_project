@echo off

echo ========================================================
echo   Student Management System - Zero-Setup Launcher
echo ========================================================
echo.

cd /d "%~dp0"

if not exist "bin" mkdir "bin"

set "CP=WebContent\WEB-INF\lib\mysql-connector-j-8.3.0.jar;WebContent\WEB-INF\lib\javax.servlet-api-4.0.1.jar"

echo [1/2] Compiling Java source files...
javac -encoding UTF-8 -cp "%CP%" -d "bin" "src\com\studentmanagement\model\Student.java" "src\com\studentmanagement\util\DatabaseConnection.java" "src\com\studentmanagement\dao\StudentDAO.java" "src\com\studentmanagement\servlet\*.java" "src\com\studentmanagement\AppServer.java"

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed. Please ensure JDK is in your PATH.
    pause
    exit /b 1
)

echo [OK] Compilation successful.
echo.
echo [2/2] Starting web server on http://localhost:8085/
echo.
echo ========================================================
echo  LIVE APPLICATION: http://localhost:8085/
echo ========================================================
echo.

start http://localhost:8085/
java -cp "bin;%CP%" com.studentmanagement.AppServer

pause
