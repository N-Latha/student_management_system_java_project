@echo off
setlocal enabledelayedexpansion

echo ========================================================
echo   Build Standalone WAR File for Apache Tomcat Deployment
echo ========================================================
echo.

set "PROJECT_DIR=%~dp0"
set "SRC_DIR=%PROJECT_DIR%src"
set "WEB_DIR=%PROJECT_DIR%WebContent"
set "CLASSES_DIR=%WEB_DIR%\WEB-INF\classes"
set "LIB_DIR=%WEB_DIR%\WEB-INF\lib"
set "WAR_FILE=%PROJECT_DIR%student_management_system.war"

:: 1. Create classes directory
if not exist "%CLASSES_DIR%" mkdir "%CLASSES_DIR%"

:: 2. Locate javac (Java Compiler)
set "JAVAC_CMD=javac"
where javac >nul 2>nul
if %errorlevel% equ 0 goto :COMPILE

for /d %%i in ("C:\Program Files\Java\jdk*") do (
    if exist "%%i\bin\javac.exe" (
        set "JAVAC_CMD=%%i\bin\javac.exe"
        goto :COMPILE
    )
)
for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk*") do (
    if exist "%%i\bin\javac.exe" (
        set "JAVAC_CMD=%%i\bin\javac.exe"
        goto :COMPILE
    )
)

:COMPILE
echo [1/2] Compiling Java classes...
set "CP=%LIB_DIR%\mysql-connector-j-8.3.0.jar;%LIB_DIR%\javax.servlet-api-4.0.1.jar"
dir /s /b "%SRC_DIR%\*.java" > "%PROJECT_DIR%sources.txt"
"!JAVAC_CMD!" -encoding UTF-8 -cp "%CP%" -d "%CLASSES_DIR%" @"%PROJECT_DIR%sources.txt"
del "%PROJECT_DIR%sources.txt" 2>nul
echo [OK] Compilation complete!

:: 3. Package into WAR file using jar / powershell zip
echo.
echo [2/2] Packaging into student_management_system.war...
where jar >nul 2>nul
if %errorlevel% equ 0 (
    cd /d "%WEB_DIR%"
    jar -cvf "%WAR_FILE%" *
) else (
    powershell -Command "Compress-Archive -Path '%WEB_DIR%\*' -DestinationPath '%WAR_FILE%' -Force"
)

echo.
echo ========================================================
echo SUCCESS! Created: student_management_system.war
echo.
echo TO DEPLOY ON YOUR OTHER LAPTOP WITH TOMCAT:
echo 1. Copy 'student_management_system.war' to the other laptop.
echo 2. Paste it into your Tomcat's 'webapps' folder:
echo    (e.g., C:\apache-tomcat-9.x\webapps\)
echo 3. Start Tomcat (run bin\startup.bat).
echo 4. Open in browser:
echo    http://localhost:8080/student_management_system/
echo ========================================================
pause
