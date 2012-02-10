/*    */ package org.codehaus.jackson.map.ser.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import org.codehaus.jackson.JsonGenerationException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.TypeSerializer;
/*    */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*    */ import org.codehaus.jackson.map.ser.SerializerBase;
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class RawSerializer<T> extends SerializerBase<T>
/*    */ {
/*    */   public RawSerializer(Class<?> cls)
/*    */   {
/* 27 */     super(cls, false);
/*    */   }
/*    */ 
/*    */   public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 34 */     jgen.writeRawValue(value.toString());
/*    */   }
/*    */ 
/*    */   public void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 42 */     typeSer.writeTypePrefixForScalar(value, jgen);
/* 43 */     serialize(value, jgen, provider);
/* 44 */     typeSer.writeTypeSuffixForScalar(value, jgen);
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 51 */     return createSchemaNode("string", true);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.RawSerializer
 * JD-Core Version:    0.6.0
 */