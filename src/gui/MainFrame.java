package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.DisableablePanel;

import core.DataMaker;
import core.Engine;
import core.datamakers.CustomSamples;
import core.datamakers.ReutersByCountry;
import core.datamakers.ReutersByTopic;

public class MainFrame extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = -8026416994513756565L;
  private Map<String, DataMaker> dataMakers;
  private JComboBox dataMakerChoice;
  private Engine engine;
  private DisableablePanel parsePane;
  private DisableablePanel pickPane;
  private DisableablePanel extractionPane;
  private DisableablePanel trainPane;
  private DisableablePanel classifyPane;
  private JButton parseTextsButton;
  private JButton forgetTextsButton;
  private JLabel parsingResults;
  private JSlider trainingSetSizeSlider;
  private JSlider testSetSizeSlider;
  private JCheckBox allowSetOverlapCb;
  private JComboBox randomizeSettingChoice;
  private JButton pickSetsButton;
  private JButton forgetSetsButton;
  private JLabel pickingResults;
  
  
  public MainFrame() {
    super();
    setTitle("KSR1 GUI");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setSize(640, 640);
    
    engine = new Engine();
    
    dataMakers = new HashMap<String, DataMaker>();
    dataMakers.put("Reuters, 6 chosen countries are labels", new ReutersByCountry());
    dataMakers.put("Reuters, 2 chosen topics are labels", new ReutersByTopic());
    dataMakers.put("Pieces of Shakespeare's and Byron's works, authors are labels", new CustomSamples());
    
    buildUI();
  }
  
  private void buildUI() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem fileExit = new JMenuItem("Exit");
    fileExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        MainFrame.this.dispose();
      }
    });
    fileMenu.add(fileExit);
    menuBar.add(fileMenu);
    
    setJMenuBar(menuBar);
    
    
    JPanel mainPane = new JPanel();
    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
    
    parsePane = new DisableablePanel();
    parsePane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Parse input data"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    parsePane.setLayout(new BoxLayout(parsePane, BoxLayout.Y_AXIS));
    DisableablePanel dataMakerPane = new DisableablePanel();
    dataMakerPane.add(new JLabel("Data to parse: "));
    dataMakerChoice = new JComboBox(dataMakers.keySet().toArray());
    dataMakerChoice.setSelectedItem("Reuters, 6 chosen countries are labels");
    dataMakerPane.add(dataMakerChoice);
    parsePane.add(dataMakerPane);
    DisableablePanel parseButtonsPane = new DisableablePanel();
    parseTextsButton = new JButton("Parse texts");
    parseTextsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        try {
          MainFrame.this.parseTexts();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    parseButtonsPane.add(parseTextsButton);
    forgetTextsButton = new JButton("Forget parsed texts");
    forgetTextsButton.setEnabled(false);
    forgetTextsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        MainFrame.this.forgetTexts();
      }
    });
    parseButtonsPane.add(forgetTextsButton);
    parsePane.add(parseButtonsPane);
    parsingResults = new JLabel(" ");
    parsePane.add(parsingResults);
    mainPane.add(parsePane);
    
    pickPane = new DisableablePanel();
    pickPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Pick training set and test set"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    pickPane.setLayout(new BoxLayout(pickPane, BoxLayout.Y_AXIS));
    DisableablePanel setSizesPane = new DisableablePanel();
    setSizesPane.setLayout(new GridLayout(2,2,5,5));
    setSizesPane.add(new JLabel("Training set size [%]: "));
    trainingSetSizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 60);
    trainingSetSizeSlider.setMajorTickSpacing(15);
    trainingSetSizeSlider.setMinorTickSpacing(5);
    trainingSetSizeSlider.setPaintTicks(true);
    trainingSetSizeSlider.setPaintLabels(true);
    trainingSetSizeSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent arg0) {
        if(! MainFrame.this.allowSetOverlapCb.isSelected()) {
          if(MainFrame.this.trainingSetSizeSlider.getValue() + MainFrame.this.testSetSizeSlider.getValue() > 100) {
            MainFrame.this.trainingSetSizeSlider.setValue(100 - MainFrame.this.testSetSizeSlider.getValue());
          }
        }
      }
    });
    setSizesPane.add(trainingSetSizeSlider);
    setSizesPane.add(new JLabel("Test set size [%]: "));
    testSetSizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 40);
    testSetSizeSlider.setMajorTickSpacing(15);
    testSetSizeSlider.setMinorTickSpacing(5);
    testSetSizeSlider.setPaintTicks(true);
    testSetSizeSlider.setPaintLabels(true);
    testSetSizeSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent arg0) {
        if(! MainFrame.this.allowSetOverlapCb.isSelected()) {
          if(MainFrame.this.trainingSetSizeSlider.getValue() + MainFrame.this.testSetSizeSlider.getValue() > 100) {
            MainFrame.this.testSetSizeSlider.setValue(100 - MainFrame.this.trainingSetSizeSlider.getValue());
          }
        }
      }
    });
    setSizesPane.add(testSetSizeSlider);
    pickPane.add(setSizesPane);
    allowSetOverlapCb = new JCheckBox("Let training set and test set overlap");
    allowSetOverlapCb.setSelected(false);
    allowSetOverlapCb.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        if(MainFrame.this.trainingSetSizeSlider.getValue() + MainFrame.this.testSetSizeSlider.getValue() > 100) {
          int sum = MainFrame.this.trainingSetSizeSlider.getValue() + MainFrame.this.testSetSizeSlider.getValue();
          int tr = (int) (MainFrame.this.trainingSetSizeSlider.getValue() * (100.0 / sum));
          int te = (int) (MainFrame.this.testSetSizeSlider.getValue() * (100.0 / sum));
          if(tr < 10)      { tr = 10; te = 90; }
          else if(te < 10) { tr = 90; te = 10; }
          if(tr + te == 99) {
            if(tr >= te) ++te;
            else ++tr;
          }
          MainFrame.this.testSetSizeSlider.setValue(10);
          MainFrame.this.trainingSetSizeSlider.setValue(tr);
          MainFrame.this.testSetSizeSlider.setValue(te);
        }
      }
    });
    pickPane.add(allowSetOverlapCb);
    DisableablePanel setRandomizeSettingsPane = new DisableablePanel();
    setRandomizeSettingsPane.add(new JLabel("Choice method: "));
    randomizeSettingChoice = new JComboBox(new String[]{"Pick test set from the beginning, training set from the end", "Perform random choice"});
    setRandomizeSettingsPane.add(randomizeSettingChoice);
    pickPane.add(setRandomizeSettingsPane);
    DisableablePanel pickButtonsPane = new DisableablePanel();
    pickSetsButton = new JButton("Pick sets");
    pickSetsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        MainFrame.this.pickSets();
      }
    });
    pickButtonsPane.add(pickSetsButton);
    forgetSetsButton = new JButton("Forget sets");
    forgetSetsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        MainFrame.this.forgetSets();
      }
    });
    pickButtonsPane.add(forgetSetsButton);
    forgetSetsButton.setEnabled(false);
    pickPane.add(pickButtonsPane);
    pickingResults = new JLabel(" ");
    pickPane.add(pickingResults);
    pickPane.setEnabled(false);
    mainPane.add(pickPane);
    
    extractionPane = new DisableablePanel();
    extractionPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Extract features"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    extractionPane.add(new JLabel("TODO"));
    extractionPane.setEnabled(false);
    mainPane.add(extractionPane);
    
    trainPane = new DisableablePanel();
    trainPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Train the classifier"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    trainPane.add(new JLabel("TODO"));
    trainPane.setEnabled(false);
    mainPane.add(trainPane);
    
    classifyPane = new DisableablePanel();
    classifyPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Perform the classification"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    classifyPane.add(new JLabel("TODO"));
    classifyPane.setEnabled(false);
    mainPane.add(classifyPane);
    
    add(mainPane);
  }

  protected void parseTexts() throws Exception {
    engine.clearTexts();
    dataMakers.get(dataMakerChoice.getSelectedItem()).createDataSets(engine.getTexts());
    dataMakerChoice.setEnabled(false);
    parseTextsButton.setEnabled(false);
    forgetTextsButton.setEnabled(true);
    parsingResults.setText(Integer.toString(engine.getTexts().size()) + " pieces of text available.");
    pickPane.setEnabled(true);
  }

  protected void forgetTexts() {
    engine.clearTexts();
    dataMakerChoice.setEnabled(true);
    parseTextsButton.setEnabled(true);
    forgetTextsButton.setEnabled(false);
    parsingResults.setText(" ");
    pickPane.setEnabled(false);
  }

  protected void pickSets() {
    double trainingSetSize = trainingSetSizeSlider.getValue() / 100.0;
    double testSetSize = testSetSizeSlider.getValue() / 100.0;
    boolean randomizeSets = (randomizeSettingChoice.getSelectedIndex() == 1);
    boolean allowOverlap = allowSetOverlapCb.isSelected();
    engine.pickTrainingAndTestSets(trainingSetSize, testSetSize, randomizeSets, allowOverlap);
    pickingResults.setText(Integer.toString(engine.getTrainingSet().size()) + " training samples, " + Integer.toString(engine.getTestSet().size()) + " test samples.");
    trainingSetSizeSlider.setEnabled(false);
    testSetSizeSlider.setEnabled(false);
    allowSetOverlapCb.setEnabled(false);
    randomizeSettingChoice.setEnabled(false);
    pickSetsButton.setEnabled(false);
    forgetSetsButton.setEnabled(true);
    parsePane.setEnabled(false);
    extractionPane.setEnabled(true);
  }
  
  protected void forgetSets() {
    engine.clearTrainingAndTestSets();
    pickingResults.setText(" ");
    trainingSetSizeSlider.setEnabled(true);
    testSetSizeSlider.setEnabled(true);
    allowSetOverlapCb.setEnabled(true);
    randomizeSettingChoice.setEnabled(true);
    pickSetsButton.setEnabled(true);
    forgetSetsButton.setEnabled(false);
    parsePane.setEnabled(true);
    extractionPane.setEnabled(false);
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
