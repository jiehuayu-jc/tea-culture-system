@echo off
setlocal EnableExtensions EnableDelayedExpansion
chcp 936 >nul
title 茶文化管理系统 - 后端启动
echo.
echo ============================================
echo   正在启动 Spring Boot 应用（茶文化管理系统）
echo ============================================
echo.

REM ===== 1. 解析 JAVA_HOME：本项目基于 JDK 8 开发，优先使用 JDK 8 =====
set "JAVA_HOME_RESOLVED="

REM 1a. 现有 JAVA_HOME 已指向有效的 JDK 8（目录名含 1.8 / jdk8）
if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" (
        echo %JAVA_HOME% | findstr /i "1.8 jdk8" >nul && set "JAVA_HOME_RESOLVED=%JAVA_HOME%"
    )
)

REM 1b. 探测常见 JDK 8 安装路径
if not defined JAVA_HOME_RESOLVED (
    for /d %%D in ("C:\Program Files\Java\jdk1.8*","C:\Program Files\Java\jdk8*","C:\Program Files (x86)\Java\jdk1.8*","C:\Program Files\Eclipse Adoptium\jdk-8*") do (
        if exist "%%D\bin\java.exe" if not defined JAVA_HOME_RESOLVED set "JAVA_HOME_RESOLVED=%%D"
    )
)

REM 1c. 兜底：从 PATH 的 java 推导（若为 JDK 9+，老项目可能启动失败，见下方提示）
if not defined JAVA_HOME_RESOLVED (
    for /f "tokens=1* delims==" %%A in ('java -XshowSettings:properties -version 2^>^&1 ^| findstr /i /c:"java.home"') do set "JH_RAW=%%B"
    for /f "tokens=* delims= " %%Z in ("!JH_RAW!") do set "JAVA_HOME_RESOLVED=%%Z"
)

if not defined JAVA_HOME_RESOLVED (
    echo [错误] 未找到任何 Java 环境，请先安装 JDK 8。
    pause
    exit /b 1
)

set "JAVA_HOME=%JAVA_HOME_RESOLVED%"
echo JAVA_HOME=%JAVA_HOME%
echo %JAVA_HOME% | findstr /i "1.8 jdk8" >nul || echo [提示] 检测到的 Java 不是 JDK 8，本项目基于 JDK 8 开发，若启动失败请安装 JDK 8。
"%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr /i "version"
echo.

REM ===== 2. 使用 Maven Wrapper 启动 =====
if exist mvnw.cmd (
    echo 使用 Maven Wrapper 启动中，首次运行需下载依赖，请耐心等待...
    call mvnw.cmd spring-boot:run
    goto end
)

REM ===== 3. 兜底：系统安装的 Maven =====
mvn -version >nul 2>&1
if %errorlevel% equ 0 (
    echo 使用系统 Maven 启动...
    mvn spring-boot:run
    goto end
)

echo [错误] 未找到 Maven Wrapper 与系统 Maven。
echo.
echo 建议的解决方案:
echo 1. 在项目根目录执行 run.bat（内置 Maven Wrapper，无需安装 Maven）
echo 2. 使用 IntelliJ IDEA 导入项目后运行 com.TeaCultureApplication
echo 3. 使用 Docker: docker compose up -d
pause

:end
