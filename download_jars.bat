@echo off
echo ========================================================
echo Downloading Required JARs for Student Management System
echo (mysql-connector-j and servlet-api)
echo ========================================================
echo.

if not exist "WebContent\WEB-INF\lib" (
    mkdir "WebContent\WEB-INF\lib"
)

echo [1/2] Downloading MySQL Connector/J JDBC Driver...
curl -L -o "WebContent\WEB-INF\lib\mysql-connector-j-8.3.0.jar" "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar"

echo.
echo [2/2] Downloading Servlet API JAR...
curl -L -o "WebContent\WEB-INF\lib\javax.servlet-api-4.0.1.jar" "https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar"

echo.
echo ========================================================
echo SUCCESS! All required JARs are in WebContent\WEB-INF\lib
echo In Eclipse/IntelliJ, press F5 or Refresh Project!
echo ========================================================
pause
