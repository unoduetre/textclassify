package core.datamakers;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import text.Text;

import core.DataMaker;

public class CustomSamples extends DataMaker {

  @Override
  public void createDataSets(List<Text> texts) throws Exception {
System.out.println("Mode: Custom texts, authors [Byron|Shakespeare] are labels.");
    
    System.out.println("Parsing texts...");
    
    List<File> files = Arrays.asList(new File("data/xml-custom").listFiles());
    Collections.sort(files);
    
    parseTexts(
        texts,
        files,
        Arrays.asList("CUSTOMSAMPLES","SAMPLE"),
        Arrays.asList("CUSTOMSAMPLES","SAMPLE","AUTHOR"),
        Arrays.asList("CUSTOMSAMPLES","SAMPLE","CONTENT")
    );
  }

}
