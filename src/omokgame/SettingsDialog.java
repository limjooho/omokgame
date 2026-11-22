package omokgame; // 환경설정 창을 만드는 클래스다.

import javafx.geometry.Pos; // 글자와 버튼의 정렬 위치를 지정한다.
import javafx.scene.control.Button; // 소리 미리 듣기 버튼을 만든다.
import javafx.scene.control.ButtonBar; // 닫기 버튼의 역할을 지정한다.
import javafx.scene.control.ButtonType; // 설정창의 닫기 버튼을 만든다.
import javafx.scene.control.CheckBox; // 음소거와 다크 모드를 켜고 끈다.
import javafx.scene.control.Dialog; // 메인 화면 위에 설정창을 띄운다.
import javafx.scene.control.Label; // 제목과 설명을 표시한다.
import javafx.scene.control.Slider; // 마우스로 볼륨을 조절한다.
import javafx.scene.layout.HBox; // 같은 줄에 내용을 배치한다.
import javafx.scene.layout.Priority; // 슬라이더가 남은 가로 공간을 사용하게 한다.
import javafx.scene.layout.VBox; // 설정 항목을 위아래로 배치한다.
import javafx.stage.Modality; // 설정 중 메인 화면의 클릭을 막는다.
import javafx.stage.Window; // 설정창의 부모 창을 지정한다.

final class SettingsDialog extends Dialog<Void> { // 값을 선택하는 대신 설정을 바로 적용한다.
    SettingsDialog(Window owner) { // 메인 화면을 전달받아 설정창을 만든다.
        initOwner(owner); // 메인 화면 위에 설정창을 띄운다.
        initModality(Modality.WINDOW_MODAL); // 설정창이 열려 있는 동안 메인 화면 클릭을 막는다.
        setTitle("환경설정"); // 창 제목을 정한다.
        setResizable(false); // 설정 항목의 배치를 고정한다.
        getDialogPane().getStyleClass().add("settings-dialog"); // 설정창 전용 스타일을 적용한다.

        Label heading = label("환경설정", "settings-heading"); // 설정창의 제목이다.
        Label hint = label("내게 편한 소리와 화면으로", "settings-hint"); // 제목 아래 설명이다.
        Label soundHeading = label("효과음", "settings-section-title"); // 소리 설정의 제목이다.
        Label percent = label(Math.round(GameSettings.getSoundVolume() * 100) + "%", "settings-percent"); // 현재 볼륨을 표시한다.
        Slider volume = new Slider(0, 100, GameSettings.getSoundVolume() * 100); // 0부터 100까지 볼륨을 조절한다.
        volume.setId("sound-volume"); // 볼륨 슬라이더를 구분할 이름이다.
        volume.setAccessibleText("효과음 볼륨"); // 키보드와 화면 읽기 도구에 용도를 알려준다.
        volume.setBlockIncrement(5); // 키보드로 한 번에 5씩 조절한다.
        HBox volumeRow = new HBox(18, volume, percent); // 볼륨과 퍼센트를 같은 줄에 놓는다.
        volumeRow.setAlignment(Pos.CENTER_LEFT); // 가로줄을 가운데 높이로 맞춘다.
        HBox.setHgrow(volume, Priority.ALWAYS); // 슬라이더가 남은 너비를 사용한다.

        CheckBox mute = new CheckBox("효과음 음소거"); // 전체 효과음을 끄는 체크박스다.
        mute.setId("sound-mute"); // 음소거 항목을 구분할 이름이다.
        mute.setSelected(GameSettings.isMuted()); // 이전 음소거 상태를 표시한다.
        volume.setDisable(mute.isSelected()); // 음소거 중에는 기존 볼륨을 유지한다.
        Button preview = new Button("소리 미리 듣기"); // 설정한 크기로 소리를 확인한다.
        preview.getStyleClass().add("settings-preview"); // 미리 듣기 버튼의 모양을 정한다.
        preview.setDisable(mute.isSelected() || volume.getValue() == 0); // 소리가 꺼져 있으면 미리 듣기도 끈다.
        preview.setOnAction(event -> SoundManager.PlaySoundEffect(SoundEffect.STONE_PLACE)); // 착수음으로 볼륨을 확인한다.

        volume.valueProperty().addListener((observable, oldValue, newValue) -> { // 볼륨을 움직이면 즉시 적용한다.
            GameSettings.setSoundVolume(newValue.doubleValue() / 100); // 0부터 1 사이의 값으로 저장한다.
            percent.setText(Math.round(newValue.doubleValue()) + "%"); // 표시할 퍼센트도 바꾼다.
            SoundManager.stopAllSounds(); // 이전 볼륨으로 재생 중인 소리를 멈춘다.
            preview.setDisable(mute.isSelected() || newValue.doubleValue() == 0); // 무음이면 미리 듣기를 끈다.
        });
        mute.setOnAction(event -> { // 음소거 체크 상태가 바뀌면 실행한다.
            GameSettings.setMuted(mute.isSelected()); // 전체 음소거 상태를 저장한다.
            SoundManager.stopAllSounds(); // 이미 재생 중인 효과음도 즉시 멈춘다.
            volume.setDisable(mute.isSelected()); // 음소거 중에는 볼륨 조절을 잠근다.
            preview.setDisable(mute.isSelected() || volume.getValue() == 0); // 음소거 중에는 미리 듣기도 잠근다.
        });
        VBox soundSection = new VBox(16, soundHeading, volumeRow, mute, preview); // 소리 설정을 한 묶음으로 만든다.
        soundSection.getStyleClass().add("settings-section"); // 소리 영역을 카드 모양으로 표시한다.

        Label displayHeading = label("화면", "settings-section-title"); // 화면 설정의 제목이다.
        CheckBox dark = new CheckBox("다크 모드"); // 어두운 화면으로 바꾸는 체크박스다.
        dark.setId("dark-mode-toggle"); // 다크 모드 항목을 구분할 이름이다.
        dark.setSelected(GameSettings.isDarkMode()); // 이전 화면 설정을 표시한다.
        dark.setOnAction(event -> { // 다크 모드를 켜거나 끄면 실행한다.
            ThemeManager.changeTheme(dark.isSelected()); // 선택한 모드로 설정창과 메인 화면을 즉시 바꾼다.
            SoundManager.PlaySoundEffect(SoundEffect.BUTTON_CLICK); // 현재 음량으로 변경을 알려준다.
        });
        Label displayHint = label("메인 화면과 선택창을 어둡게 표시합니다.", "settings-hint"); // 적용 범위를 설명한다.
        VBox displaySection = new VBox(14, displayHeading, dark, displayHint); // 화면 설정을 한 묶음으로 만든다.
        displaySection.getStyleClass().add("settings-section"); // 화면 영역을 카드 모양으로 표시한다.
        VBox content = new VBox(14, heading, hint, soundSection, displaySection); // 두 설정 영역을 세로로 배치한다.
        content.getStyleClass().add("settings-content"); // 설정창의 여백을 지정한다.
        content.setPrefWidth(420); // 창의 기본 너비를 정한다.
        getDialogPane().setContent(content); // 완성한 내용을 창에 넣는다.

        ButtonType close = new ButtonType("닫기", ButtonBar.ButtonData.CANCEL_CLOSE); // 닫기와 Esc로 창을 닫는다.
        getDialogPane().getButtonTypes().add(close); // 창 아래에 닫기 버튼을 넣는다.
        getDialogPane().lookupButton(close).getStyleClass().add("difficulty-cancel"); // 기존 취소 버튼 스타일을 사용한다.
        setResultConverter(button -> null); // 설정은 즉시 적용되므로 별도 반환값이 없다.
        setOnHidden(event -> { // 닫기, Esc, X 모두 같은 저장 처리를 한다.
            GameSettings.save(); // 다음 실행에서도 같은 설정을 사용한다.
            SoundManager.PlaySoundEffect(SoundEffect.CANCEL); // 음소거 상태가 아니면 닫기 소리를 낸다.
        });
        ThemeManager.apply(getDialogPane()); // 현재 화면 모드를 설정창에도 적용한다.
    }

    private Label label(String text, String styleClass) { // 같은 방식으로 글자를 만드는 보조 메소드다.
        Label label = new Label(text); // 표시할 글자를 만든다.
        label.getStyleClass().add(styleClass); // 글자의 색과 크기를 정한다.
        return label; // 완성한 글자를 반환한다.
    }
}
