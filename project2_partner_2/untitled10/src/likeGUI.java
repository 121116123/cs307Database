import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class likeGUI extends JFrame {
    private JTextField authornameField;
    private JTextField postidField;

    public likeGUI() {
        setTitle("like");
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

        JButton likeButton = new JButton("like");
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                author_like_post();
            }
        });

        add(inputPanel, BorderLayout.CENTER);
        add(likeButton, BorderLayout.SOUTH);
    }

    private void author_like_post() {
        String authorname = authornameField.getText();
        int postID = Integer.parseInt(postidField.getText());

        // 调用Author.registerAccount()方法注册新用户
        author_like_post.action_author_like_post(authorname, postID);

        // 可以在这里添加一些成功注册的提示信息或其他操作
        JOptionPane.showMessageDialog(this, "like successful!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                likeGUI likeGUI = new likeGUI();
                likeGUI.setVisible(true);
            }
        });
    }
}
