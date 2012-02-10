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
/*     */ import org.codehaus.jackson.util.JsonParserSequence;
/*     */ import org.codehaus.jackson.util.TokenBuffer;
/*     */ 
/*     */ public class AsPropertyTypeDeserializer extends AsArrayTypeDeserializer
/*     */ {
/*     */   protected final String _typePropertyName;
/*     */ 
/*     */   public AsPropertyTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property, String typePropName)
/*     */   {
/*  31 */     super(bt, idRes, property);
/*  32 */     this._typePropertyName = typePropName;
/*     */   }
/*     */ 
/*     */   public JsonTypeInfo.As getTypeInclusion()
/*     */   {
/*  37 */     return JsonTypeInfo.As.PROPERTY;
/*     */   }
/*     */ 
/*     */   public String getPropertyName() {
/*  41 */     return this._typePropertyName;
/*     */   }
/*     */ 
/*     */   public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  52 */     JsonToken t = jp.getCurrentToken();
/*  53 */     if (t == JsonToken.START_OBJECT)
/*  54 */       t = jp.nextToken();
/*  55 */     else if (t != JsonToken.FIELD_NAME) {
/*  56 */       throw ctxt.wrongTokenException(jp, JsonToken.START_OBJECT, "need JSON Object to contain As.PROPERTY type information (for class " + baseTypeName() + ")");
/*     */     }
/*     */ 
/*  60 */     TokenBuffer tb = null;
/*     */ 
/*  62 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/*  63 */       String name = jp.getCurrentName();
/*  64 */       jp.nextToken();
/*  65 */       if (this._typePropertyName.equals(name)) {
/*  66 */         JsonDeserializer deser = _findDeserializer(ctxt, jp.getText());
/*     */ 
/*  68 */         if (tb != null) {
/*  69 */           jp = JsonParserSequence.createFlattened(tb.asParser(jp), jp);
/*     */         }
/*     */ 
/*  74 */         jp.nextToken();
/*     */ 
/*  76 */         return deser.deserialize(jp, ctxt);
/*     */       }
/*  78 */       if (tb == null) {
/*  79 */         tb = new TokenBuffer(null);
/*     */       }
/*  81 */       tb.writeFieldName(name);
/*  82 */       tb.copyCurrentStructure(jp);
/*     */     }
/*     */ 
/*  85 */     throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "missing property '" + this._typePropertyName + "' that is to contain type id  (for class " + baseTypeName() + ")");
/*     */   }
/*     */ 
/*     */   public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 100 */     if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
/* 101 */       return super.deserializeTypedFromArray(jp, ctxt);
/*     */     }
/* 103 */     return deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.AsPropertyTypeDeserializer
 * JD-Core Version:    0.6.0
 */