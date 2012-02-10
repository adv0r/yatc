package org.codehaus.jackson.map.jsontype;

import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.type.JavaType;

public abstract interface TypeIdResolver
{
  public abstract void init(JavaType paramJavaType);

  public abstract String idFromValue(Object paramObject);

  public abstract JavaType typeFromId(String paramString);

  public abstract JsonTypeInfo.Id getMechanism();
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.TypeIdResolver
 * JD-Core Version:    0.6.0
 */