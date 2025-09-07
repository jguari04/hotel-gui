import javax.swing.SwingUtilities;
import ui.MainWindow; // importa la ventana principal de tu proyecto

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
