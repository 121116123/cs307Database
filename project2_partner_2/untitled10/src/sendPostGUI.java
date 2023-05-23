import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class sendPostGUI extends JFrame {
    private JTextField authornameField;
    private JTextField idField;

    public sendPostGUI() {
        setTitle("sendpost");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JLabel authornameLabel = new JLabel("authorname:");
        JLabel contentLabel = new JLabel("content:");
        authornameField = new JTextField();
        idField = new JTextField();
        inputPanel.add(authornameLabel);
        inputPanel.add(authornameField);
        inputPanel.add(contentLabel);
        inputPanel.add(idField);

        JButton sendpostButton = new JButton("sendpost");
        sendpostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendpost();
            }
        });

        add(inputPanel, BorderLayout.CENTER);
        add(sendpostButton, BorderLayout.SOUTH);
    }

    private void sendpost() {
        String name = authornameField.getText();
        String content = idField.getText();


        author_send_post.sendpost(name,content);

        JOptionPane.showMessageDialog(this, "sendpost successful!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sendPostGUI sendpostGUI = new sendPostGUI();
                sendpostGUI.setVisible(true);
            }
        });
    }
}
