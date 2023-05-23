import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class listfavoriteGUI extends JFrame {
    private JTextField userIdField;
    private JTextArea resultArea;
    public static Connection con = null;

    public listfavoriteGUI() {
        setTitle("用户点赞帖子列表");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 用户name输入区域
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel userIdLabel = new JLabel("用户name: ");
        userIdField = new JTextField(10);
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userIdField.getText();
                // 调用API接口，根据用户name获取点赞帖子列表的数据
                int[] likedPosts = getLikedPosts(name);

                // 将结果显示在结果区域
                resultArea.setText("");
                for (int post : likedPosts) {
                    resultArea.append(post + "\n");
                }
            }
        });

        inputPanel.add(userIdLabel);
        inputPanel.add(userIdField);
        inputPanel.add(confirmButton);

        // 结果显示区域
        resultArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    // 模拟API接口，根据用户name获取点赞帖子列表的数据
    private int[] getLikedPosts(String name) {
        Statement stmt0;
        int[] likedPosts = new int[0]; // 初始化为空数组

        if (con != null) {
            try {
                stmt0 = con.createStatement();
                String query = "SELECT p.ID\n" +
                        "FROM author_favorite_post AS afp\n" +
                        "JOIN post AS p ON afp.postid = p.ID\n" +
                        "JOIN author AS a ON afp.author = a.name\n" +
                        "WHERE a.name = ?";

                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, name); // 将name变量绑定到参数占位符

                ResultSet resultSet = preparedStatement.executeQuery();
                // 动态添加帖子ID到likedPosts数组
                ArrayList<Integer> tempList = new ArrayList<>();
                while (resultSet.next()) {
                    tempList.add(resultSet.getInt("ID"));
                }

                // 将ArrayList转换为int数组
                likedPosts = new int[tempList.size()];
                for (int i = 0; i < tempList.size(); i++) {
                    likedPosts[i] = tempList.get(i);
                }

                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return likedPosts;
    }
//                con.commit();
//                stmt0.close();
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//        // 这里可以根据具体的数据库操作来获取用户点赞帖子的列表数据
//        // 获取到的数据为字符串数组
//        return resultSet;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listfavoriteGUI ui = new listfavoriteGUI();
                ui.setVisible(true);
            }
        });
    }
}
