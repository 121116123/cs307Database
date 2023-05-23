import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registrationGUI extends JFrame {
    private JTextField authornameField;
    private JTextField idField;
    private firstGUI parentGUI;

    public registrationGUI() {
//        setTitle("Registration");
        super("Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setSize(300, 200);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JLabel authornameLabel = new JLabel("authorname:");
        JLabel idLabel = new JLabel("ID:");
        authornameField = new JTextField();
        idField = new JTextField();
        inputPanel.add(authornameLabel);
        inputPanel.add(authornameField);
        inputPanel.add(idLabel);
        inputPanel.add(idField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAccount();
            }
        });

        add(inputPanel, BorderLayout.CENTER);
        add(registerButton, BorderLayout.SOUTH);
    }

    public registrationGUI(firstGUI parentGUI) {
        this();//调用无参构造函数进行初始化
        this.parentGUI=parentGUI;
    }

    private void registerAccount() {
        String name = authornameField.getText();
        String ID = idField.getText();

        // 调用Author.registerAccount()方法注册新用户
        author.registerNewAuthor(name, ID);

        // 可以在这里添加一些成功注册的提示信息或其他操作
        JOptionPane.showMessageDialog(this, "Registration successful!");
    }

    public void openFirstGUI(){
        parentGUI.setVisible(true);
        setVisible(false);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                registrationGUI registrationGUI = new registrationGUI();
                registrationGUI.setVisible(true);
            }
        });
    }
}
