package org.codehaus.jackson.map.jsontype;

import java.util.Collection;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.type.JavaType;

public abstract interface TypeResolverBuilder<T extends TypeResolverBuilder<T>>
{
  public abstract TypeSerializer buildTypeSerializer(JavaType paramJavaType, Collection<NamedType> paramCollection, BeanProperty paramBeanProperty);

  public abstract TypeDeserializer buildTypeDeserializer(JavaType paramJavaType, Collection<NamedType> paramCollection, BeanProperty paramBeanProperty);

  public abstract T init(JsonTypeInfo.Id paramId, TypeIdResolver paramTypeIdResolver);

  public abstract T inclusion(JsonTypeInfo.As paramAs);

  public abstract T typeProperty(String paramString);
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.TypeResolverBuilder
 * JD-Core Version:    0.6.0
 */