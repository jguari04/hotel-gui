import javax.swing.SwingUtilities;
import ui.MainWindow; // importa tu ventana principal

public class Main {
    public static void main(String[] args) {
        // Lanzar la UI en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}
