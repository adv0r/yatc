package it.unibz.twitter.utils;

import it.unibz.twitter.model.Tweet;
import java.util.List;

public abstract interface Parser
{
  public abstract List<Tweet> parseTimeLine(String paramString)
    throws Exception;

  public abstract Tweet parseTweetResponse(String paramString)
    throws Exception;

  public abstract String parseUserInfo(String paramString)
    throws Exception;

  public abstract List<Tweet> parseDirects(String paramString)
    throws Exception;
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.utils.Parser
 * JD-Core Version:    0.6.0
 */