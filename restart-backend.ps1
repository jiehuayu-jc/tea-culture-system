$cl = (Get-Content "$env:TEMP\backend-cmdline.txt" -Raw).Trim()
$exe = $cl.Substring(1, $cl.IndexOf('"', 1) - 1)
$rest = $cl.Substring($cl.IndexOf('"', 1) + 2)
Start-Process -FilePath $exe -ArgumentList $rest -WorkingDirectory "C:\Users\14483\Desktop\springbootj8kskvkr" -WindowStyle Hidden
Write-Output "launched"
