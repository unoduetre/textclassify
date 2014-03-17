package fuzzy;

import java.util.HashMap;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.InputStream;

public class FuzzySet extends HashMap<String,Float>
{
  public FuzzySet(InputStream stream) throws Exception
  {
    super();
    StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(stream)));
    tokenizer.resetSyntax();
    tokenizer.wordChars(0x0000,0xFFFF);
    tokenizer.whitespaceChars(' ',' ');
    tokenizer.whitespaceChars('\t','\t');
    tokenizer.whitespaceChars('\n','\n');
    tokenizer.whitespaceChars('\u000B','\u000B');
    tokenizer.whitespaceChars('\f','\f');
    tokenizer.whitespaceChars('\r','\r');
    tokenizer.eolIsSignificant(false);
    tokenizer.parseNumbers();
    tokenizer.commentChar('#');
    tokenizer.slashSlashComments(false);
    tokenizer.slashStarComments(false);
    tokenizer.lowerCaseMode(false);

    String token = null;
    Float value = null;

    while(tokenizer.nextToken() != StreamTokenizer.TT_EOF)
    {
      switch(tokenizer.ttype)
      {
        case StreamTokenizer.TT_EOL:
          break;
        case StreamTokenizer.TT_WORD:
          token = tokenizer.sval;
          break;
        case StreamTokenizer.TT_NUMBER:
          put(token,new Float((float)tokenizer.nval));
          break;
        default:
          break;
      }
    }
  }
}
