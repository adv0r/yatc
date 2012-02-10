package org.codehaus.jackson.map;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;

public abstract class Module
  implements Versioned
{
  public abstract String getModuleName();

  public abstract Version version();

  public abstract void setupModule(SetupContext paramSetupContext);

  public static abstract interface SetupContext
  {
    public abstract Version getMapperVersion();

    public abstract DeserializationConfig getDeserializationConfig();

    public abstract SerializationConfig getSerializationConfig();

    @Deprecated
    public abstract SerializationConfig getSeserializationConfig();

    public abstract void addDeserializers(Deserializers paramDeserializers);

    public abstract void addSerializers(Serializers paramSerializers);

    public abstract void addBeanDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier);

    public abstract void addBeanSerializerModifier(BeanSerializerModifier paramBeanSerializerModifier);

    public abstract void insertAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);

    public abstract void appendAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);

    public abstract void setMixInAnnotations(Class<?> paramClass1, Class<?> paramClass2);
  }
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.Module
 * JD-Core Version:    0.6.0
 */