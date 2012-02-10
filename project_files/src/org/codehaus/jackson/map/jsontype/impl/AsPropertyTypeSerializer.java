/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*    */ import org.codehaus.jackson.map.BeanProperty;
/*    */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*    */ 
/*    */ public class AsPropertyTypeSerializer extends AsArrayTypeSerializer
/*    */ {
/*    */   protected final String _typePropertyName;
/*    */ 
/*    */   public AsPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName)
/*    */   {
/* 29 */     super(idRes, property);
/* 30 */     this._typePropertyName = propName;
/*    */   }
/*    */ 
/*    */   public String getPropertyName() {
/* 34 */     return this._typePropertyName;
/*    */   }
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 37 */     return JsonTypeInfo.As.PROPERTY;
/*    */   }
/*    */ 
/*    */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 43 */     jgen.writeStartObject();
/* 44 */     jgen.writeStringField(this._typePropertyName, this._idResolver.idFromValue(value));
/*    */   }
/*    */ 
/*    */   public void writeTypeSuffixForObject(Object value, JsonGenerator jgen)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 54 */     jgen.writeEndObject();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.AsPropertyTypeSerializer
 * JD-Core Version:    0.6.0
 */