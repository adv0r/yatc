/*    */ package org.codehaus.jackson.map;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ 
/*    */ public abstract class DeserializationProblemHandler
/*    */ {
/*    */   public boolean handleUnknownProperty(DeserializationContext ctxt, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 54 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.DeserializationProblemHandler
 * JD-Core Version:    0.6.0
 */