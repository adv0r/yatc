package org.codehaus.jackson.map;

public abstract interface ContextualDeserializer<T>
{
  public abstract JsonDeserializer<T> createContextual(DeserializationConfig paramDeserializationConfig, BeanProperty paramBeanProperty)
    throws JsonMappingException;
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ContextualDeserializer
 * JD-Core Version:    0.6.0
 */