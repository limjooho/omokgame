param(
    [Parameter(Mandatory = $true)]
    [string]$JavaFxLib,
    [switch]$CompileOnly
)
$ErrorActionPreference = 'Stop'
if (-not (Test-Path -LiteralPath (Join-Path $JavaFxLib 'javafx.controls.jar'))) {
    throw 'JavaFxLib에 JavaFX SDK의 lib 폴더 경로를 지정하세요.'
}
$sourceRoot = Join-Path $PSScriptRoot 'src'
$outputRoot = Join-Path $PSScriptRoot 'bin'
New-Item -ItemType Directory -Force -Path $outputRoot | Out-Null
$sources = @(Get-ChildItem -LiteralPath $sourceRoot -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
& javac -encoding UTF-8 --release 25 --module-path $JavaFxLib -d $outputRoot $sources
if ($LASTEXITCODE -ne 0) { throw 'Java 컴파일에 실패했습니다.' }
# javac는 CSS와 PNG를 복사하지 않으므로 상대 경로를 유지해 출력 폴더에 포함한다.
Get-ChildItem -LiteralPath $sourceRoot -Recurse -File | Where-Object { $_.Extension -ne '.java' } | ForEach-Object {
    $relativeResource = $_.FullName.Substring($sourceRoot.Length + 1)
    $destination = Join-Path $outputRoot $relativeResource
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $destination) | Out-Null
    Copy-Item -LiteralPath $_.FullName -Destination $destination -Force
}
if (-not $CompileOnly) {
    & java --enable-native-access=javafx.graphics,javafx.media --module-path "$JavaFxLib;$outputRoot" -m omokgame/omokgame.omok
    if ($LASTEXITCODE -ne 0) { throw 'JavaFX 실행에 실패했습니다.' }
}
