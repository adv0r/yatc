/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import org.codehaus.jackson.JsonGenerationException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.map.JsonMappingException;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ 
/*    */ public final class FailingSerializer extends SerializerBase<Object>
/*    */ {
/*    */   final String _msg;
/*    */ 
/*    */   public FailingSerializer(String msg)
/*    */   {
/* 25 */     super(Object.class);
/* 26 */     this._msg = msg;
/*    */   }
/*    */ 
/*    */   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 33 */     throw new JsonGenerationException(this._msg);
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.FailingSerializer
 * JD-Core Version:    0.6.0
 */