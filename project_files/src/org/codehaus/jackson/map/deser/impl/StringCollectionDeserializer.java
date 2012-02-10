/*     */ package org.codehaus.jackson.map.deser.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Collection;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.deser.ContainerDeserializer;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class StringCollectionDeserializer extends ContainerDeserializer<Collection<String>>
/*     */ {
/*     */   protected final JavaType _collectionType;
/*     */   protected final JsonDeserializer<String> _valueDeserializer;
/*     */   protected final boolean _isDefaultDeserializer;
/*     */   final Constructor<Collection<String>> _defaultCtor;
/*     */ 
/*     */   public StringCollectionDeserializer(JavaType collectionType, JsonDeserializer<?> valueDeser, Constructor<?> ctor)
/*     */   {
/*  48 */     super(collectionType.getRawClass());
/*  49 */     this._collectionType = collectionType;
/*  50 */     this._valueDeserializer = valueDeser;
/*  51 */     this._defaultCtor = ctor;
/*  52 */     this._isDefaultDeserializer = isDefaultSerializer(valueDeser);
/*     */   }
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/*  63 */     return this._collectionType.getContentType();
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/*  69 */     JsonDeserializer deser = this._valueDeserializer;
/*  70 */     return deser;
/*     */   }
/*     */ 
/*     */   public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  84 */     if (!jp.isExpectedStartArrayToken())
/*  85 */       throw ctxt.mappingException(this._collectionType.getRawClass());
/*     */     Collection result;
/*     */     try
/*     */     {
/*  90 */       result = (Collection)this._defaultCtor.newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/*  92 */       throw ctxt.instantiationException(this._collectionType.getRawClass(), e);
/*     */     }
/*  94 */     return deserialize(jp, ctxt, result);
/*     */   }
/*     */ 
/*     */   public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<String> result)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 102 */     if (!this._isDefaultDeserializer)
/* 103 */       return deserializeUsingCustom(jp, ctxt, result);
/*     */     JsonToken t;
/* 107 */     while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
/* 108 */       result.add(t == JsonToken.VALUE_NULL ? null : jp.getText());
/*     */     }
/* 110 */     return result;
/*     */   }
/*     */ 
/*     */   private Collection<String> deserializeUsingCustom(JsonParser jp, DeserializationContext ctxt, Collection<String> result)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 118 */     JsonDeserializer deser = this._valueDeserializer;
/*     */     JsonToken t;
/* 120 */     while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*     */     {
/*     */       String value;
/*     */       String value;
/* 123 */       if (t == JsonToken.VALUE_NULL)
/* 124 */         value = null;
/*     */       else {
/* 126 */         value = (String)deser.deserialize(jp, ctxt);
/*     */       }
/* 128 */       result.add(value);
/*     */     }
/* 130 */     return result;
/*     */   }
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 139 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.impl.StringCollectionDeserializer
 * JD-Core Version:    0.6.0
 */