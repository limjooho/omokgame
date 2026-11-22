
package omokgame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent; // 마우스나 키보드로 버튼을 실행하는 이벤트다.
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip; // 톱니바퀴 버튼에 설명을 표시한다.
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class omok extends Application {

    private Stage window;
    private String selectedAiDifficulty = "보통"; // 처음 난이도는 보통으로 설정한다.

    @Override
    public void start(Stage stage) {
        window = stage;
        GameSettings.load(); // 이전에 저장한 음량과 화면 모드를 읽는다.

        Label brand = label("●  O M O K", "brand");
        Label edition = label("PLAY IN A NEW DIMENSION", "edition");

        Region headerSpace = new Region();
        HBox.setHgrow(headerSpace, Priority.ALWAYS);

        HBox header = new HBox(brand, headerSpace, edition);
        header.setAlignment(Pos.CENTER_LEFT);

        Label eyebrow = label("한 수의 즐거움, 새로운 차원으로", "eyebrow");
        Label title = label("3D 오목", "title");
        Label subtitle = label(
                "친구와 함께, 또는 AI와 함께. 오늘의 한 수를 시작해 보세요.",
                "subtitle");

        VBox intro = new VBox(8, eyebrow, title, subtitle);
        intro.setAlignment(Pos.CENTER);

        // 24. 3D 게임 시작 버튼
        Button localGame = createModeCard(
                "3D 오목",
                "친구와 마주하는 한 판",
                "LOCAL · 2인 대전",
                "함께 시작하기",
                "gomoku-board.png",
                "mint");

        // 25. AI 게임 시작 버튼
        Button aiGame = createModeCard(
                "AI 대전",
                "나만의 전략으로 AI에 도전하세요",
                "SOLO · AI 대전",
                "AI와 시작하기",
                "ai-robot.png",
                "lavender");

        // 각 버튼에 메서드 연결
        //3D 오목 버튼 클릭시 game3DstartButton 메소드 실행
        localGame.setOnAction(event -> game3DstartButton());
        //AI 대전 버튼 클릭시 AIGamestartButton 메소드 실행
        aiGame.setOnAction(event -> AIGamestartButton());

        GridPane modes = new GridPane();
        modes.setHgap(24);

        for (int i = 0; i < 2; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(50);
            column.setHgrow(Priority.ALWAYS);
            modes.getColumnConstraints().add(column);
        }

        modes.add(localGame, 0, 0);
        modes.add(aiGame, 1, 0);

        // 27. 전적 확인 버튼
        Button records = new Button("전적 확인  ↗");
        records.getStyleClass().addAll("utility", "records");
        //전적 확인 버튼 클릭시 openHistoryButton 메소드 실행
        records.setOnAction(event -> openHistoryButton());

        // 26. 게임 종료 버튼
        Button exit = new Button("게임 종료");
        exit.getStyleClass().addAll("utility", "exit");
        //게임 종료 버튼 클릭시 EndGameButton 메소드 실행
        exit.setOnAction(event -> EndGameButton());

        HBox actions = new HBox(16, records, exit);
        actions.setAlignment(Pos.CENTER);

        Label footer = label("흑과 백 사이, 무한한 가능성", "footer");

        VBox content = new VBox(
                20, header, intro, modes, actions, footer);

        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(28, 48, 24, 48));
        content.setMaxWidth(1100);

        StackPane root = new StackPane(content);
        root.getStyleClass().add("app");
        Button settings = new Button("\u2699"); // 작은 톱니바퀴 모양의 환경설정 버튼이다.
        settings.getStyleClass().add("settings-button"); // 톱니바퀴의 색과 크기를 정한다.
        settings.setId("open-settings"); // 환경설정 버튼을 구분할 이름이다.
        settings.setAccessibleText("환경설정"); // 아이콘의 뜻을 알려준다.
        settings.setTooltip(new Tooltip("환경설정")); // 마우스를 올리면 설명을 보여준다.
        settings.setMaxSize(44, 44); // 버튼이 화면을 채우지 않게 한다.
        settings.setVisible(false); // 인트로 중에는 설정 버튼을 숨긴다.
        settings.setOnAction(event -> gameoption()); // 환경설정 메소드를 실행한다.
        StackPane.setAlignment(settings, Pos.BOTTOM_LEFT); // 화면의 왼쪽 아래에 고정한다.
        StackPane.setMargin(settings, new Insets(0, 0, 18, 18)); // 화면 끝에서 조금 떨어뜨린다.
        root.getChildren().add(settings); // 메뉴 위에 작은 버튼을 올린다.

        // 인트로가 끝나거나 건너뛰면 기존의 네 버튼 메뉴를 표시한다.
        content.setOpacity(0);
        content.setDisable(true);
        StartupIntro startupIntro = new StartupIntro(() -> {
            root.getChildren().removeIf(node -> node instanceof StartupIntro);
            content.setDisable(false);
            settings.setVisible(true); // 메뉴가 나타날 때 설정 버튼도 보여준다.
            FadeTransition revealMenu = new FadeTransition(Duration.millis(300), content);
            revealMenu.setToValue(1);
            revealMenu.setOnFinished(event -> localGame.requestFocus());
            revealMenu.play();
        });
        root.getChildren().add(startupIntro);

        Scene scene = new Scene(root, 1080, 860);
        scene.addEventFilter(ActionEvent.ACTION, event -> { // 메인 화면과 인트로의 버튼 실행을 확인한다.
            if (event.getTarget() instanceof Button) { // 실제 버튼이 실행된 경우에만 소리를 낸다.
                Button clicked = (Button) event.getTarget(); // 실행된 버튼을 확인한다.
                if (!clicked.getStyleClass().contains("intro-skip")) { // 건너뛰기는 메뉴 전환음만 사용한다.
                    SoundManager.PlaySoundEffect(SoundEffect.BUTTON_CLICK); // 일반 버튼에는 클릭음을 재생한다.
                }
            }
        });

        ThemeManager.apply(root); // 저장한 화면 모드와 공통 스타일을 적용한다.

        stage.setTitle("3D 오목");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(880);

        stage.setOnCloseRequest(event -> {
            event.consume();
            EndGameButton();
        });

        stage.show();
        startupIntro.gamestartintro(); // 게임 최초 실행 시 인트로를 시작한다.
    }

    // 라벨 생성 메서드
    private Label label(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    // 게임 모드 카드 생성 메서드
    private Button createModeCard(
            String title, String description,
            String badge, String action,
            String asset, String color) {

        Button button = new Button();
        button.getStyleClass().addAll("mode-card", color);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAccessibleText(
                title + ". " + description + ". " + action);

        Image image = new Image(
                java.util.Objects.requireNonNull(
                        getClass().getResource("assets/" + asset),
                        "이미지 리소스 누락: " + asset
                ).toExternalForm());

        ImageView art = new ImageView(image);
        art.setPreserveRatio(true);
        art.setSmooth(true);
        art.setFitHeight(180);

        art.fitWidthProperty().bind(
                Bindings.min(
                        360,
                        button.widthProperty().subtract(64)
                ));

        StackPane illustration = new StackPane(art);
        illustration.setMinHeight(185);
        illustration.setPrefHeight(185);
        illustration.setMaxHeight(185);

        Label tag = label(badge, "mode-badge");
        Label name = label(title, "mode-title");
        Label detail = label(description, "mode-description");

        detail.setWrapText(true);
        detail.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER);

        Label cta = label(action + "  →", "mode-action");
        cta.setMaxWidth(Double.MAX_VALUE);
        cta.setAlignment(Pos.CENTER);

        VBox graphic = new VBox(
                8, tag, illustration, name, detail, cta);

        graphic.setAlignment(Pos.CENTER);
        graphic.prefWidthProperty().bind(
                button.widthProperty().subtract(48));

        graphic.setMouseTransparent(true);
        button.setGraphic(graphic);

        return button;
    }

    private void gameoption() { // 환경설정 창을 여는 메소드다.
        SettingsDialog dialog = new SettingsDialog(window); // 환경설정 창을 만든다.
        dialog.showAndWait(); // 설정창을 띄우고 닫을 때까지 기다린다.
    }

    // 24. 3D 게임 시작 버튼 클릭 메서드 (자바에서는 메소드 이름을 숫자로 시작할 수 없다.)
    private void game3DstartButton() {
        showMessage(
                "3D 오목",
                "3D 게임 시작",
                "2인 대전 모드를 선택했습니다."
        );

        // 추후 3D 게임 화면 연결
    }

    // 25. AI전 게임 시작 버튼 클릭 메서드
    private void AIGamestartButton() {
        AiDifficultyDialog dialog = new AiDifficultyDialog(window, selectedAiDifficulty); // 얼굴 카드 두 개가 있는 창을 만든다.
        dialog.showAndWait().ifPresent(difficulty -> { // 선택을 확정했을 때만 실행한다.
            selectedAiDifficulty = difficulty; // 확정한 난이도를 저장한다.
            showMessage("AI 대전", "난이도 선택 완료", selectedAiDifficulty + " 난이도를 선택했습니다."); // 선택 결과를 알려준다.
            // 추후 이 위치에서 선택한 난이도로 AI 게임 화면을 연다.
        });
    }

    // 26. 게임 종료 버튼 클릭 메서드
    private void EndGameButton() {
        ButtonType quit = new ButtonType("종료");

        ButtonType cancel = new ButtonType(
                "취소",
                javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE
        );

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "게임을 종료하시겠습니까?",
                quit,
                cancel
        );

        alert.initOwner(window);
        alert.setTitle("게임 종료");
        alert.setHeaderText(null);
        ThemeManager.apply(alert.getDialogPane()); // 종료 확인창에도 현재 화면 모드를 적용한다.
        alert.getDialogPane().addEventFilter(ActionEvent.ACTION, event -> { // 종료와 취소 버튼의 실행을 확인한다.
            if (event.getTarget() instanceof Button) { // 버튼 실행인지 확인한다.
                Button clicked = (Button) event.getTarget(); // 종료 확인창에서 누른 버튼을 확인한다.
                if (clicked.isCancelButton()) { // 취소 버튼인지 확인한다.
                    SoundManager.PlaySoundEffect(SoundEffect.CANCEL); // 취소할 때는 돌아가는 소리를 낸다.
                } else {
                    SoundManager.PlaySoundEffect(SoundEffect.BUTTON_CLICK); // 종료를 선택하면 클릭음을 낸다.
                }
            }
        });

        alert.showAndWait()
                .filter(quit::equals)
                .ifPresent(answer -> Platform.exit());
    }

    // 27. 전적 확인 버튼 클릭 메서드
    private void openHistoryButton() {
        showMessage(
                "전적 확인",
                "아직 저장된 전적이 없습니다.",
                "게임 전적 저장 기능을 연결하면 "
                + "승리·패배 기록을 표시합니다."
        );

        // 추후 전적 확인 화면 연결
    }

    // 공통 알림창 메서드
    private void showMessage(
            String title, String header, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(window);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        ThemeManager.apply(alert.getDialogPane()); // 안내창에도 현재 화면 모드를 적용한다.
        alert.getDialogPane().addEventFilter(ActionEvent.ACTION, event -> { // 안내창의 확인 버튼 실행을 확인한다.
            if (event.getTarget() instanceof Button) { // 버튼 실행인지 확인한다.
                SoundManager.PlaySoundEffect(SoundEffect.BUTTON_CLICK); // 안내창에서도 클릭음을 재생한다.
            }
        });
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
