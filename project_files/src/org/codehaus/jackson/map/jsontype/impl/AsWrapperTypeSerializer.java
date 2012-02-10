/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*    */ import org.codehaus.jackson.map.BeanProperty;
/*    */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*    */ 
/*    */ public class AsWrapperTypeSerializer extends TypeSerializerBase
/*    */ {
/*    */   public AsWrapperTypeSerializer(TypeIdResolver idRes, BeanProperty property)
/*    */   {
/* 27 */     super(idRes, property);
/*    */   }
/*    */ 
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 31 */     return JsonTypeInfo.As.WRAPPER_OBJECT;
/*    */   }
/*    */ 
/*    */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 38 */     jgen.writeStartObject();
/*    */ 
/* 40 */     jgen.writeObjectFieldStart(this._idResolver.idFromValue(value));
/*    */   }
/*    */ 
/*    */   public void writeTypePrefixForArray(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 48 */     jgen.writeStartObject();
/*    */ 
/* 50 */     jgen.writeArrayFieldStart(this._idResolver.idFromValue(value));
/*    */   }
/*    */ 
/*    */   public void writeTypePrefixForScalar(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 58 */     jgen.writeStartObject();
/* 59 */     jgen.writeFieldName(this._idResolver.idFromValue(value));
/*    */   }
/*    */ 
/*    */   public void writeTypeSuffixForObject(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 67 */     jgen.writeEndObject();
/*    */ 
/* 69 */     jgen.writeEndObject();
/*    */   }
/*    */ 
/*    */   public void writeTypeSuffixForArray(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 77 */     jgen.writeEndArray();
/*    */ 
/* 79 */     jgen.writeEndObject();
/*    */   }
/*    */ 
/*    */   public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 87 */     jgen.writeEndObject();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.AsWrapperTypeSerializer
 * JD-Core Version:    0.6.0
 */