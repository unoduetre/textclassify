package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = -8026416994513756565L;
  
  
  public MainFrame() {
    super();
    setTitle("KSR1 GUI");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setSize(320, 480);
    
    buildUI();
  }
  
  private void buildUI() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem fileExit = new JMenuItem("Exit");
    fileExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        System.exit(0);
      }
    });
    fileMenu.add(fileExit);
    menuBar.add(fileMenu);
    
    setJMenuBar(menuBar);
    
    
    JPanel mainPane = new JPanel();
    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
    
    JPanel parsePane = new JPanel();
    parsePane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Parse input data"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    mainPane.add(parsePane);
    
    JPanel pickPane = new JPanel();
    pickPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Pick training set and test set"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    mainPane.add(pickPane);
    
    JPanel trainPane = new JPanel();
    trainPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Train the classifier"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    trainPane.add(new JLabel("TODO"));
    mainPane.add(trainPane);
    
    JPanel classifyPane = new JPanel();
    classifyPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Perform the classification"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    classifyPane.add(new JLabel("TODO"));
    mainPane.add(classifyPane);
    
    add(mainPane);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          MainFrame ex = new MainFrame();
            ex.setVisible(true);
        }
    });
}
}
