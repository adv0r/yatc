package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

public abstract class ClassIntrospector<T extends BeanDescription>
{
  public abstract T forSerialization(SerializationConfig paramSerializationConfig, JavaType paramJavaType, MixInResolver paramMixInResolver);

  public abstract T forDeserialization(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, MixInResolver paramMixInResolver);

  public abstract T forCreation(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, MixInResolver paramMixInResolver);

  public abstract T forClassAnnotations(MapperConfig<?> paramMapperConfig, Class<?> paramClass, MixInResolver paramMixInResolver);

  public abstract T forDirectClassAnnotations(MapperConfig<?> paramMapperConfig, Class<?> paramClass, MixInResolver paramMixInResolver);

  public static abstract interface MixInResolver
  {
    public abstract Class<?> findMixInClassFor(Class<?> paramClass);
  }
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ClassIntrospector
 * JD-Core Version:    0.6.0
 */