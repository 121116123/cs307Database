import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class shareGUI extends JFrame {
    private JTextField authornameField;
    private JTextField postidField;

    public shareGUI() {
        setTitle("share");
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

        JButton shareButton = new JButton("share");
        shareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                author_share_post();
            }
        });

        add(inputPanel, BorderLayout.CENTER);
        add(shareButton, BorderLayout.SOUTH);
    }

    private void author_share_post() {
        String authorname = authornameField.getText();
        int postID = Integer.parseInt(postidField.getText());

        // 调用Author.registerAccount()方法注册新用户
        author_share_post.action_author_share_post(authorname, postID);

        // 可以在这里添加一些成功注册的提示信息或其他操作
        JOptionPane.showMessageDialog(this, "share successful!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                shareGUI shareGUI = new shareGUI();
                shareGUI.setVisible(true);
            }
        });
    }
}
