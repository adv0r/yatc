/*     */ package org.codehaus.jackson.map.jsontype.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class AsArrayTypeDeserializer extends TypeDeserializerBase
/*     */ {
/*     */   public AsArrayTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property)
/*     */   {
/*  24 */     super(bt, idRes, property);
/*     */   }
/*     */ 
/*     */   public JsonTypeInfo.As getTypeInclusion()
/*     */   {
/*  29 */     return JsonTypeInfo.As.WRAPPER_ARRAY;
/*     */   }
/*     */ 
/*     */   public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  39 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  49 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  56 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  63 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   private final Object _deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  80 */     JsonDeserializer deser = _findDeserializer(ctxt, _locateTypeId(jp, ctxt));
/*  81 */     Object value = deser.deserialize(jp, ctxt);
/*     */ 
/*  83 */     if (jp.nextToken() != JsonToken.END_ARRAY) {
/*  84 */       throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "expected closing END_ARRAY after type information and deserialized value");
/*     */     }
/*     */ 
/*  87 */     return value;
/*     */   }
/*     */ 
/*     */   protected final String _locateTypeId(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  93 */     if (!jp.isExpectedStartArrayToken()) {
/*  94 */       throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_ARRAY type information for class " + baseTypeName());
/*     */     }
/*     */ 
/*  98 */     if (jp.nextToken() != JsonToken.VALUE_STRING) {
/*  99 */       throw ctxt.wrongTokenException(jp, JsonToken.VALUE_STRING, "need JSON String that contains type id (for subtype of " + baseTypeName() + ")");
/*     */     }
/*     */ 
/* 102 */     String result = jp.getText();
/* 103 */     jp.nextToken();
/* 104 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.AsArrayTypeDeserializer
 * JD-Core Version:    0.6.0
 */