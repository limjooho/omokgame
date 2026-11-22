package omokgame; // 오목 게임에서 사용하는 사운드 종류를 모아 둔다.

public enum SoundEffect { // 정해진 이름으로 효과음을 구분한다.
    BUTTON_CLICK, // 버튼을 눌렀을 때의 소리다.
    INTRO_PLACE, // 인트로에서 돌이 내려앉을 때의 소리다.
    STONE_PLACE, // 게임 중 돌을 놓았을 때의 소리다.
    VICTORY, // 사용자가 이겼을 때의 소리다.
    DEFEAT, // 사용자가 졌을 때의 소리다.
    INTRO_SWISH, // 인트로의 돌이 내려오기 시작할 때의 소리다.
    MENU_OPEN, // 인트로가 끝나고 메뉴가 나타날 때의 소리다.
    SELECT_NORMAL, // 보통 난이도를 선택했을 때의 소리다.
    SELECT_HARD, // 어려움 난이도를 선택했을 때의 소리다.
    CANCEL // 선택을 취소하거나 이전 화면으로 돌아갈 때의 소리다.
}
