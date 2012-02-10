/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Date;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ 
/*    */ public class DateDeserializer extends StdScalarDeserializer<Date>
/*    */ {
/*    */   public DateDeserializer()
/*    */   {
/* 20 */     super(Date.class);
/*    */   }
/*    */ 
/*    */   public Date deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 26 */     return _parseDate(jp, ctxt);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.DateDeserializer
 * JD-Core Version:    0.6.0
 */