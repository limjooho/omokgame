package omokgame;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/** 실행할 때 한 번 재생하는 바둑판 착수 인트로. */
final class StartupIntro extends StackPane {
    private final Timeline animation;
    private final Runnable onFinished;
    private boolean finished;

    StartupIntro(Runnable onFinished) {
        this.onFinished = onFinished;
        getStyleClass().add("startup-intro");
        setPadding(new Insets(36));
        setFocusTraversable(true);
        setAccessibleText("오목 시작 애니메이션. 클릭하거나 Esc 키로 건너뛰기.");

        Label eyebrow = text("O M O K  /  3D dimension omok game", "intro-eyebrow");
        Label title = text("3D 오목", "intro-title");
        Label subtitle = text("3차원에서 즐기는 오목 게임", "intro-subtitle");
        VBox caption = new VBox(8, title, subtitle);
        caption.setAlignment(Pos.CENTER);

        // 15 x 15 교차점. 중앙 좌표 (230, 230)에 흑돌을 놓는다.
        Rectangle wood = new Rectangle(460, 460);
        wood.setArcWidth(32);
        wood.setArcHeight(32);
        wood.setStyle("-fx-fill: linear-gradient(to bottom right, #efd4a1, #d5a867);");
        wood.setStroke(Color.web("#c79959"));
        Group board = new Group(wood);
        for (int i = 0; i < 15; i++) {
            double coordinate = 34 + i * 28;
            Line horizontal = new Line(34, coordinate, 426, coordinate);
            Line vertical = new Line(coordinate, 34, coordinate, 426);
            horizontal.setStroke(Color.web("#765733", 0.55));
            vertical.setStroke(Color.web("#765733", 0.55));
            board.getChildren().addAll(horizontal, vertical);
        }
        for (int row : new int[]{3, 7, 11}) {
            for (int column : new int[]{3, 7, 11}) {
                board.getChildren().add(new Circle(
                        34 + column * 28, 34 + row * 28, 3, Color.web("#765733")));
            }
        }

        Ellipse shadow = new Ellipse(230, 237, 22, 9);
        shadow.setFill(Color.web("#352b23", 0.24));
        Circle ripple = new Circle(230, 230, 24, Color.TRANSPARENT);
        ripple.setStroke(Color.web("#fff9df"));
        ripple.setStrokeWidth(2);
        Circle stone = new Circle(230, 230, 21);
        stone.setFill(new RadialGradient(0, 0, 0.32, 0.24, 0.8, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#697079")),
                new Stop(0.35, Color.web("#30363e")),
                new Stop(1, Color.web("#10151b"))));
        stone.setEffect(new DropShadow(8, 2, 5, Color.web("#21190f", 0.4)));

        // Group의 바깥쪽 래퍼가 배율을 반영해 작은 창에서도 중앙에 배치한다.
        Group artwork = new Group(board, shadow, ripple, stone);
        StackPane boardSlot = new StackPane(new Group(artwork));
        boardSlot.setMinSize(0, 0);
        boardSlot.setPrefSize(500, 500);
        boardSlot.setMaxSize(500, 500);
        artwork.scaleXProperty().bind(Bindings.min(1,
                Bindings.min(boardSlot.widthProperty().divide(500),
                        boardSlot.heightProperty().divide(500))));
        artwork.scaleYProperty().bind(artwork.scaleXProperty());

        Button skip = new Button("건너뛰기 · Esc");
        skip.getStyleClass().add("intro-skip");
        skip.setOnAction(event -> finish());
        VBox layout = new VBox(20, eyebrow, boardSlot, caption, skip);
        layout.setAlignment(Pos.CENTER);
        getChildren().add(layout);

        board.setOpacity(0);
        board.setScaleX(0.96);
        board.setScaleY(0.96);
        shadow.setOpacity(0);
        ripple.setOpacity(0);
        stone.setOpacity(0);
        stone.setTranslateY(-150);
        stone.setScaleX(1.3);
        stone.setScaleY(1.3);
        caption.setOpacity(0);
        caption.setTranslateY(12);

        // 시간(밀리초)별 상태를 지정하면 JavaFX가 중간 프레임을 보간한다.
        animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(board.opacityProperty(), 0),
                        new KeyValue(board.scaleXProperty(), 0.96),
                        new KeyValue(board.scaleYProperty(), 0.96)),
                new KeyFrame(Duration.millis(500),
                        event -> SoundManager.PlaySoundEffect(SoundEffect.INTRO_SWISH), // 돌이 내려오기 시작할 때 짧은 바람 소리를 낸다.
                        new KeyValue(board.opacityProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(board.scaleXProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(board.scaleYProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(stone.opacityProperty(), 0),
                        new KeyValue(stone.translateYProperty(), -150),
                        new KeyValue(stone.scaleXProperty(), 1.3),
                        new KeyValue(stone.scaleYProperty(), 1.3),
                        new KeyValue(shadow.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(720),
                        new KeyValue(stone.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(1100),
                        event -> SoundManager.PlaySoundEffect(SoundEffect.INTRO_PLACE), // 돌이 바둑판에 닿는 순간 착수음을 낸다.
                        new KeyValue(stone.translateYProperty(), 0, Interpolator.EASE_IN),
                        new KeyValue(stone.scaleXProperty(), 1),
                        new KeyValue(stone.scaleYProperty(), 1),
                        new KeyValue(shadow.opacityProperty(), 1),
                        new KeyValue(ripple.opacityProperty(), 0),
                        new KeyValue(ripple.scaleXProperty(), 1),
                        new KeyValue(ripple.scaleYProperty(), 1)),
                new KeyFrame(Duration.millis(1180),
                        new KeyValue(stone.scaleXProperty(), 1.08),
                        new KeyValue(stone.scaleYProperty(), 0.92),
                        new KeyValue(ripple.opacityProperty(), 0.8)),
                new KeyFrame(Duration.millis(1300),
                        new KeyValue(stone.scaleXProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(stone.scaleYProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(caption.opacityProperty(), 0),
                        new KeyValue(caption.translateYProperty(), 12)),
                new KeyFrame(Duration.millis(1800),
                        new KeyValue(ripple.opacityProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(ripple.scaleXProperty(), 5, Interpolator.EASE_OUT),
                        new KeyValue(ripple.scaleYProperty(), 5, Interpolator.EASE_OUT),
                        new KeyValue(caption.opacityProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(caption.translateYProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.millis(3400), new KeyValue(opacityProperty(), 1)),
                new KeyFrame(Duration.millis(3750), new KeyValue(opacityProperty(), 0))
        );
        animation.setOnFinished(event -> finish());
        setOnMouseClicked(event -> {
            event.consume();
            finish();
        });
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE
                    || event.getCode() == KeyCode.SPACE
                    || event.getCode() == KeyCode.ENTER) {
                event.consume();
                finish();
            }
        });
    }

    void gamestartintro() { // 게임 최초 실행 시 인트로 애니메이션을 시작한다.
        if (!finished) {
            requestFocus();
            animation.playFromStart();
        }
    }

    private void finish() {
        if (finished) {
            return;
        }
        finished = true;
        animation.stop();
        setOnMouseClicked(null);
        setOnKeyPressed(null);
        SoundManager.PlaySoundEffect(SoundEffect.MENU_OPEN); // 인트로 종료와 건너뛰기에 같은 메뉴 전환음을 낸다.
        onFinished.run();
    }

    private Label text(String value, String styleClass) {
        Label label = new Label(value);
        label.getStyleClass().add(styleClass);
        return label;
    }
}
