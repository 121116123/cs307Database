//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.*;
//import java.util.ArrayList;
//
//public class personalGUI extends JFrame {
//    private JTextField nameField;
//    private JTextArea postsTextArea;
//    private JTextArea repliesTextArea;
//    public static Connection con = null;
//
//    public personalGUI() {
//        setTitle("User Posts and Replies");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(500, 400);
//        setLayout(new BorderLayout());
//
//        postsTextArea = new JTextArea();
//        repliesTextArea = new JTextArea();
//        nameField = new JTextField(10);
//
//        JButton viewPostsButton = new JButton("View Posts");
//        viewPostsButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String name = nameField.getText();
//                // 获取用户发布的帖子数据并显示在postsTextArea上
//                String posts = getPostsData(name);  // 获取帖子数据
//                postsTextArea.setText(posts);
//            }
//        });
//
//        JButton viewRepliesButton = new JButton("View Replies");
//        viewRepliesButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // 获取用户回复的帖子数据并显示在repliesTextArea上
//                String replies = getRepliesData();  //获取回复数据
//                repliesTextArea.setText(replies);
//            }
//        });
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(viewPostsButton);
//        buttonPanel.add(viewRepliesButton);
//
//        add(postsTextArea, BorderLayout.NORTH);
//        add(repliesTextArea, BorderLayout.CENTER);
//        add(buttonPanel, BorderLayout.SOUTH);
//
//        setVisible(true);
//    }
//
//
//    private int[] getPostsData(String name) {
//        Statement stmt0;
//        int[] sendPosts = new int[0]; // 初始化为空数组
//
//        if (con != null) {
//            try {
//                stmt0 = con.createStatement();
//                String query = "SELECT p.ID\n" +
//                        "FROM author_favorite_post AS afp\n" +
//                        "JOIN post AS p ON afp.postid = p.ID\n" +
//                        "JOIN author AS a ON afp.author = a.name\n" +
//                        "WHERE a.name = ?";
//
//                PreparedStatement preparedStatement = con.prepareStatement(query);
//                preparedStatement.setString(1, name); // 将name变量绑定到参数占位符
//
//                ResultSet resultSet = preparedStatement.executeQuery();
//                // 动态添加帖子ID到likedPosts数组
//                ArrayList<Integer> tempList = new ArrayList<>();
//                while (resultSet.next()) {
//                    tempList.add(resultSet.getInt("ID"));
//                }
//
//                // 将ArrayList转换为int数组
//                sendPosts = new int[tempList.size()];
//                for (int i = 0; i < tempList.size(); i++) {
//                    sendPosts[i] = tempList.get(i);
//                }
//
//                con.commit();
//                stmt0.close();
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//        return sendPosts;
//    }
//
//    // 从数据库或其他数据源获取用户回复的帖子数据
//    private String getRepliesData() {
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new personalGUI();
//            }
//        });
//    }
//}
