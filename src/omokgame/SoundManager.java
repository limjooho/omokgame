package omokgame; // 오목 게임의 공통 사운드를 관리한다.

import java.net.URL; // 사운드 파일의 위치를 저장할 때 사용한다.
import javafx.scene.media.AudioClip; // 짧은 효과음을 재생할 때 사용한다.

public class SoundManager { // 여러 화면에서 같은 재생 메소드를 사용한다.
    private static AudioClip[] sounds = new AudioClip[10]; // 읽어 둔 효과음 열 개를 보관한다.

    public static void PlaySoundEffect(SoundEffect effect) { // 전달받은 종류에 맞는 효과음을 재생한다.
        if (effect == null || GameSettings.isMuted() || GameSettings.getSoundVolume() == 0) { // 무음 설정이면 모든 효과음을 건너뛴다.
            return; // 메소드를 끝낸다.
        }

        int number; // 효과음을 보관할 배열 번호다.
        String fileName; // 재생할 사운드 파일 이름이다.
        double volume = 0.65; // 기본 볼륨을 정하고 필요한 소리만 따로 조절한다.

        switch (effect) { // 전달받은 사운드 종류를 확인한다.
            case BUTTON_CLICK: // 버튼 클릭 소리를 고른다.
                number = 0; // 배열의 첫 번째 자리를 사용한다.
                fileName = "button-click.wav"; // 버튼 클릭 파일이다.
                break; // 다른 경우는 확인하지 않는다.
            case INTRO_PLACE: // 인트로 착수 소리를 고른다.
                number = 1; // 배열의 두 번째 자리를 사용한다.
                fileName = "intro-place.wav"; // 인트로 착수 파일이다.
                volume = 0.8; // 인트로 착수는 조금 더 묵직하게 들려준다.
                break; // 다른 경우는 확인하지 않는다.
            case STONE_PLACE: // 게임 중 착수 소리를 고른다.
                number = 2; // 배열의 세 번째 자리를 사용한다.
                fileName = "stone-place.wav"; // 게임 중 착수 파일이다.
                break; // 다른 경우는 확인하지 않는다.
            case VICTORY: // 승리 소리를 고른다.
                number = 3; // 배열의 네 번째 자리를 사용한다.
                fileName = "victory.wav"; // 승리 파일이다.
                break; // 다른 경우는 확인하지 않는다.
            case DEFEAT: // 패배 소리를 고른다.
                number = 4; // 배열의 다섯 번째 자리를 사용한다.
                fileName = "defeat.wav"; // 패배 파일이다.
                break; // 다른 경우는 확인하지 않는다.
            case INTRO_SWISH: // 돌이 내려오는 바람 소리를 고른다.
                number = 5; // 배열의 여섯 번째 자리를 사용한다.
                fileName = "intro-swish.wav"; // 인트로의 짧은 바람 소리다.
                volume = 0.35; // 착수음보다 작게 재생한다.
                break; // 다른 경우는 확인하지 않는다.
            case MENU_OPEN: // 메뉴가 나타나는 소리를 고른다.
                number = 6; // 배열의 일곱 번째 자리를 사용한다.
                fileName = "menu-open.wav"; // 메뉴 전환을 알리는 맑은 소리다.
                volume = 0.5; // 메뉴에서는 부드럽게 재생한다.
                break; // 다른 경우는 확인하지 않는다.
            case SELECT_NORMAL: // 보통 난이도 선택 소리를 고른다.
                number = 7; // 배열의 여덟 번째 자리를 사용한다.
                fileName = "select-normal.wav"; // 보통 카드에 어울리는 밝은 소리다.
                break; // 다른 경우는 확인하지 않는다.
            case SELECT_HARD: // 어려움 난이도 선택 소리를 고른다.
                number = 8; // 배열의 아홉 번째 자리를 사용한다.
                fileName = "select-hard.wav"; // 어려움 카드에 어울리는 낮고 강한 소리다.
                break; // 다른 경우는 확인하지 않는다.
            case CANCEL: // 취소 소리를 고른다.
                number = 9; // 배열의 열 번째 자리를 사용한다.
                fileName = "cancel.wav"; // 돌아가는 느낌의 짧은 소리다.
                volume = 0.45; // 취소 소리는 작게 재생한다.
                break; // 다른 경우는 확인하지 않는다.
            default: // 처리할 수 없는 종류라면 재생하지 않는다.
                return; // 메소드를 끝낸다.
        }

        try { // 사운드를 읽거나 재생하다 문제가 생기는지 확인한다.
            if (effect == SoundEffect.MENU_OPEN) { // 건너뛰거나 메뉴로 넘어오면 인트로 소리를 정리한다.
                if (sounds[1] != null) { // 인트로 착수음을 읽은 적이 있는지 확인한다.
                    sounds[1].stop(); // 남아 있는 착수음의 잔향을 멈춘다.
                }
                if (sounds[5] != null) { // 인트로 바람 소리를 읽은 적이 있는지 확인한다.
                    sounds[5].stop(); // 건너뛴 인트로의 바람 소리를 멈춘다.
                }
            }
            if (sounds[number] == null) { // 아직 읽지 않은 효과음인지 확인한다.
                URL soundFile = SoundManager.class.getResource("assets/sounds/" + fileName); // 파일 위치를 찾는다.
                if (soundFile == null) { // 파일이 없으면 원인을 출력한다.
                    System.err.println("사운드 파일을 찾을 수 없습니다: " + fileName); // 빠진 파일을 알려준다.
                    return; // 게임은 계속 실행하고 소리만 건너뛴다.
                }
                sounds[number] = new AudioClip(soundFile.toExternalForm()); // 파일을 읽고 다음 재생을 위해 보관한다.
            }
            sounds[number].play(volume * GameSettings.getSoundVolume()); // 효과음 기본 크기에 사용자 볼륨을 곱한다.
        } catch (RuntimeException error) { // 사운드 문제 때문에 게임이 종료되는 것을 막는다.
            System.err.println("사운드 재생 실패: " + fileName + " / " + error.getMessage()); // 실패 이유를 출력한다.
        }
    }

    public static void stopAllSounds() { // 음소거와 볼륨 변경을 즉시 반영한다.
        for (int i = 0; i < sounds.length; i++) { // 읽어 둔 효과음을 하나씩 확인한다.
            if (sounds[i] != null) { // 실제로 준비된 효과음만 처리한다.
                sounds[i].stop(); // 현재 재생 중인 소리를 멈춘다.
            }
        }
    }
}
