$ErrorActionPreference = 'Stop'

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$AppHome = Split-Path -Parent $ScriptDir

& (Join-Path $AppHome 'service.sh') 'start' 'all'
