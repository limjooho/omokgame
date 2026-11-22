package omokgame;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;

// 얼굴의 색과 표정으로 난이도를 구분하는 선택창이다.
final class AiDifficultyDialog extends Dialog<String> {
    AiDifficultyDialog(Window owner, String previousDifficulty) {
        initOwner(owner); // 메인 화면 위에 창을 띄운다.
        initModality(Modality.WINDOW_MODAL); // 선택창이 열린 동안 메인 화면의 클릭을 막는다.
        setTitle("AI 대전 · 난이도 선택"); // 창 제목을 정한다.
        setResizable(false); // 두 카드를 같은 크기로 유지한다.
        getDialogPane().getStyleClass().add("difficulty-dialog"); // 선택창 전용 스타일을 적용한다.
        getDialogPane().getStylesheets().add(java.util.Objects.requireNonNull(
                getClass().getResource("menu.css")).toExternalForm()); // 메인 화면과 같은 CSS를 사용한다.
        ThemeManager.apply(getDialogPane()); // 다크 모드 설정을 난이도 선택창에도 적용한다.

        Label eyebrow = label("CHOOSE YOUR CHALLENGE", "difficulty-eyebrow"); // 작은 안내 문구다.
        Label title = label("어떤 상대에게 도전할까요?", "difficulty-heading"); // 선택창의 큰 제목이다.
        Label hint = label("얼굴 카드를 눌러 난이도를 선택하세요.", "difficulty-hint"); // 선택 방법을 알려준다.
        Button normal = createCard("보통", "차분하게 한 수씩", false); // 초록색 얼굴의 보통 버튼이다.
        Button hard = createCard("어려움", "더 치열한 한 판", true); // 붉은 얼굴의 어려움 버튼이다.
        HBox cards = new HBox(20, normal, hard); // 두 버튼을 같은 가로줄에 배치한다.
        cards.setAlignment(Pos.CENTER); // 두 카드를 가운데로 정렬한다.
        VBox content = new VBox(12, eyebrow, title, hint, cards); // 제목 아래에 카드들을 넣는다.
        content.setAlignment(Pos.CENTER); // 창의 내용을 가운데로 정렬한다.
        content.getStyleClass().add("difficulty-content"); // 내용 영역의 여백을 지정한다.
        getDialogPane().setContent(content); // 완성한 화면을 선택창에 넣는다.

        ButtonType cancel = new ButtonType("취소", ButtonBar.ButtonData.CANCEL_CLOSE); // 취소와 Esc로 창을 닫는다.
        getDialogPane().getButtonTypes().add(cancel); // 아래쪽에 취소 버튼을 넣는다.
        getDialogPane().lookupButton(cancel).getStyleClass().add("difficulty-cancel"); // 취소 버튼의 모양을 정한다.
        setResultConverter(button -> null); // 취소하거나 X를 누르면 난이도를 반환하지 않는다.
        setOnCloseRequest(event -> { // 버튼, Esc, X로 창을 닫는 경우를 모두 확인한다.
            if (getResult() == null) { // 난이도를 선택하지 않고 닫은 경우다.
                SoundManager.PlaySoundEffect(SoundEffect.CANCEL); // 취소음을 한 번만 재생한다.
            }
        });
        setOnShown(event -> { // 창이 열리면 마지막에 선택했던 버튼에 초점을 준다.
            if ("어려움".equals(previousDifficulty)) { // 이전 난이도가 어려움인지 확인한다.
                hard.requestFocus(); // 어려움 카드를 키보드로 바로 선택할 수 있다.
            } else {
                normal.requestFocus(); // 처음에는 보통 카드에 초점을 준다.
            }
        });
    }

    private Button createCard(String difficulty, String description, boolean hard) {
        Button card = new Button(); // 카드 전체를 누를 수 있는 버튼으로 만든다.
        card.getStyleClass().addAll("difficulty-card", hard ? "difficulty-hard" : "difficulty-normal"); // 난이도별 색을 정한다.
        card.setPrefSize(250, 310); // 두 카드의 크기를 같게 정한다.
        card.setMinSize(250, 310); // 내용 때문에 카드가 작아지는 것을 막는다.
        card.setAccessibleText(difficulty + " 난이도 선택. " + description); // 얼굴을 보지 않아도 버튼의 역할을 알 수 있다.
        String asset = hard ? "difficulty-hard.png" : "difficulty-normal.png"; // 난이도에 맞는 얼굴 파일을 고른다.
        Image image = new Image(java.util.Objects.requireNonNull(
                getClass().getResource("assets/" + asset), "얼굴 이미지 누락: " + asset).toExternalForm()); // 얼굴 이미지를 읽는다.
        ImageView faceImage = new ImageView(image); // 이미지를 화면에 표시할 준비를 한다.
        faceImage.setFitWidth(160); // 얼굴의 가로 표시 크기를 정한다.
        faceImage.setFitHeight(160); // 얼굴의 세로 표시 크기를 정한다.
        faceImage.setPreserveRatio(true); // 이미지의 가로세로 비율을 유지한다.
        faceImage.setSmooth(true); // 크기를 줄여도 부드럽게 표시한다.
        Label name = label(difficulty, "difficulty-name"); // 보통 또는 어려움을 표시한다.
        Label detail = label(description, "difficulty-description"); // 난이도 설명을 표시한다.
        Label action = label("선택하기  →", "difficulty-action"); // 카드를 누를 수 있다는 것을 알려준다.
        VBox graphic = new VBox(10, faceImage, name, detail, action); // 얼굴 이미지를 설명과 함께 바로 배치한다.
        graphic.setAlignment(Pos.CENTER); // 카드 안의 내용을 가운데로 맞춘다.
        graphic.setMouseTransparent(true); // 얼굴을 눌러도 카드 버튼이 클릭되게 한다.
        card.setGraphic(graphic); // 완성한 내용을 버튼에 넣는다.
        card.setOnAction(event -> { // 카드 하나를 누르면 바로 선택한다.
            if (hard) { // 어려움 카드인지 확인한다.
                SoundManager.PlaySoundEffect(SoundEffect.SELECT_HARD); // 낮고 강한 선택음을 낸다.
            } else {
                SoundManager.PlaySoundEffect(SoundEffect.SELECT_NORMAL); // 밝고 부드러운 선택음을 낸다.
            }
            setResult(difficulty); // 누른 카드의 난이도를 결과로 저장한다.
            close(); // 선택창을 닫고 호출한 메서드로 돌아간다.
        });
        return card; // 완성된 카드 버튼을 반환한다.
    }

    private Label label(String text, String styleClass) {
        Label label = new Label(text); // 표시할 글자를 만든다.
        label.getStyleClass().add(styleClass); // 글자의 색과 크기를 지정한다.
        return label; // 완성된 글자를 반환한다.
    }
}
