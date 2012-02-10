/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import org.codehaus.jackson.map.DeserializationConfig;
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*    */ 
/*    */ public abstract class BeanDeserializerModifier
/*    */ {
/*    */   public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder)
/*    */   {
/* 36 */     return builder;
/*    */   }
/*    */ 
/*    */   public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BasicBeanDescription beanDesc, JsonDeserializer<?> deserializer)
/*    */   {
/* 49 */     return deserializer;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BeanDeserializerModifier
 * JD-Core Version:    0.6.0
 */