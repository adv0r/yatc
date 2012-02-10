/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.codehaus.jackson.map.JsonSerializer;
/*    */ import org.codehaus.jackson.map.SerializationConfig;
/*    */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*    */ 
/*    */ public abstract class BeanSerializerModifier
/*    */ {
/*    */   public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> beanProperties)
/*    */   {
/* 51 */     return beanProperties;
/*    */   }
/*    */ 
/*    */   public List<BeanPropertyWriter> orderProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> beanProperties)
/*    */   {
/* 66 */     return beanProperties;
/*    */   }
/*    */ 
/*    */   public BeanSerializerBuilder updateBuilder(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializerBuilder builder)
/*    */   {
/* 80 */     return builder;
/*    */   }
/*    */ 
/*    */   public JsonSerializer<?> modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, JsonSerializer<?> serializer)
/*    */   {
/* 93 */     return serializer;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BeanSerializerModifier
 * JD-Core Version:    0.6.0
 */