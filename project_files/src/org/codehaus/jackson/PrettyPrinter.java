package org.codehaus.jackson;

import java.io.IOException;

public abstract interface PrettyPrinter
{
  public abstract void writeRootValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeStartObject(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeEndObject(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException;

  public abstract void writeObjectEntrySeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeObjectFieldValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeStartArray(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeEndArray(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException;

  public abstract void writeArrayValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void beforeArrayValues(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void beforeObjectEntries(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.PrettyPrinter
 * JD-Core Version:    0.6.0
 */