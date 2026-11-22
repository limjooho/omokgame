package omokgame; // 화면 전체의 밝고 어두운 스타일을 관리한다.

import javafx.scene.Parent; // 화면의 가장 바깥쪽 요소에 스타일을 적용한다.
import javafx.stage.Window; // 현재 열려 있는 창들을 확인한다.

public class ThemeManager { // 메인 화면과 여러 선택창에서 함께 사용한다.
    public static void apply(Parent root) { // 한 화면에 현재 설정을 적용한다.
        String css = ThemeManager.class.getResource("menu.css").toExternalForm(); // 공통 스타일 파일을 찾는다.
        if (!root.getStylesheets().contains(css)) { // 같은 스타일이 중복되는 것을 막는다.
            root.getStylesheets().add(css); // 화면에서 공통 스타일을 사용한다.
        }
        root.getStyleClass().remove("dark-mode"); // 이전의 다크 모드 표시를 지운다.
        if (GameSettings.isDarkMode()) { // 다크 모드가 켜져 있는지 확인한다.
            root.getStyleClass().add("dark-mode"); // 어두운 색상으로 표시한다.
        }
    }

    public static void changeTheme(boolean theme) { // true는 다크 모드, false는 밝은 모드로 바꾼다.
        GameSettings.setDarkMode(theme); // 전달받은 화면 모드를 저장한다.
        for (Window window : Window.getWindows()) { // 현재 표시 중인 창을 하나씩 확인한다.
            if (window.getScene() != null) { // 화면이 준비된 창인지 확인한다.
                apply(window.getScene().getRoot()); // 메인 화면과 설정창에 같은 모드를 적용한다.
            }
        }
    }
}
