package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.Map;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.type.JavaType;

public abstract interface MapperConfig<T extends MapperConfig<T>> extends ClassIntrospector.MixInResolver
{
  public abstract void fromAnnotations(Class<?> paramClass);

  public abstract T createUnshared(TypeResolverBuilder<?> paramTypeResolverBuilder, VisibilityChecker<?> paramVisibilityChecker, SubtypeResolver paramSubtypeResolver);

  public abstract AnnotationIntrospector getAnnotationIntrospector();

  public abstract void setAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);

  public abstract void insertAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);

  public abstract void appendAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);

  public abstract void setIntrospector(ClassIntrospector<? extends BeanDescription> paramClassIntrospector);

  public abstract void setMixInAnnotations(Map<Class<?>, Class<?>> paramMap);

  public abstract void addMixInAnnotations(Class<?> paramClass1, Class<?> paramClass2);

  public abstract Class<?> findMixInClassFor(Class<?> paramClass);

  public abstract DateFormat getDateFormat();

  public abstract void setDateFormat(DateFormat paramDateFormat);

  public abstract TypeResolverBuilder<?> getDefaultTyper(JavaType paramJavaType);

  public abstract VisibilityChecker<?> getDefaultVisibilityChecker();

  public abstract SubtypeResolver getSubtypeResolver();

  public abstract void setSubtypeResolver(SubtypeResolver paramSubtypeResolver);

  public abstract <DESC extends BeanDescription> DESC introspectClassAnnotations(Class<?> paramClass);

  public abstract <DESC extends BeanDescription> DESC introspectDirectClassAnnotations(Class<?> paramClass);
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.MapperConfig
 * JD-Core Version:    0.6.0
 */