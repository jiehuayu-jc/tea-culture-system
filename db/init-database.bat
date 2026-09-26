@echo off
chcp 65001 >nul
setlocal
cd /d "%~dp0"

set DB=springbootj8kskvkr
set SQLFILE=%~dp0springbootj8kskvkr.sql
set MYSQL_USER=root
set MYSQL_PWD=123456

if not exist "%SQLFILE%" (
  echo 找不到 SQL 文件: %SQLFILE%
  pause
  exit /b 1
)

set "MYSQL_BIN="
if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
if exist "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
if "%MYSQL_BIN%"=="" set "MYSQL_BIN=mysql"

echo ============================================
echo 一键初始化数据库（与 application.yml 一致）
echo 库名: %DB%用户: %MYSQL_USER%
echo 将执行: DROP 库 -^> CREATE 库 -^> 导入完整 springbootj8kskvkr.sql
echo 成功后约有 29 张表，含茶叶主题演示数据
echo.
echo 若你的 root 密码不是 123456，请右键编辑本 bat 修改 MYSQL_PWD
echo 使用的 mysql.exe: %MYSQL_BIN%
echo ============================================
pause

"%MYSQL_BIN%" -u%MYSQL_USER% -p%MYSQL_PWD% -e "DROP DATABASE IF EXISTS %DB%; CREATE DATABASE %DB% CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if errorlevel 1 (
  echo.
  echo [失败] 无法连接 MySQL。请确认：服务已启动、密码正确、或已将 mysql.exe 加入 PATH。
  pause
  exit /b 1
)

"%MYSQL_BIN%" -u%MYSQL_USER% -p%MYSQL_PWD% --default-character-set=utf8mb4 %DB% < "%SQLFILE%"
if errorlevel 1 (
  echo.
  echo [失败]导入 SQL 出错。请把 Navicat/命令行里的第一条报错复制下来排查。
  pause
  exit /b 1
)

echo.
echo [成功] 已导入。请在 Navicat 中对库 %DB% 刷新「表」，应能看到 yonghu。
pause
endlocal
