package org.codehaus.jackson;

import java.io.IOException;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public abstract class ObjectCodec
{
  public abstract <T> T readValue(JsonParser paramJsonParser, Class<T> paramClass)
    throws IOException, JsonProcessingException;

  public abstract <T> T readValue(JsonParser paramJsonParser, TypeReference<?> paramTypeReference)
    throws IOException, JsonProcessingException;

  public abstract <T> T readValue(JsonParser paramJsonParser, JavaType paramJavaType)
    throws IOException, JsonProcessingException;

  public abstract JsonNode readTree(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException;

  public abstract void writeValue(JsonGenerator paramJsonGenerator, Object paramObject)
    throws IOException, JsonProcessingException;

  public abstract void writeTree(JsonGenerator paramJsonGenerator, JsonNode paramJsonNode)
    throws IOException, JsonProcessingException;

  public abstract JsonNode createObjectNode();

  public abstract JsonNode createArrayNode();

  public abstract JsonParser treeAsTokens(JsonNode paramJsonNode);

  public abstract <T> T treeToValue(JsonNode paramJsonNode, Class<T> paramClass)
    throws IOException, JsonProcessingException;
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.ObjectCodec
 * JD-Core Version:    0.6.0
 */