/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ import org.codehaus.jackson.map.TypeDeserializer;
/*    */ 
/*    */ public abstract class StdScalarDeserializer<T> extends StdDeserializer<T>
/*    */ {
/*    */   protected StdScalarDeserializer(Class<?> vc)
/*    */   {
/* 19 */     super(vc);
/*    */   }
/*    */ 
/*    */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 27 */     return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdScalarDeserializer
 * JD-Core Version:    0.6.0
 */