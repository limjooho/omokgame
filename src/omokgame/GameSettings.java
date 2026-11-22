package omokgame; // 게임 전체에서 사용하는 설정을 모아 둔다.

import java.io.FileInputStream; // 저장한 설정 파일을 읽는다.
import java.io.FileOutputStream; // 변경한 설정을 파일에 쓴다.
import java.io.IOException; // 파일을 읽고 쓸 때의 오류를 처리한다.
import java.util.Properties; // 설정을 이름과 값으로 저장한다.

public class GameSettings { // 화면을 바꿔도 같은 설정을 사용한다.
    private static double soundVolume = 1.0; // 효과음 전체의 기본 크기는 100%다.
    private static boolean muted = false; // 처음에는 소리를 켠다.
    private static boolean darkMode = false; // 처음에는 밝은 화면을 사용한다.
    private static final String FILE_NAME = "omok-settings.properties"; // 실행 폴더에 저장할 파일 이름이다.

    public static double getSoundVolume() { // 현재 효과음 크기를 알려준다.
        return soundVolume; // 0부터 1 사이의 값을 반환한다.
    }

    public static void setSoundVolume(double value) { // 새 효과음 크기를 저장한다.
        soundVolume = Math.max(0, Math.min(1, value)); // 볼륨이 범위를 벗어나지 않게 한다.
    }

    public static boolean isMuted() { // 음소거 상태를 알려준다.
        return muted; // 음소거면 true를 반환한다.
    }

    public static void setMuted(boolean value) { // 음소거 상태를 바꾼다.
        muted = value; // 체크한 상태를 기억한다.
    }

    public static boolean isDarkMode() { // 다크 모드 상태를 알려준다.
        return darkMode; // 다크 모드면 true를 반환한다.
    }

    public static void setDarkMode(boolean value) { // 화면 모드를 바꾼다.
        darkMode = value; // 체크한 상태를 기억한다.
    }

    public static void load() { // 게임을 시작할 때 이전 설정을 읽는다.
        Properties settings = new Properties(); // 파일에서 읽을 값들을 준비한다.
        try (FileInputStream file = new FileInputStream(FILE_NAME)) { // 파일은 읽은 후 자동으로 닫는다.
            settings.load(file); // 저장된 이름과 값을 읽는다.
            double savedVolume = Double.parseDouble(settings.getProperty("volume", "1.0")); // 저장된 볼륨을 숫자로 바꾼다.
            if (Double.isFinite(savedVolume)) { // 올바른 숫자일 때만 반영한다.
                setSoundVolume(savedVolume); // 저장된 볼륨을 적용한다.
            }
            muted = Boolean.parseBoolean(settings.getProperty("muted", "false")); // 음소거 설정을 읽는다.
            darkMode = Boolean.parseBoolean(settings.getProperty("darkMode", "false")); // 화면 모드를 읽는다.
        } catch (java.io.FileNotFoundException error) { // 처음 실행해서 파일이 없을 수 있다.
            // 설정 파일이 없으면 기본값으로 시작한다.
        } catch (IOException | NumberFormatException error) { // 손상된 파일이나 숫자는 건너뛴다.
            System.err.println("설정을 읽지 못했습니다: " + error.getMessage()); // 원인을 콘솔에 표시한다.
        }
    }

    public static void save() { // 설정창을 닫을 때 현재 설정을 저장한다.
        Properties settings = new Properties(); // 저장할 이름과 값을 준비한다.
        settings.setProperty("volume", String.valueOf(soundVolume)); // 볼륨을 문자열로 넣는다.
        settings.setProperty("muted", String.valueOf(muted)); // 음소거 상태를 넣는다.
        settings.setProperty("darkMode", String.valueOf(darkMode)); // 화면 모드를 넣는다.
        try (FileOutputStream file = new FileOutputStream(FILE_NAME)) { // 파일은 저장한 후 자동으로 닫는다.
            settings.store(file, "OMOK settings"); // 다음 실행에서도 읽을 수 있게 저장한다.
        } catch (IOException error) { // 저장할 수 없는 폴더에서도 게임은 계속 실행한다.
            System.err.println("설정을 저장하지 못했습니다: " + error.getMessage()); // 원인을 콘솔에 표시한다.
        }
    }
}
