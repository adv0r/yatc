/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.sql.Timestamp;
/*    */ import java.util.Date;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ 
/*    */ public class TimestampDeserializer extends StdScalarDeserializer<Timestamp>
/*    */ {
/*    */   public TimestampDeserializer()
/*    */   {
/* 20 */     super(Timestamp.class);
/*    */   }
/*    */ 
/*    */   public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 26 */     return new Timestamp(_parseDate(jp, ctxt).getTime());
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.TimestampDeserializer
 * JD-Core Version:    0.6.0
 */