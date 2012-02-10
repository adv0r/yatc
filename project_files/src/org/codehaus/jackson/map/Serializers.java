package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

public abstract interface Serializers
{
  public abstract JsonSerializer<?> findSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanDescription paramBeanDescription, BeanProperty paramBeanProperty);
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.Serializers
 * JD-Core Version:    0.6.0
 */