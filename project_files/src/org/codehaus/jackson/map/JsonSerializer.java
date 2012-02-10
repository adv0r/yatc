/*    */ package org.codehaus.jackson.map;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ 
/*    */ public abstract class JsonSerializer<T>
/*    */ {
/*    */   public abstract void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*    */     throws IOException, JsonProcessingException;
/*    */ 
/*    */   public void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 54 */     serialize(value, jgen, provider);
/*    */   }
/*    */ 
/*    */   public Class<T> handledType()
/*    */   {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   public static abstract class None extends JsonSerializer<Object>
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.JsonSerializer
 * JD-Core Version:    0.6.0
 */