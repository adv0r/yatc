package org.codehaus.jackson.map;

public abstract interface ResolvableSerializer
{
  public abstract void resolve(SerializerProvider paramSerializerProvider)
    throws JsonMappingException;
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ResolvableSerializer
 * JD-Core Version:    0.6.0
 */