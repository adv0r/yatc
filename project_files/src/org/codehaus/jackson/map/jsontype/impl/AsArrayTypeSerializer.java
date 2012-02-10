/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*    */ import org.codehaus.jackson.map.BeanProperty;
/*    */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*    */ 
/*    */ public class AsArrayTypeSerializer extends TypeSerializerBase
/*    */ {
/*    */   public AsArrayTypeSerializer(TypeIdResolver idRes, BeanProperty property)
/*    */   {
/* 23 */     super(idRes, property);
/*    */   }
/*    */ 
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 27 */     return JsonTypeInfo.As.WRAPPER_ARRAY;
/*    */   }
/*    */ 
/*    */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 33 */     jgen.writeStartArray();
/* 34 */     jgen.writeString(this._idResolver.idFromValue(value));
/* 35 */     jgen.writeStartObject();
/*    */   }
/*    */ 
/*    */   public void writeTypePrefixForArray(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 42 */     jgen.writeStartArray();
/* 43 */     jgen.writeString(this._idResolver.idFromValue(value));
/* 44 */     jgen.writeStartArray();
/*    */   }
/*    */ 
/*    */   public void writeTypePrefixForScalar(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 52 */     jgen.writeStartArray();
/* 53 */     jgen.writeString(this._idResolver.idFromValue(value));
/*    */   }
/*    */ 
/*    */   public void writeTypeSuffixForObject(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 60 */     jgen.writeEndObject();
/* 61 */     jgen.writeEndArray();
/*    */   }
/*    */ 
/*    */   public void writeTypeSuffixForArray(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 69 */     jgen.writeEndArray();
/* 70 */     jgen.writeEndArray();
/*    */   }
/*    */ 
/*    */   public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 78 */     jgen.writeEndArray();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.AsArrayTypeSerializer
 * JD-Core Version:    0.6.0
 */