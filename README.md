# 3D 오목

팀 프로젝트로 개발 중인 JavaFX 기반 3D 오목의 시작 화면입니다.
상단 제목, 가운데 큰 모드 버튼 2개, 하단 전적 확인·종료 버튼을 배치했습니다.
창 크기 조절, 마우스 강조, 키보드 Tab/Space 조작을 지원합니다.

밝은 크림색 배경에 민트색 2인 대전 카드, 라벤더색 AI 대전 카드,
골드색 전적 버튼을 배치했습니다. 두 모드 카드에는 생성한 투명 PNG 이미지를 사용합니다.
`src/omokgame/menu.css`에서 색상, 여백, 호버 및 포커스 스타일을 변경할 수 있습니다.
이미지는 `src/omokgame/assets/gomoku-board.png`, `ai-robot.png`에 있으며
생성 프롬프트는 같은 폴더의 `PROMPTS.md`에 기록했습니다.
`run.ps1`은 CSS 및 이미지도 `bin`으로 복사합니다.

## 팀원이 내려받고 실행하기

Git과 JDK 25, 각자 사용하는 운영체제와 CPU에 맞는 JavaFX SDK 27.0.1을 설치합니다.
현재 실행 스크립트는 Windows PowerShell 기준입니다.
JavaFX는 JDK와 별도로 설치하며, 다른 사람의 Downloads 경로를 그대로 쓰지 않습니다.
공식 설치 안내: https://openjfx.io/openjfx-docs/install-javafx

비공개 저장소이므로 저장소 소유자의 초대를 수락하고 GitHub에 로그인한 뒤 clone합니다.

```powershell
git clone https://github.com/limjooho/omokgame.git
cd omokgame
```

JDK 25의 `java`와 `javac`가 PATH에 등록되어 있는지 확인합니다.

```powershell
java -version
javac -version
```

프로젝트 폴더에서 실행합니다. `JavaFxLib`는 **팀원 본인이 설치한 SDK의 lib 폴더**로 바꾸세요.

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 -JavaFxLib 'C:\javafx-sdk-27.0.1\lib'
```

컴파일만 확인하려면 같은 명령 끝에 `-CompileOnly`를 붙입니다.
스크립트가 Java 파일을 컴파일하고 CSS, 이미지, 효과음을 `bin`으로 복사합니다.

IntelliJ에서는 내려받은 폴더를 열고 Project SDK를 JDK 25로 지정합니다.
`src`를 Sources Root로 지정하고, Module Dependencies에 본인의 JavaFX SDK `lib` 안의
`javafx.base.jar`, `javafx.graphics.jar`, `javafx.controls.jar`, `javafx.media.jar`를 추가합니다.
메인 클래스는 `omokgame.omok`입니다. IDE별 설정 파일은 각 PC에서 생성하며 Git으로 공유하지 않습니다.

다른 팀원의 변경 사항은 작업 내용을 커밋한 뒤 `git pull`로 받습니다.
새 기능은 각자 브랜치를 만들어 작업하고 Pull Request로 합칩니다.

## 구현 범위

- `src/omokgame/omok.java`: 레이아웃, 버튼 스타일과 이벤트.
- `src/module-info.java`: JavaFX 모듈 설정.
- 시작 시 돌 착수 인트로, 보통/어려움 난이도 선택창, 효과음, 볼륨/음소거 및 다크 모드를 제공합니다.
- 실제 3D 게임과 AI는 아직 구현 전입니다. `game3DstartButton()`과 `AIGamestartButton()`에 게임 화면을 연결하면 됩니다.
- 전적 확인은 저장 기능을 연결하기 전의 빈 상태 안내입니다.
- 종료 버튼과 창 닫기는 확인 후 프로그램을 종료합니다.

## 효과음 사용

`SoundManager.PlaySoundEffect()`에 `SoundEffect` 종류를 전달해 재생합니다.
메인 화면과 안내창에는 짧은 클릭음을 연결했습니다.
인트로에서는 0.5초에 하강 소리, 돌이 바둑판에 닿는 1.1초에 착수음을 재생합니다.
인트로가 끝나거나 건너뛰면 남은 인트로 소리를 멈추고 메뉴 전환음을 재생합니다.
난이도 카드에는 전용 선택음만 사용하며, 취소 버튼과 Esc, X에는 취소음을 연결했습니다.

```java
SoundManager.PlaySoundEffect(SoundEffect.BUTTON_CLICK); // 버튼 클릭
SoundManager.PlaySoundEffect(SoundEffect.INTRO_PLACE);  // 인트로 착수
SoundManager.PlaySoundEffect(SoundEffect.STONE_PLACE);  // 게임 중 정상 착수 후 호출
SoundManager.PlaySoundEffect(SoundEffect.VICTORY);      // 사용자 승리 확정 후 호출
SoundManager.PlaySoundEffect(SoundEffect.DEFEAT);       // 사용자 패배 확정 후 호출
SoundManager.PlaySoundEffect(SoundEffect.INTRO_SWISH); // 인트로에서 돌이 내려오기 시작할 때
SoundManager.PlaySoundEffect(SoundEffect.MENU_OPEN); // 메뉴가 나타날 때
SoundManager.PlaySoundEffect(SoundEffect.SELECT_NORMAL); // 보통 난이도 선택
SoundManager.PlaySoundEffect(SoundEffect.SELECT_HARD); // 어려움 난이도 선택
SoundManager.PlaySoundEffect(SoundEffect.CANCEL); // 선택 취소
```

게임 화면과 승패 판정은 아직 구현 전이므로 STONE_PLACE, VICTORY, DEFEAT는 호출 준비만 되어 있습니다.
효과음 열 개는 외부 녹음이나 다운로드 없이 직접 합성한 스테레오 WAV 파일이며 `src/omokgame/assets/sounds/`에 있습니다.
돌 소리는 충격음과 여러 나무판 공명음을 겹쳤고, 짧은 좌우 잔향을 더했습니다.
승리는 밝게 올라가는 화음과 멜로디, 패배는 낮아지는 단조 멜로디로 구분했습니다.
보통 선택음은 밝은 두 음, 어려움 선택음은 낮은 충격음과 단조 화음으로 구분했습니다.
같은 이름의 WAV 파일로 교체하면 소리를 바꿀 수 있습니다.
효과음은 처음 재생할 때 읽어 두고 다음부터 재사용합니다.
볼륨은 `SoundManager.java`의 `volume` 값에서 조절하며 0은 무음, 1은 최대입니다.
일반 효과음은 0.65, 인트로 착수는 0.8, 바람은 0.35, 메뉴 전환은 0.5, 취소는 0.45로 설정했습니다.
사운드 파일이 없거나 재생에 실패하면 콘솔에 원인을 출력하고 게임은 계속 실행합니다.
JavaFX SDK의 `javafx.media` 모듈이 필요하며 `run.ps1`은 WAV도 출력 폴더에 복사합니다.

## 환경설정

인트로가 끝난 뒤 메인 화면 왼쪽 아래의 작은 톱니바퀴 버튼으로 설정창을 엽니다.

- 효과음 볼륨: 0~100%로 즉시 조절하며, 각 효과음의 기본 크기에 곱해서 적용합니다.
- 소리 미리 듣기: 현재 볼륨으로 게임 착수음을 재생합니다.
- 음소거: 재생 중인 소리를 멈추고 모든 효과음을 끕니다. 해제하면 이전 볼륨을 사용합니다.
- 다크 모드: 메인 화면, 인트로 배경, 난이도 선택창, 설정창과 안내창을 어둡게 표시합니다.

설정은 즉시 적용되며 닫기, Esc 또는 X로 설정창을 닫으면 실행 폴더의
`omok-settings.properties`에 저장됩니다. 다음 실행 때 같은 폴더에 있는 설정을 읽습니다.
`SettingsDialog.java`는 설정창, `GameSettings.java`는 설정 저장,
`ThemeManager.java`는 화면 모드를 담당합니다.
