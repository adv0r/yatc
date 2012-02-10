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
/*    */ public class NullSerializer extends SerializerBase<Object>
/*    */ {
/* 21 */   public static final NullSerializer instance = new NullSerializer();
/*    */ 
/* 23 */   private NullSerializer() { super(Object.class);
/*    */   }
/*    */ 
/*    */   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 29 */     jgen.writeNull();
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 36 */     return createSchemaNode("null");
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.NullSerializer
 * JD-Core Version:    0.6.0
 */