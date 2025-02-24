package r4MS;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginDialog extends JDialog
{

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JTextField tfUsername;
   private JTextField tfDni;
   private JPasswordField pfPassword;
   private JLabel lbUsername;
   private JLabel lbDni;
   private JLabel lbPassword;
   private JButton btnLogin;
   private JButton btnCancel;
   private boolean succeeded;

   public LoginDialog hideDni()
   {
      tfDni.setEnabled(false);
      lbDni.setEnabled(false);
      return this;
   }
   
   public LoginDialog(Frame parent)
   {
      super(parent, "Login", true);
      //
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints cs = new GridBagConstraints();

      cs.fill = GridBagConstraints.HORIZONTAL;

      lbUsername = new JLabel("Username: ");
      cs.gridx = 0;
      cs.gridy = 0;
      cs.gridwidth = 1;
      panel.add(lbUsername, cs);

      tfUsername = new JTextField(20);
      cs.gridx = 1;
      cs.gridy = 0;
      cs.gridwidth = 2;
      panel.add(tfUsername, cs);
      
      lbDni = new JLabel("Dni: ");
      cs.gridx = 0;
      cs.gridy = 1;
      cs.gridwidth = 1;
      panel.add(lbDni, cs);

      tfDni = new JTextField(20);
      cs.gridx = 1;
      cs.gridy = 1;
      cs.gridwidth = 2;
      panel.add(tfDni, cs);

      lbPassword = new JLabel("Password: ");
      cs.gridx = 0;
      cs.gridy = 2;
      cs.gridwidth = 1;
      panel.add(lbPassword, cs);

      pfPassword = new JPasswordField(20);
      cs.gridx = 1;
      cs.gridy = 2;
      cs.gridwidth = 2;
      panel.add(pfPassword, cs);
      panel.setBorder(new LineBorder(Color.GRAY));

      btnLogin = new JButton("Login");

      btnLogin.addActionListener(new ActionListener()
      {

         public void actionPerformed(ActionEvent e)
         {
            /*
             * if (Login.authenticate(getUsername(), getPassword())) {
             * JOptionPane.showMessageDialog(LoginDialog.this, "Hi " +
             * getUsername() + "! You have successfully logged in.", "Login",
             * JOptionPane.INFORMATION_MESSAGE); succeeded = true; dispose(); }
             * else { JOptionPane.showMessageDialog(LoginDialog.this,
             * "Invalid username or password", "Login",
             * JOptionPane.ERROR_MESSAGE); // reset username and password
             * tfUsername.setText(""); pfPassword.setText(""); succeeded =
             * false;
             * 
             * }
             */
            dispose();
         }
      });
      btnCancel = new JButton("Cancel");
      btnCancel.addActionListener(new ActionListener()
      {

         public void actionPerformed(ActionEvent e)
         {
            dispose();
         }
      });
      JPanel bp = new JPanel();
      bp.add(btnLogin);
      bp.add(btnCancel);

      getContentPane().add(panel, BorderLayout.CENTER);
      getContentPane().add(bp, BorderLayout.PAGE_END);

      pack();
      setResizable(false);
      setLocationRelativeTo(parent);
   }

   public String getUsername()
   {
      return tfUsername.getText().trim();
   }

   public String getPassword()
   {
      return new String(pfPassword.getPassword());
   }

   public String getDni()
   {
      return tfDni.getText().trim();
   }
   
   public boolean isSucceeded()
   {
      return succeeded;
   }
}