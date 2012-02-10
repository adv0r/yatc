/*     */ package org.codehaus.jackson.map.ser.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
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
/*     */ public class StringCollectionSerializer extends StaticListSerializerBase<Collection<String>>
/*     */   implements ResolvableSerializer
/*     */ {
/*     */   protected JsonSerializer<String> _serializer;
/*     */ 
/*     */   public StringCollectionSerializer(BeanProperty property)
/*     */   {
/*  35 */     super(Collection.class, property);
/*     */   }
/*     */ 
/*     */   protected JsonNode contentSchema() {
/*  39 */     return createSchemaNode("string", true);
/*     */   }
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*  46 */     JsonSerializer ser = provider.findValueSerializer(String.class, this._property);
/*  47 */     if (!isDefaultSerializer(ser))
/*  48 */       this._serializer = ser;
/*     */   }
/*     */ 
/*     */   public void serialize(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  56 */     jgen.writeStartArray();
/*  57 */     if (this._serializer == null)
/*  58 */       serializeContents(value, jgen, provider);
/*     */     else {
/*  60 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/*  62 */     jgen.writeEndArray();
/*     */   }
/*     */ 
/*     */   public void serializeWithType(Collection<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  70 */     typeSer.writeTypePrefixForArray(value, jgen);
/*  71 */     if (this._serializer == null)
/*  72 */       serializeContents(value, jgen, provider);
/*     */     else {
/*  74 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/*  76 */     typeSer.writeTypeSuffixForArray(value, jgen);
/*     */   }
/*     */ 
/*     */   private final void serializeContents(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  82 */     if (this._serializer != null) {
/*  83 */       serializeUsingCustom(value, jgen, provider);
/*  84 */       return;
/*     */     }
/*  86 */     int i = 0;
/*  87 */     for (String str : value)
/*     */       try {
/*  89 */         if (str == null)
/*  90 */           provider.defaultSerializeNull(jgen);
/*     */         else {
/*  92 */           jgen.writeString(str);
/*     */         }
/*  94 */         i++;
/*     */       } catch (Exception e) {
/*  96 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void serializeUsingCustom(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 104 */     JsonSerializer ser = this._serializer;
/* 105 */     int i = 0;
/* 106 */     for (String str : value)
/*     */       try {
/* 108 */         if (str == null)
/* 109 */           provider.defaultSerializeNull(jgen);
/*     */         else
/* 111 */           ser.serialize(str, jgen, provider);
/*     */       }
/*     */       catch (Exception e) {
/* 114 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.StringCollectionSerializer
 * JD-Core Version:    0.6.0
 */