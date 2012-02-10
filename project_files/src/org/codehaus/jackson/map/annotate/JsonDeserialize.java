package org.codehaus.jackson.map.annotate;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.KeyDeserializer;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonDeserialize
{
  public abstract Class<? extends JsonDeserializer<?>> using();

  public abstract Class<? extends JsonDeserializer<?>> contentUsing();

  public abstract Class<? extends KeyDeserializer> keyUsing();

  public abstract Class<?> as();

  public abstract Class<?> keyAs();

  public abstract Class<?> contentAs();
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.annotate.JsonDeserialize
 * JD-Core Version:    0.6.0
 */