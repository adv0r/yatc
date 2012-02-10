/*     */ package org.codehaus.jackson.map.ser.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.ResolvableSerializer;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class IndexedStringListSerializer extends StaticListSerializerBase<List<String>>
/*     */   implements ResolvableSerializer
/*     */ {
/*     */   protected JsonSerializer<String> _serializer;
/*     */ 
/*     */   public IndexedStringListSerializer(BeanProperty property)
/*     */   {
/*  34 */     super(List.class, property);
/*     */   }
/*     */ 
/*     */   protected JsonNode contentSchema() {
/*  38 */     return createSchemaNode("string", true);
/*     */   }
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*  45 */     JsonSerializer ser = provider.findValueSerializer(String.class, this._property);
/*  46 */     if (!isDefaultSerializer(ser))
/*  47 */       this._serializer = ser;
/*     */   }
/*     */ 
/*     */   public void serialize(List<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  55 */     jgen.writeStartArray();
/*  56 */     if (this._serializer == null)
/*  57 */       serializeContents(value, jgen, provider);
/*     */     else {
/*  59 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/*  61 */     jgen.writeEndArray();
/*     */   }
/*     */ 
/*     */   public void serializeWithType(List<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  69 */     typeSer.writeTypePrefixForArray(value, jgen);
/*  70 */     if (this._serializer == null)
/*  71 */       serializeContents(value, jgen, provider);
/*     */     else {
/*  73 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/*  75 */     typeSer.writeTypeSuffixForArray(value, jgen);
/*     */   }
/*     */ 
/*     */   private final void serializeContents(List<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  81 */     int i = 0;
/*     */     try {
/*  83 */       int len = value.size();
/*  84 */       for (; i < len; i++) {
/*  85 */         String str = (String)value.get(i);
/*  86 */         if (str == null)
/*  87 */           provider.defaultSerializeNull(jgen);
/*     */         else
/*  89 */           jgen.writeString(str);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  93 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void serializeUsingCustom(List<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 100 */     int i = 0;
/*     */     try {
/* 102 */       int len = value.size();
/* 103 */       JsonSerializer ser = this._serializer;
/* 104 */       for (i = 0; i < len; i++) {
/* 105 */         String str = (String)value.get(i);
/* 106 */         if (str == null)
/* 107 */           provider.defaultSerializeNull(jgen);
/*     */         else
/* 109 */           ser.serialize(str, jgen, provider);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 113 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.IndexedStringListSerializer
 * JD-Core Version:    0.6.0
 */