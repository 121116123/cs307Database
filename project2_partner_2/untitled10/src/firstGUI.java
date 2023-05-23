import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class firstGUI extends JFrame {
    private JButton signInButton;
    private JButton signOutButton;

    public firstGUI() {
        setTitle("BBS-cs307");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        signInButton = new JButton("Sign In");
        signOutButton = new JButton("Sign Out");

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opensigninGUI();
            }
        });

        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openregistrationGUI();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(signInButton);
        panel.add(signOutButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void opensigninGUI() {
        signinGUI signinGUI = new signinGUI(this);
        signinGUI.setVisible(true);
        setVisible(false);
    }

    private void openregistrationGUI() {
        registrationGUI registrationGUI = new registrationGUI(this);
        registrationGUI.setVisible(true);
        setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new firstGUI();
            }
        });
    }
}
