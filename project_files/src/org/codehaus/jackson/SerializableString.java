package org.codehaus.jackson;

public abstract interface SerializableString
{
  public abstract String getValue();

  public abstract int charLength();

  public abstract char[] asQuotedChars();

  public abstract byte[] asUnquotedUTF8();

  public abstract byte[] asQuotedUTF8();
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.SerializableString
 * JD-Core Version:    0.6.0
 */