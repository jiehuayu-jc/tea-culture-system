@echo off
setlocal EnableExtensions EnableDelayedExpansion
echo 正在启动Spring Boot应用...

REM 修复常见问题：JAVA_HOME 指向无效目录会导致 mvnw 直接退出
if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" goto java_ok
)

REM 优先从 java.home 推导 JAVA_HOME（仅对当前窗口生效）
for /f "tokens=1,2,3* delims= " %%A in ('java -XshowSettings:properties -version 2^>^&1') do (
    if "%%A"=="java.home" if "%%B"=="=" (
        set "JAVA_HOME=%%C %%D"
    )
)

:java_ok
REM 检查是否安装了Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java，请先安装Java 8或更高版本
    pause
    exit /b 1
)

echo Java版本检查通过
echo JAVA_HOME=!JAVA_HOME!

REM 尝试使用Maven Wrapper运行（如果存在）
if exist mvnw.cmd (
    echo 使用Maven Wrapper运行...
    call mvnw.cmd spring-boot:run
    goto end
)

REM 尝试直接使用Maven运行
mvn -version >nul 2>&1
if %errorlevel% equ 0 (
    echo 使用Maven运行...
    mvn spring-boot:run
    goto end
)

echo 错误: 未找到Maven，请先安装Maven或使用IDE运行项目
echo.
echo 建议的解决方案:
echo 1. 安装Maven: https://maven.apache.org/download.cgi
echo 2. 使用IntelliJ IDEA或Eclipse导入项目并运行
echo 3. 使用Docker运行项目

pause

:end