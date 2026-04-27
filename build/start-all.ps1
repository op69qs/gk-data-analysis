$ErrorActionPreference = 'Stop'

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$AppHome = Split-Path -Parent $ScriptDir
$RunDir = Join-Path $AppHome 'run'
$LogDir = Join-Path $AppHome 'logs'
$SpringProfile = if ($env:SPRING_PROFILES_ACTIVE) { $env:SPRING_PROFILES_ACTIVE } else { 'dev' }
$JavaCmd = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME 'bin\java.exe' } else { 'java' }

$Modules = @(
    @{ Name = 'dwbi-statistical-analysis'; JarPattern = 'dwbi-statisticalAnalysis-*.jar'; MainClass = 'org.triber.analysis.StatisticalAnalysisApplication'; LaunchMode = 'classpath' },
    @{ Name = 'dwbi-system-docking'; JarPattern = 'dwbi-system-docking-*.jar'; MainClass = 'org.dockingProjects.SystemDockingApplication'; LaunchMode = 'classpath' },
    @{ Name = 'fixedReport'; JarPattern = 'fixedReport-*.jar'; MainClass = 'org.fixedReport.FixedReportApplication'; LaunchMode = 'classpath' },
    @{ Name = 'indicatorsLibv-1.0'; JarPattern = 'indicatorsLib-*.jar'; MainClass = 'org.indicatorsLib.IndicatorsLibApplication'; LaunchMode = 'classpath' },
    @{ Name = 'org-tribe-system'; JarPattern = 'org-tribe-system-*.jar'; MainClass = 'org.jeecg.JeecgApplication'; LaunchMode = 'classpath' },
    @{ Name = 'seo'; JarPattern = 'seo-*.jar'; MainClass = 'org.seo.ComprehensiveQuery'; LaunchMode = 'classpath' },
    @{ Name = 'vis-screen'; JarPattern = 'vis-screen-*.jar'; MainClass = 'org.jeecg.JeecgSystemApplication'; LaunchMode = 'fatjar' }
)

function Resolve-Jar {
    param([string]$Pattern)
    $match = Get-ChildItem -Path (Join-Path $AppHome 'app') -Filter $Pattern | Select-Object -First 1
    return $match
}

function Get-PidFile {
    param([string]$ModuleName)
    Join-Path $RunDir ($ModuleName + '.pid')
}

function Test-ModuleRunning {
    param([string]$ModuleName)
    $pidFile = Get-PidFile $ModuleName
    if (-not (Test-Path $pidFile)) {
        return $false
    }
    $pidValue = Get-Content $pidFile -ErrorAction SilentlyContinue
    if (-not $pidValue) {
        Remove-Item $pidFile -Force -ErrorAction SilentlyContinue
        return $false
    }
    $process = Get-Process -Id ([int]$pidValue) -ErrorAction SilentlyContinue
    if ($null -eq $process) {
        Remove-Item $pidFile -Force -ErrorAction SilentlyContinue
        return $false
    }
    return $true
}

function Start-Module {
    param($Module)

    if (Test-ModuleRunning $Module.Name) {
        Write-Output "[RUNNING] $($Module.Name)"
        return
    }

    $jar = Resolve-Jar $Module.JarPattern
    if ($null -eq $jar) {
        Write-Output "[MISSING] $($Module.Name) jar not found for pattern $($Module.JarPattern)"
        return
    }

    New-Item -ItemType Directory -Force -Path $RunDir | Out-Null
    $moduleLogDir = Join-Path $LogDir $Module.Name
    New-Item -ItemType Directory -Force -Path $moduleLogDir | Out-Null

    $stdoutLog = Join-Path $moduleLogDir 'console.log'
    $stderrLog = Join-Path $moduleLogDir 'console.err.log'
    $configArg = '--spring.config.additional-location=file:' + (Join-Path $AppHome ('config/' + $Module.Name + '/'))
    $profileArg = '--spring.profiles.active=' + $SpringProfile

    if ($Module.LaunchMode -eq 'fatjar') {
        $argumentList = @(
            '-Dserver.bes.basedir=' + (Join-Path $AppHome ('bes/' + $Module.Name)),
            '-jar',
            $jar.FullName,
            $profileArg,
            $configArg
        )
    }
    else {
        $classPath = (Join-Path $AppHome ('app/' + $jar.Name)) + ';' + (Join-Path $AppHome ('lib/' + $Module.Name + '/*'))
        $argumentList = @(
            '-cp',
            $classPath,
            $Module.MainClass,
            $profileArg,
            $configArg
        )
    }

    $process = Start-Process -FilePath $JavaCmd -ArgumentList $argumentList -RedirectStandardOutput $stdoutLog -RedirectStandardError $stderrLog -PassThru -WindowStyle Hidden
    Set-Content -Path (Get-PidFile $Module.Name) -Value $process.Id
    Write-Output "[STARTED] $($Module.Name)"
}

function Stop-Module {
    param($Module)

    $pidFile = Get-PidFile $Module.Name
    if (-not (Test-Path $pidFile)) {
        Write-Output "[STOPPED] $($Module.Name)"
        return
    }

    $pidValue = Get-Content $pidFile -ErrorAction SilentlyContinue
    if ($pidValue) {
        Stop-Process -Id ([int]$pidValue) -Force -ErrorAction SilentlyContinue
    }
    Remove-Item $pidFile -Force -ErrorAction SilentlyContinue
    Write-Output "[STOPPED] $($Module.Name)"
}

function Show-ModuleStatus {
    param($Module)

    if (Test-ModuleRunning $Module.Name) {
        Write-Output "[RUNNING] $($Module.Name)"
    }
    else {
        Write-Output "[STOPPED] $($Module.Name)"
    }
}

$Action = if ($args.Count -gt 0) { $args[0] } else { 'status' }

foreach ($module in $Modules) {
    switch ($Action) {
        'start' { Start-Module $module }
        'stop' { Stop-Module $module }
        'restart' { Stop-Module $module; Start-Module $module }
        'status' { Show-ModuleStatus $module }
        default {
            throw 'Usage: start-all.ps1 [start|stop|restart|status]'
        }
    }
}