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
/*    */ public final class StdKeySerializer extends SerializerBase<Object>
/*    */ {
/* 20 */   static final StdKeySerializer instace = new StdKeySerializer();
/*    */ 
/* 22 */   public StdKeySerializer() { super(Object.class);
/*    */   }
/*    */ 
/*    */   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 28 */     String keyStr = value.getClass() == String.class ? (String)value : value.toString();
/*    */ 
/* 30 */     jgen.writeFieldName(keyStr);
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 37 */     return createSchemaNode("string");
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.StdKeySerializer
 * JD-Core Version:    0.6.0
 */