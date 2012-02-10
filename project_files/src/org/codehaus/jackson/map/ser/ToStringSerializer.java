/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import org.codehaus.jackson.JsonGenerationException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.map.JsonMappingException;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public final class ToStringSerializer extends SerializerBase<Object>
/*    */ {
/* 25 */   public static final ToStringSerializer instance = new ToStringSerializer();
/*    */ 
/*    */   public ToStringSerializer()
/*    */   {
/* 35 */     super(Object.class);
/*    */   }
/*    */ 
/*    */   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 41 */     jgen.writeString(value.toString());
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 48 */     return createSchemaNode("string", true);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ToStringSerializer
 * JD-Core Version:    0.6.0
 */