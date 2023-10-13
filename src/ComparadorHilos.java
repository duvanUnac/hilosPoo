import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComparadorHilos extends JFrame {
    private VerticalBarGraphPanel graphPanelFutbol;
    private VerticalBarGraphPanel graphPanelBasketball;
    private JTextField txtFutbol;
    private JTextField txtBasketball;
    private JLabel timeLabel;

    private ArrayList<Integer> futbolData;
    private ArrayList<Integer> basketballData;

    public ComparadorHilos() {
        super("Comparador de Intereses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        graphPanelFutbol = new VerticalBarGraphPanel();
        graphPanelBasketball = new VerticalBarGraphPanel();

        // Etiqueta y campo de texto para el fútbol
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("Hombres que les gusta el fútbol:"), gbc);
        gbc.gridy++;
        txtFutbol = new JTextField(5);
        mainPanel.add(txtFutbol, gbc);

        // Etiqueta y campo de texto para el baloncesto
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Hombres que les gusta el baloncesto:"), gbc);
        gbc.gridy++;
        txtBasketball = new JTextField(5);
        mainPanel.add(txtBasketball, gbc);

        // Botón de comparación
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        JButton compararButton = new JButton("Comparar");
        compararButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compararPorcentajes();
            }
        });
        mainPanel.add(compararButton, gbc);

        // Barras de progreso
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(graphPanelFutbol, gbc);

        gbc.gridx = 2;
        mainPanel.add(graphPanelBasketball, gbc);

        // Etiqueta para mostrar la hora
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        timeLabel = new JLabel("Hora actual: ");
        mainPanel.add(timeLabel, gbc);

        getContentPane().add(mainPanel);

        futbolData = new ArrayList<>();
        basketballData = new ArrayList<>();

        // Iniciar un hilo para mostrar la hora
        Thread timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf.format(new Date());
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            timeLabel.setText("Hora actual: " + currentTime);
                        }
                    });
                    try {
                        Thread.sleep(1000); // Actualizar cada segundo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timeThread.start();

        // Iniciar un hilo para generar gráficas cada 3 segundos
        Thread graphUpdateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            graphPanelFutbol.setPercentage(getAverage(futbolData));
                            graphPanelBasketball.setPercentage(getAverage(basketballData));
                        }
                    });
                    try {
                        Thread.sleep(3000); // Generar gráficas cada 3 segundos
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        graphUpdateThread.start();
    }

    private void compararPorcentajes() {
        try {
            int porcentajeFutbol = Integer.parseInt(txtFutbol.getText());
            int porcentajeBasketball = Integer.parseInt(txtBasketball.getText());

            futbolData.add(porcentajeFutbol);
            basketballData.add(porcentajeBasketball);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getAverage(ArrayList<Integer> data) {
        if (data.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (int value : data) {
            sum += value;
        }
        return sum / data.size();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ComparadorHilos comparador = new ComparadorHilos();
                comparador.setVisible(true);
            }
        });
    }
}


