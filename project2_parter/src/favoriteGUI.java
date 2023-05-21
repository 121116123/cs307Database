import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class favoriteGUI extends JFrame {
    private JTextField authornameField;
    private JTextField postidField;

    public favoriteGUI() {
        setTitle("favorite");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JLabel authornameLabel = new JLabel("authorname:");
        JLabel postidLabel = new JLabel("postID:");
        authornameField = new JTextField();
        postidField = new JTextField();
        inputPanel.add(authornameLabel);
        inputPanel.add(authornameField);
        inputPanel.add(postidLabel);
        inputPanel.add(postidField);

        JButton favoriteButton = new JButton("favorite");
        favoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                author_favorite_post();
            }
        });

        add(inputPanel, BorderLayout.CENTER);
        add(favoriteButton, BorderLayout.SOUTH);
    }

    private void author_favorite_post() {
        String authorname = authornameField.getText();
        int postID = Integer.parseInt(postidField.getText());

        // 调用方法注册新用户
        author_favorite_post.action_author_favorite_post(authorname, postID);


        JOptionPane.showMessageDialog(this, "favorite successful!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                favoriteGUI favoriteGUI = new favoriteGUI();
                favoriteGUI.setVisible(true);
            }
        });
    }
}
