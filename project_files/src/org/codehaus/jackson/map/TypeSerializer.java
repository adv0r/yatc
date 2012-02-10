package org.codehaus.jackson.map;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;

public abstract class TypeSerializer
{
  public abstract JsonTypeInfo.As getTypeInclusion();

  public abstract String getPropertyName();

  public abstract TypeIdResolver getTypeIdResolver();

  public abstract void writeTypePrefixForScalar(Object paramObject, JsonGenerator paramJsonGenerator)
    throws IOException, JsonProcessingException;

  public abstract void writeTypePrefixForObject(Object paramObject, JsonGenerator paramJsonGenerator)
    throws IOException, JsonProcessingException;

  public abstract void writeTypePrefixForArray(Object paramObject, JsonGenerator paramJsonGenerator)
    throws IOException, JsonProcessingException;

  public abstract void writeTypeSuffixForScalar(Object paramObject, JsonGenerator paramJsonGenerator)
    throws IOException, JsonProcessingException;

  public abstract void writeTypeSuffixForObject(Object paramObject, JsonGenerator paramJsonGenerator)
    throws IOException, JsonProcessingException;

  public abstract void writeTypeSuffixForArray(Object paramObject, JsonGenerator paramJsonGenerator)
    throws IOException, JsonProcessingException;
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.TypeSerializer
 * JD-Core Version:    0.6.0
 */