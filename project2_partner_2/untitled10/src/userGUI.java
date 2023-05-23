import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class userGUI extends JFrame {
    private List<String> followedAuthors; // 关注列表

    public userGUI() {
        followedAuthors = new ArrayList<>();

        // 创建GUI组件
        JLabel authorLabel = new JLabel("作者:");
        JTextField authorField = new JTextField(20);
        JButton followButton = new JButton("关注");
        JButton unfollowButton = new JButton("取消关注");
        JButton showFollowedButton = new JButton("查看关注列表");

        // 设置布局
        setLayout(new FlowLayout());

        // 添加组件到窗口
        add(authorLabel);
        add(authorField);
        add(followButton);
        add(unfollowButton);
        add(showFollowedButton);

        // 添加关注按钮的事件处理
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String author = authorField.getText();
                followAuthor(author);
                authorField.setText(""); // 清空输入框
            }
        });

        // 添加取消关注按钮的事件处理
        unfollowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String author = authorField.getText();
                unfollowAuthor(author);
                authorField.setText(""); // 清空输入框
            }
        });

        // 添加查看关注列表按钮的事件处理
        showFollowedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFollowedAuthors();
            }
        });

        // 设置窗口属性
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setVisible(true);
    }

    // 关注作者
    private void followAuthor(String author) {
        followedAuthors.add(author);
        JOptionPane.showMessageDialog(this, "成功关注作者: " + author);
    }

    // 取消关注作者
    private void unfollowAuthor(String author) {
        if (followedAuthors.contains(author)) {
            followedAuthors.remove(author);
            JOptionPane.showMessageDialog(this, "成功取消关注作者: " + author);
        } else {
            JOptionPane.showMessageDialog(this, "未关注该作者: " + author);
        }
    }

    // 显示关注列表
    private void showFollowedAuthors() {
        StringBuilder sb = new StringBuilder();
        for (String author : followedAuthors) {
            sb.append(author).append("\n");
        }
        JOptionPane.showMessageDialog(this, "关注的作者列表:\n" + sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new userGUI();
            }
        });
    }
}
