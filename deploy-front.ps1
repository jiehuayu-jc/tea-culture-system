# 前端构建并部署（前端 + classpath 双写）
#
# 为什么需要这个脚本：
#   application.yml 的 static-locations 是 "classpath:static/,file:static/"，
#   classpath 优先 —— Spring Boot 实际读的是 target/classes/static/front，
#   而不是 src/main/resources/static/front。
#   只往源码目录部署，运行中的服务不会生效（会一直返回旧 hash 的资源）。
#
# 用法：pwsh -File deploy-front.ps1

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$fe = Join-Path $root 'src\main\resources\front\front'

Write-Host "[1/3] 构建前端..." -ForegroundColor Cyan
Push-Location $fe
try {
    & 'C:\Program Files\nodejs\npm.cmd' run build
    if ($LASTEXITCODE -ne 0) { throw "构建失败 (exit $LASTEXITCODE)" }
} finally {
    Pop-Location
}

$dist = Join-Path $fe 'dist'
if (-not (Test-Path $dist)) { throw "未找到构建产物: $dist" }

# 必须两处都写：源码目录入库，classpath 目录供运行中的服务读取
$targets = @(
    (Join-Path $root 'src\main\resources\static\front'),
    (Join-Path $root 'target\classes\static\front')
)

Write-Host "[2/3] 部署..." -ForegroundColor Cyan
foreach ($t in $targets) {
    if (-not (Test-Path $t)) { New-Item -ItemType Directory -Force -Path $t | Out-Null }
    Remove-Item "$t\*" -Recurse -Force -ErrorAction SilentlyContinue
    Copy-Item "$dist\*" $t -Recurse -Force
    $n = (Get-ChildItem $t -Recurse -File | Measure-Object).Count
    Write-Host ("  -> {0}  ({1} 文件)" -f $t.Replace($root, '.'), $n)
}

Write-Host "[3/3] 验证..." -ForegroundColor Cyan
$idx = Join-Path $targets[1] 'index.html'
$m = [regex]::Match((Get-Content $idx -Raw), 'css/app\.[a-f0-9]+\.css')
if ($m.Success) {
    Write-Host ("  当前产物: {0}" -f $m.Value) -ForegroundColor Green
    Write-Host "  浏览器请用 Ctrl+Shift+R 强刷（index.html 无 hash，可能命中缓存）" -ForegroundColor Yellow
} else {
    throw "index.html 中未找到 CSS 引用，部署可能已损坏"
}
