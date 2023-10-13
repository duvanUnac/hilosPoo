import javax.swing.*;
import java.awt.*;

public class VerticalBarGraphPanel extends JPanel {
    private int percentage;

    public VerticalBarGraphPanel() {
        this.percentage = 0;
    }

    public void setPercentage(int percentage) {
        if (percentage >= 0 && percentage <= 100) {
            this.percentage = percentage;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        int barWidth = (int) (width * 0.8);
        int barHeight = (int) (height * (percentage / 100.0));

        g.setColor(Color.BLUE);
        g.fillRect(width / 2 - barWidth / 2, height - barHeight, barWidth, barHeight);
    }
}
