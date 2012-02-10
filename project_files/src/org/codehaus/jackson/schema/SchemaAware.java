package org.codehaus.jackson.schema;

import java.lang.reflect.Type;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;

public abstract interface SchemaAware
{
  public abstract JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    throws JsonMappingException;
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.schema.SchemaAware
 * JD-Core Version:    0.6.0
 */