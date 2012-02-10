package org.codehaus.jackson.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

public abstract interface Indenter
{
  public abstract void writeIndentation(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException;

  public abstract boolean isInline();
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.Indenter
 * JD-Core Version:    0.6.0
 */