@echo off
setlocal enabledelayedexpansion

echo ========================================================
echo   Student Management System - One-Click Launcher
echo           (No Eclipse Required!)
echo ========================================================
echo.

set "PROJECT_DIR=%~dp0"
set "SRC_DIR=%PROJECT_DIR%src"
set "WEB_DIR=%PROJECT_DIR%WebContent"
set "CLASSES_DIR=%WEB_DIR%\WEB-INF\classes"
set "LIB_DIR=%WEB_DIR%\WEB-INF\lib"
set "TOMCAT_DIR=%PROJECT_DIR%apache-tomcat-9.0.86"

:: 1. Create classes directory
if not exist "%CLASSES_DIR%" mkdir "%CLASSES_DIR%"

:: 2. Locate javac (Java Compiler)
set "JAVAC_CMD=javac"
where javac >nul 2>nul
if %errorlevel% equ 0 (
    goto :FOUND_JAVAC
)

:: Search JDK in common Windows locations
for /d %%i in ("C:\Program Files\Java\jdk*") do (
    if exist "%%i\bin\javac.exe" (
        set "JAVAC_CMD=%%i\bin\javac.exe"
        set "JAVA_HOME=%%i"
        goto :FOUND_JAVAC
    )
)
for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk*") do (
    if exist "%%i\bin\javac.exe" (
        set "JAVAC_CMD=%%i\bin\javac.exe"
        set "JAVA_HOME=%%i"
        goto :FOUND_JAVAC
    )
)
for /d %%i in ("C:\Program Files\Amazon Corretto\jdk*") do (
    if exist "%%i\bin\javac.exe" (
        set "JAVAC_CMD=%%i\bin\javac.exe"
        set "JAVA_HOME=%%i"
        goto :FOUND_JAVAC
    )
)
for /d %%i in ("C:\Program Files\Zulu\jdk*") do (
    if exist "%%i\bin\javac.exe" (
        set "JAVAC_CMD=%%i\bin\javac.exe"
        set "JAVA_HOME=%%i"
        goto :FOUND_JAVAC
    )
)
for /d %%i in ("C:\Program Files\Microsoft\jdk*") do (
    if exist "%%i\bin\javac.exe" (
        set "JAVAC_CMD=%%i\bin\javac.exe"
        set "JAVA_HOME=%%i"
        goto :FOUND_JAVAC
    )
)

echo [ERROR] 'javac' was not found automatically.
echo Please install JDK (Java Development Kit) or set JAVA_HOME.
pause
exit /b 1

:FOUND_JAVAC
echo [1/3] Found Java Compiler at: "!JAVAC_CMD!"
if defined JAVA_HOME echo       JAVA_HOME is set to: "!JAVA_HOME!"

:: 3. Compile Java Source Files
echo.
echo [2/3] Compiling Java classes...
set "CP=%LIB_DIR%\mysql-connector-j-8.3.0.jar;%LIB_DIR%\javax.servlet-api-4.0.1.jar"

dir /s /b "%SRC_DIR%\*.java" > "%PROJECT_DIR%sources.txt"
"!JAVAC_CMD!" -encoding UTF-8 -cp "%CP%" -d "%CLASSES_DIR%" @"%PROJECT_DIR%sources.txt"
if %errorlevel% neq 0 (
    echo [ERROR] Java compilation failed.
    del "%PROJECT_DIR%sources.txt" 2>nul
    pause
    exit /b 1
)
del "%PROJECT_DIR%sources.txt" 2>nul
echo [OK] All Java files compiled successfully into WebContent\WEB-INF\classes!
echo.

:: 4. Check for Apache Tomcat (or download portable Tomcat 9)
if not exist "%TOMCAT_DIR%\bin\catalina.bat" (
    echo [3/3] Downloading portable Apache Tomcat 9 server (approx. 12 MB)...
    set "TOMCAT_ZIP=%PROJECT_DIR%tomcat.zip"
    curl -L -o "!TOMCAT_ZIP!" "https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.86/bin/apache-tomcat-9.0.86-windows-x64.zip"
    
    echo Extracting Apache Tomcat...
    tar -xf "!TOMCAT_ZIP!" -C "%PROJECT_DIR%" 2>nul
    if not exist "%TOMCAT_DIR%\bin\catalina.bat" (
        powershell -Command "Expand-Archive -Path '!TOMCAT_ZIP!' -DestinationPath '%PROJECT_DIR%' -Force"
    )
    del "!TOMCAT_ZIP!" 2>nul
)

:: 5. Deploy application to Tomcat's webapps folder
echo Deploying Student Management System to Tomcat...
set "DEPLOY_DIR=%TOMCAT_DIR%\webapps\student_management_system"
if exist "%DEPLOY_DIR%" rmdir /s /q "%DEPLOY_DIR%"
mkdir "%DEPLOY_DIR%"

xcopy /E /I /Y "%WEB_DIR%\*" "%DEPLOY_DIR%\" >nul
echo [OK] Deployment complete!
echo.

:: 6. Launch Tomcat and open browser
echo ========================================================
echo  STARTING APACHE TOMCAT SERVER...
echo  Your web application will open at:
echo  http://localhost:8080/student_management_system/
echo ========================================================
echo.
echo Note: Keep this black command window open while using the app!
echo.

start http://localhost:8080/student_management_system/
call "%TOMCAT_DIR%\bin\catalina.bat" run
pause
