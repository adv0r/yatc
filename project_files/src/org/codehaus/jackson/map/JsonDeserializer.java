/*    */ package org.codehaus.jackson.map;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ 
/*    */ public abstract class JsonDeserializer<T>
/*    */ {
/*    */   public abstract T deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*    */     throws IOException, JsonProcessingException;
/*    */ 
/*    */   public T deserialize(JsonParser jp, DeserializationContext ctxt, T intoValue)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 58 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 83 */     return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*    */   }
/*    */ 
/*    */   public T getNullValue()
/*    */   {
/* 99 */     return null;
/*    */   }
/*    */ 
/*    */   public static abstract class None extends JsonDeserializer<Object>
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.JsonDeserializer
 * JD-Core Version:    0.6.0
 */