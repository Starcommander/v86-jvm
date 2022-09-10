import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.Border;

/**
 *
 * This class demonstrates the simple hello world swing program.
 * @Ramesh Fadatare
 */
public class HelloWorldSwing {

static int count = 0;

    private static void createAndShowGUI() {
        JFrame jFrame = new JFrame("Hello World Swing Example");
        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(500, 360);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel label = new JLabel("Hello World Swing");
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        label.setBorder(border);
        label.setPreferredSize(new Dimension(150, 100));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        jFrame.add(label);
        JButton button = new JButton("Is OK.");
        button.addActionListener(e -> {label.setText("ClickCounter: " + count); count++;});
        jFrame.add(button);

        String cbList[] = {"Vienna", "Berlin", "London"};
        JComboBox cbSelect = new JComboBox(cbList);
        jFrame.add(cbSelect);

        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }
}
