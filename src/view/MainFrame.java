package view;

import comparer.ImageComparer;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        initFrame();
    }

    private void initFrame() {

        Container contentPane = getContentPane();
        GroupLayout groupLayout = new GroupLayout(contentPane);
        contentPane.setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);

        JLabel jLabel = new JLabel();
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setBorder(new LineBorder(new Color(102, 102, 102), 1, true));

        JButton selectImage = new JButton("Select images");

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(selectImage)
                .addComponent(jLabel)
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(selectImage)
                .addComponent(jLabel)
        );

        setTitle("Select images to compare");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        selectImage.addActionListener((ActionEvent event) -> {
            JFileChooser fileChooser = new JFileChooser(" Select the first file");
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
                JOptionPane.showMessageDialog( null, "This is first selected file \n" + fileChooser.getSelectedFile().getAbsolutePath() + "\n Select the second file", "Info", JOptionPane.DEFAULT_OPTION );
                ImageComparer.setPath(fileChooser.getSelectedFile().getAbsolutePath());
                fileChooser.setToolTipText(" Select the second file");
                if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
                    ImageComparer.setPathForComparison(fileChooser.getSelectedFile().getAbsolutePath());
                    String msg = ImageComparer.compare();
                    if (!msg.equals("")) {
                        JOptionPane.showMessageDialog( null, msg, "Error Message", JOptionPane.DEFAULT_OPTION );
                    }
                } else {
                    JOptionPane.showMessageDialog( null, "There are no files to compare. Try to select the files", "Info", JOptionPane.DEFAULT_OPTION );
                }
            }

            if (ImageComparer.resultingBufferedImage != null) {
                jLabel.setIcon(new ImageIcon(ImageComparer.resultingBufferedImage));
                setSize(ImageComparer.resultingBufferedImage.getWidth() + 200, ImageComparer.resultingBufferedImage.getHeight() + 100);
            }
        });
    }

    public static void main(String[] args) {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
    }

}
