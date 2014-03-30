package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileFilter;

import categories.StringCategory;

import comparators.JaccardKNNComparator;
import comparators.KeywordsKNNComparator;
import comparators.MetricKNNComparator;
import comparators.NGramComparator;

import metrics.ChebyshevMetric;
import metrics.EuclidMetric;
import metrics.MetricClassifiable;
import metrics.TaxicabMetric;

import knn.Category;
import knn.Classifiable;
import knn.KNN;
import knn.KNNComparator;

import text.Text;
import text.WordList;
import utils.DisableablePanel;

import core.DataMaker;
import core.Engine;
import core.datamakers.CustomSamples;
import core.datamakers.ReutersByCountry;
import core.datamakers.ReutersByTopic;
import extraction.FuzzyVectorsManager;
import extraction.TrivialTextVectorsManager;
import extraction.VectorManager;

public class MainFrame extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = -8026416994513756565L;
  private Map<String, DataMaker> dataMakers;
  @SuppressWarnings("rawtypes")
  private JComboBox dataMakerChoice;
  private Engine engine;
  private JLabel parsingResults;
  private JLabel pickingResults;
  private JLabel trainingResults;
  private JLabel classificationResults;
  private DisableablePanel parsePane;
  private DisableablePanel pickPane;
  private DisableablePanel trainPane;
  private DisableablePanel classifyPane;
  private JButton parseTextsButton;
  private JButton forgetTextsButton;
  private JSlider trainingSetSizeSlider;
  private JSlider testSetSizeSlider;
  private JCheckBox allowSetOverlapCb;
  @SuppressWarnings("rawtypes")
  private JComboBox randomizeSettingChoice;
  private JButton pickSetsButton;
  private JButton forgetSetsButton;
  private DisableablePanel similarityPane;
  @SuppressWarnings("rawtypes")
  private JComboBox similarityChoice;
  private DisableablePanel metricPane;
  @SuppressWarnings("rawtypes")
  private JComboBox vectorContentsChoice;
  @SuppressWarnings("rawtypes")
  private JComboBox distanceChoice;
  private JButton trainButton;
  private JButton forgetTrainingButton;
  private JSlider kknnSlider;
  private KNN knn = null;
  private VectorManager vectorManager = null;
  private List<Classifiable> votes = null;
  private List<Classifiable> objects = null;
  private List<Category> answers = null;
  private KNNComparator comparator = null;
  private JButton gcrButton1;
  private JButton gcrButton2;
  private JButton gcrButton3;
  private JButton classifyButton;
  
  
  public MainFrame() {
    super();
    setTitle("KSR1 GUI");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setSize(800, 640);
    
    engine = new Engine();
    
    dataMakers = new HashMap<String, DataMaker>();
    dataMakers.put("Reuters, 6 chosen countries are labels", new ReutersByCountry());
    dataMakers.put("Reuters, 2 chosen topics are labels", new ReutersByTopic());
    dataMakers.put("Pieces of Shakespeare's and Byron's works, authors are labels", new CustomSamples());
    
    buildUI();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
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
    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

    parsingResults = new JLabel(" ");
    mainPane.add(parsingResults);
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
    mainPane.add(parsePane);

    pickingResults = new JLabel(" ");
    mainPane.add(pickingResults);
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
    pickPane.setVisible(false);
    pickPane.setEnabled(false);
    mainPane.add(pickPane);
    
    trainingResults = new JLabel(" ");
    mainPane.add(trainingResults);
    trainPane = new DisableablePanel();
    trainPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Extraction & training"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    similarityPane = new DisableablePanel();
    similarityPane.add(new JLabel("Similarity measure: "));
    similarityChoice = new JComboBox(new String[]{"Metric space on R^n", "Keywords based", "N-Gram based", "Jaccard coefficient"});
    similarityChoice.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        if(MainFrame.this.similarityChoice.getSelectedIndex() == 0) {
          metricPane.setEnabled(true);
          metricPane.setVisible(true);
        } else {
          metricPane.setVisible(false);
          metricPane.setEnabled(false);
        }
      }
    });
    similarityPane.add(similarityChoice);
    trainPane.add(similarityPane);
    metricPane = new DisableablePanel();
    metricPane.setLayout(new GridLayout(2,2,5,5));
    metricPane.add(new JLabel("Vector contents: "));
    vectorContentsChoice = new JComboBox(new String[]{"All the words (SLOW!)", "Predefined keywords", "Based on fuzzy sets"});
    metricPane.add(vectorContentsChoice);
    metricPane.add(new JLabel("Distance function: "));
    distanceChoice = new JComboBox(new String[]{"Euclidean", "Chebyshev", "Manhattan"});
    metricPane.add(distanceChoice);
    trainPane.add(metricPane);
    
    DisableablePanel kknnPane = new DisableablePanel();
    kknnPane.add(new JLabel("``k'' parameter for k-NN: "));
    kknnSlider = new JSlider(JSlider.HORIZONTAL, 3, 15, 5);
    kknnSlider.setMajorTickSpacing(4);
    kknnSlider.setMinorTickSpacing(1);
    kknnSlider.setPaintTicks(true);
    kknnSlider.setPaintLabels(true);
    kknnPane.add(kknnSlider);
    trainPane.add(kknnPane);
    DisableablePanel trainButtonsPane = new DisableablePanel();
    trainButton = new JButton("Perform training");
    trainButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          MainFrame.this.train();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    trainButtonsPane.add(trainButton);
    forgetTrainingButton = new JButton("Forget training results");
    forgetTrainingButton.setEnabled(false);
    forgetTrainingButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        MainFrame.this.forgetTraining();
      }
    });
    trainButtonsPane.add(forgetTrainingButton);
    trainPane.add(trainButtonsPane);
    trainPane.setVisible(false);
    trainPane.setEnabled(false);
    mainPane.add(trainPane);
    
    classifyPane = new DisableablePanel();
    classifyPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Classify the test set"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
    classifyPane.setLayout(new BoxLayout(classifyPane, BoxLayout.X_AXIS));
    classifyButton = new JButton("Classify test set!");
    classifyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        MainFrame.this.classify();
      }
    });
    classifyPane.add(classifyButton);
    gcrButton1 = new JButton("Get per-sample log");
    gcrButton1.setEnabled(false);
    gcrButton1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        try {
          MainFrame.this.getPerSampleCSV();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    classifyPane.add(gcrButton1);
    gcrButton2 = new JButton("Get per-class statistics (TPR, PPV)");
    gcrButton2.setEnabled(false);
    gcrButton2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        try {
          MainFrame.this.getPerClassCSV();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    classifyPane.add(gcrButton2);
    gcrButton3 = new JButton("Get class relations matrix");
    gcrButton3.setEnabled(false);
    gcrButton3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        try {
          MainFrame.this.getMatrixCSV();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    classifyPane.add(gcrButton3);
    classifyPane.setVisible(false);
    classifyPane.setEnabled(false);
    mainPane.add(classifyPane);
    
    classificationResults = new JLabel(" ");
    mainPane.add(classificationResults);
    
    add(mainPane);
  }

  protected void parseTexts() throws Exception {
    engine.clearTexts();
    dataMakers.get(dataMakerChoice.getSelectedItem()).createDataSets(engine.getTexts());
    dataMakerChoice.setEnabled(false);
    parseTextsButton.setEnabled(false);
    forgetTextsButton.setEnabled(true);
    parsingResults.setText("<html>" + Integer.toString(engine.getTexts().size()) + " pieces of text are available<br>(from ``" + dataMakerChoice.getSelectedItem() + "'' set).</html>");
    pickPane.setEnabled(true);
    pickPane.setVisible(true);
  }

  protected void forgetTexts() {
    engine.clearTexts();
    dataMakerChoice.setEnabled(true);
    parseTextsButton.setEnabled(true);
    forgetTextsButton.setEnabled(false);
    parsingResults.setText(" ");
    pickPane.setVisible(false);
    pickPane.setEnabled(false);
    parsingResults.setText(" ");
  }

  protected void pickSets() {
    double trainingSetSize = trainingSetSizeSlider.getValue() / 100.0;
    double testSetSize = testSetSizeSlider.getValue() / 100.0;
    boolean randomizeSets = (randomizeSettingChoice.getSelectedIndex() == 1);
    boolean allowOverlap = allowSetOverlapCb.isSelected();
    engine.pickTrainingAndTestSets(trainingSetSize, testSetSize, randomizeSets, allowOverlap);
    pickingResults.setText("<html> Picked " + Integer.toString(engine.getTrainingSet().size()) + " training samples, " + Integer.toString(engine.getTestSet().size()) + " test samples<br>(with " + (randomizeSets ? "random" : "predefined") + " order, " + (allowOverlap ? "overlapping allowed" : "without overlapping") + ").</html>");
    trainingSetSizeSlider.setEnabled(false);
    testSetSizeSlider.setEnabled(false);
    allowSetOverlapCb.setEnabled(false);
    randomizeSettingChoice.setEnabled(false);
    pickSetsButton.setEnabled(false);
    forgetSetsButton.setEnabled(true);
    parsePane.setVisible(false);
    parsePane.setEnabled(false);
    trainPane.setEnabled(true);
    trainPane.setVisible(true);
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
    parsePane.setVisible(true);
    trainPane.setVisible(false);
    trainPane.setEnabled(false);
    pickingResults.setText(" ");
  }
  
  public void train() throws Exception {
    int k = kknnSlider.getValue();
    
    votes = null;
    comparator = null;
    
    StringBuilder message = new StringBuilder();
    
    message.append("<html>Classifier to be used: K-NN with k=");
    message.append(k);
    message.append(",<br>");
    
    if(similarityChoice.getSelectedIndex() == 0) { // metric
      message.append("samples translated into R^n vectors,<br>");
      if(vectorContentsChoice.getSelectedIndex() == 0) { // all
        message.append("all words taken into consideration,<br>");
        vectorManager = new TrivialTextVectorsManager(engine.getTrainingSet(), null);
        votes = vectorManager.getVectors();
      } else if(vectorContentsChoice.getSelectedIndex() == 1) { // keywords
        message.append("only keywords into consideration,<br>");
        WordList keywords = new WordList(Arrays.asList(
            new File("data/wordtypes/exchanges.txt"),
            new File("data/wordtypes/orgs.txt"),
            new File("data/wordtypes/people.txt"),
            new File("data/wordtypes/places.txt"),
            new File("data/wordtypes/topics.txt")));
        vectorManager = new TrivialTextVectorsManager(engine.getTrainingSet(), keywords);
        votes = vectorManager.getVectors();
      } else if(vectorContentsChoice.getSelectedIndex() == 2) { // fuzzy
        message.append("method based on fuzzy set used,<br>");
        engine.createNewFuzzySets(new File("data/fuzzy/countries"), Arrays.asList("exchanges", "orgs", "people", "places", "topics"));
        vectorManager = new FuzzyVectorsManager(engine.getTrainingSet(), engine.getFuzzySets());
        votes = vectorManager.getVectors();
      }
      if(distanceChoice.getSelectedIndex() == 0) {
        message.append("Eudlidean distance will be used,<br>");
        comparator = new MetricKNNComparator(new EuclidMetric());
      } else if(distanceChoice.getSelectedIndex() == 1) {
        message.append("Chebyshev distance will be used,<br>");
        comparator = new MetricKNNComparator(new ChebyshevMetric());
      } else if(distanceChoice.getSelectedIndex() == 2) {
        message.append("Taxicab distance will be used,<br>");
        comparator = new MetricKNNComparator(new TaxicabMetric());
      }
      message.append("Dimensions: ");
      message.append(((MetricClassifiable) votes.get(0)).getVector().size());
      message.append(".");
      objects = new ArrayList<Classifiable>();
      answers = new ArrayList<Category>();
      for(Text text: engine.getTestSet()) {
        objects.add(vectorManager.getVectorForNewSample(text));
        answers.add(text.getCategory());
      }
    } else if(similarityChoice.getSelectedIndex() == 1) {
      message.append("samples will be tested for the presence of keywords.");
      votes = new ArrayList<Classifiable>();
      votes.addAll(engine.getTrainingSet());
      objects = new ArrayList<Classifiable>();
      answers = new ArrayList<Category>();
      for(Text text: engine.getTestSet()) {
        Text object = new Text(text);
        object.setCategory(null);
        objects.add(object);
        answers.add(text.getCategory());
      }
      WordList keywords = new WordList(Arrays.asList(
          new File("data/wordtypes/exchanges.txt"),
          new File("data/wordtypes/orgs.txt"),
          new File("data/wordtypes/people.txt"),
          new File("data/wordtypes/places.txt"),
          new File("data/wordtypes/topics.txt")));
      comparator = new KeywordsKNNComparator(new HashSet<String>(keywords));
    } else if(similarityChoice.getSelectedIndex() == 2) {
      message.append("N-grams statistics will be used.");
      votes = new ArrayList<Classifiable>();
      votes.addAll(engine.getTrainingSet());
      objects = new ArrayList<Classifiable>();
      answers = new ArrayList<Category>();
      for(Text text: engine.getTestSet()) {
        Text object = new Text(text);
        object.setCategory(null);
        objects.add(object);
        answers.add(text.getCategory());
      }
      comparator = new NGramComparator();
    } else if(similarityChoice.getSelectedIndex() == 3) {
      message.append("Jaccard coefficient will be used.");
      votes = new ArrayList<Classifiable>();
      votes.addAll(engine.getTrainingSet());
      objects = new ArrayList<Classifiable>();
      answers = new ArrayList<Category>();
      for(Text text: engine.getTestSet()) {
        Text object = new Text(text);
        object.setCategory(null);
        objects.add(object);
        answers.add(text.getCategory());
      }
      comparator = new JaccardKNNComparator();
    }
    
    message.append("</html>");
    trainingResults.setText(message.toString());
    
    knn = new KNN(k);
    knn.train(votes);
    
    similarityChoice.setEnabled(false);
    vectorContentsChoice.setEnabled(false);
    distanceChoice.setEnabled(false);
    kknnSlider.setEnabled(false);
    trainButton.setEnabled(false);
    forgetTrainingButton.setEnabled(true);
    pickPane.setVisible(false);
    pickPane.setEnabled(false);
    classifyPane.setEnabled(true);
    classifyPane.setVisible(true);
  }

  public void forgetTraining() {
    votes = null;
    objects = null;
    answers = null;
    vectorManager = null;
    comparator = null;
    knn = null;
    
    similarityChoice.setEnabled(true);
    vectorContentsChoice.setEnabled(true);
    distanceChoice.setEnabled(true);
    kknnSlider.setEnabled(true);
    trainButton.setEnabled(true);
    forgetTrainingButton.setEnabled(false);
    pickPane.setEnabled(true);
    pickPane.setVisible(true);
    classifyButton.setEnabled(true);
    gcrButton1.setEnabled(false);
    gcrButton2.setEnabled(false);
    gcrButton3.setEnabled(false);
    classifyPane.setVisible(false);
    classifyPane.setEnabled(false);
    trainingResults.setText(" ");
    classificationResults.setText(" ");
  }
  
  public void classify() {
    knn.classify(objects, comparator);
    classifyButton.setEnabled(false);
    gcrButton1.setEnabled(true);
    gcrButton2.setEnabled(true);
    gcrButton3.setEnabled(true);
    
    int correct = 0;
    for(int i = 0; i < objects.size(); ++i)
      if(objects.get(i).getCategory().equals(answers.get(i))) ++correct;
    
    classificationResults.setText(Double.toString((correct * 100.0) / objects.size()) + "% of classifications are correct.");
  }
  
  public void getPerSampleCSV() throws Exception {
    JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new FileFilter() {
      @Override
      public String getDescription() {
        return "*.csv (Comma Separated File)";
      }
      
      @Override
      public boolean accept(File arg0) {
        return arg0.getPath().endsWith(".csv");
      }
    });
    int ret = fc.showSaveDialog(null);
    if(ret == JFileChooser.APPROVE_OPTION) {
      BufferedWriter out = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
      out.write("Sample index,Actual category,Classification result\n");
      for(int i = 0; i < objects.size(); ++i) {
        out.write(Integer.toString(i + 1));
        out.write(",");
        out.write(((StringCategory) answers.get(i)).getString());
        out.write(",");
        out.write(((StringCategory) objects.get(i).getCategory()).getString());
        out.write("\n");
      }
      out.close();
    }
  }
  
  public void getPerClassCSV() throws Exception {
    JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new FileFilter() {
      @Override
      public String getDescription() {
        return "*.csv (Comma Separated File)";
      }
      
      @Override
      public boolean accept(File arg0) {
        return arg0.getPath().endsWith(".csv");
      }
    });
    int ret = fc.showSaveDialog(null);
    if(ret == JFileChooser.APPROVE_OPTION) {
      BufferedWriter out = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
      out.write("Category,Elements in category,Sensitivity (TPR),Elements classified as category,Precision (PPV)\n");
      
      Set<String> categoriesSet = new TreeSet<String>();
      for(Classifiable cla: objects) categoriesSet.add(((StringCategory) cla.getCategory()).getString());
      for(Category cat: answers) categoriesSet.add(((StringCategory) cat).getString());
      List<String> categoriesList = new ArrayList<String>(categoriesSet);
      Map<String, Integer> categoryIDs = new HashMap<String, Integer>();
      for(int j = 0; j < categoriesList.size(); ++j) categoryIDs.put(categoriesList.get(j), j);
      Integer[][] counters = new Integer[categoriesList.size()][categoriesList.size()];
      
      for(int j = 0; j < categoriesList.size(); ++j)
        for(int k = 0; k < categoriesList.size(); ++k)
          counters[j][k] = 0;
      
      for(int i = 0; i < objects.size(); ++i) {
        String actual = ((StringCategory) answers.get(i)).getString();
        String guessed = ((StringCategory) objects.get(i).getCategory()).getString();
        counters[categoryIDs.get(actual)][categoryIDs.get(guessed)] += 1;
      }
      
      for(int j = 0; j < categoriesList.size(); ++j) {
        int classSize = 0;
        int picksCount = 0;
        for(int k = 0; k < categoriesList.size(); ++k) classSize += counters[j][k];
        for(int k = 0; k < categoriesList.size(); ++k) picksCount += counters[k][j];
        out.write(categoriesList.get(j));
        out.write(",");
        out.write(Integer.toString(classSize));
        out.write(",");
        out.write(Double.toString((counters[j][j] * 100.0) / classSize));
        out.write("%");
        out.write(",");
        out.write(Integer.toString(picksCount));
        out.write(",");
        out.write(Double.toString((counters[j][j] * 100.0) / picksCount));
        out.write("%");
        out.write("\n");
      }
      out.close();
    }
  }
  
  public void getMatrixCSV() throws Exception {
    JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new FileFilter() {
      @Override
      public String getDescription() {
        return "*.csv (Comma Separated File)";
      }
      
      @Override
      public boolean accept(File arg0) {
        return arg0.getPath().endsWith(".csv");
      }
    });
    int ret = fc.showSaveDialog(null);
    if(ret == JFileChooser.APPROVE_OPTION) {
      BufferedWriter out = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
      
      Set<String> categoriesSet = new TreeSet<String>();
      for(Classifiable cla: objects) categoriesSet.add(((StringCategory) cla.getCategory()).getString());
      for(Category cat: answers) categoriesSet.add(((StringCategory) cat).getString());
      List<String> categoriesList = new ArrayList<String>(categoriesSet);
      Map<String, Integer> categoryIDs = new HashMap<String, Integer>();
      for(int j = 0; j < categoriesList.size(); ++j) categoryIDs.put(categoriesList.get(j), j);
      Integer[][] counters = new Integer[categoriesList.size()][categoriesList.size()];
      
      for(int j = 0; j < categoriesList.size(); ++j)
        for(int k = 0; k < categoriesList.size(); ++k)
          counters[j][k] = 0;
      
      for(int i = 0; i < objects.size(); ++i) {
        String actual = ((StringCategory) answers.get(i)).getString();
        String guessed = ((StringCategory) objects.get(i).getCategory()).getString();
        counters[categoryIDs.get(actual)][categoryIDs.get(guessed)] += 1;
      }
      
      out.write("Actual/Classified as");
      for(int j = 0; j < categoriesList.size(); ++j) {
        out.write(",");
        out.write(categoriesList.get(j));
      }
      out.write("\n");
      
      for(int j = 0; j < categoriesList.size(); ++j) {
        out.write(categoriesList.get(j));
        for(int k = 0; k < categoriesList.size(); ++k) {
          out.write(",");
          out.write(Integer.toString(counters[j][k]));
        }
        out.write("\n");
      }
      out.close();
    }
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
