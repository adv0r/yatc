/*     */ package org.codehaus.jackson.map.deser;
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
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class CollectionDeserializer extends ContainerDeserializer<Collection<Object>>
/*     */ {
/*     */   protected final JavaType _collectionType;
/*     */   final JsonDeserializer<Object> _valueDeserializer;
/*     */   final TypeDeserializer _valueTypeDeserializer;
/*     */   final Constructor<Collection<Object>> _defaultCtor;
/*     */ 
/*     */   public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, Constructor<Collection<Object>> ctor)
/*     */   {
/*  54 */     super(collectionType.getRawClass());
/*  55 */     this._collectionType = collectionType;
/*  56 */     this._valueDeserializer = valueDeser;
/*  57 */     this._valueTypeDeserializer = valueTypeDeser;
/*  58 */     if (ctor == null) {
/*  59 */       throw new IllegalArgumentException("No default constructor found for container class " + collectionType.getRawClass().getName());
/*     */     }
/*  61 */     this._defaultCtor = ctor;
/*     */   }
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/*  72 */     return this._collectionType.getContentType();
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/*  77 */     return this._valueDeserializer;
/*     */   }
/*     */ 
/*     */   public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*     */     Collection result;
/*     */     try
/*     */     {
/*  92 */       result = (Collection)this._defaultCtor.newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/*  94 */       throw ctxt.instantiationException(this._collectionType.getRawClass(), e);
/*     */     }
/*  96 */     return deserialize(jp, ctxt, result);
/*     */   }
/*     */ 
/*     */   public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<Object> result)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 105 */     if (!jp.isExpectedStartArrayToken()) {
/* 106 */       throw ctxt.mappingException(this._collectionType.getRawClass());
/*     */     }
/*     */ 
/* 109 */     JsonDeserializer valueDes = this._valueDeserializer;
/*     */ 
/* 111 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     JsonToken t;
/* 113 */     while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*     */     {
/*     */       Object value;
/*     */       Object value;
/* 116 */       if (t == JsonToken.VALUE_NULL) {
/* 117 */         value = null;
/*     */       }
/*     */       else
/*     */       {
/*     */         Object value;
/* 118 */         if (typeDeser == null)
/* 119 */           value = valueDes.deserialize(jp, ctxt);
/*     */         else
/* 121 */           value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */       }
/* 123 */       result.add(value);
/*     */     }
/* 125 */     return result;
/*     */   }
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 134 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.CollectionDeserializer
 * JD-Core Version:    0.6.0
 */