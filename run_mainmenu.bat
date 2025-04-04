@echo off
cd /d D:\PROJECT\Canteen_Management

:menu
cls
echo ======================================
echo        Delicious Kitchen System       
echo ======================================
echo.
echo 1. Run MainMenu
echo 2. Exit
echo.

set /p choice="Enter your choice (1 or 2): "

if "%choice%"=="1" (
    echo.
    echo Compiling MainMenu.java...
    javac -cp ".;libs\mysql-connector-j-9.2.0.jar" MainMenu.java

    echo.
    echo Running MainMenu...
    java -cp ".;libs\mysql-connector-j-9.2.0.jar" MainMenu

    echo.
    pause
    goto menu
) else if "%choice%"=="2" (
    echo.
    echo Exiting... Goodbye!
    timeout /t 2 >nul
    exit
) else (
    echo Invalid choice. Try again.
    timeout /t 2 >nul
    goto menu
)
