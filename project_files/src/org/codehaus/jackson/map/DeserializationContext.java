/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders;
/*     */ import org.codehaus.jackson.map.util.ObjectBuffer;
/*     */ import org.codehaus.jackson.node.JsonNodeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class DeserializationContext
/*     */ {
/*     */   protected final DeserializationConfig _config;
/*     */   protected final int _featureFlags;
/*     */ 
/*     */   protected DeserializationContext(DeserializationConfig config)
/*     */   {
/*  33 */     this._config = config;
/*  34 */     this._featureFlags = config._featureFlags;
/*     */   }
/*     */ 
/*     */   public DeserializationConfig getConfig()
/*     */   {
/*  47 */     return this._config;
/*     */   }
/*     */ 
/*     */   public DeserializerProvider getDeserializerProvider()
/*     */   {
/*  57 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isEnabled(DeserializationConfig.Feature feat)
/*     */   {
/*  68 */     return (this._featureFlags & feat.getMask()) != 0;
/*     */   }
/*     */ 
/*     */   public Base64Variant getBase64Variant()
/*     */   {
/*  80 */     return this._config.getBase64Variant();
/*     */   }
/*     */ 
/*     */   public abstract JsonParser getParser();
/*     */ 
/*     */   public final JsonNodeFactory getNodeFactory()
/*     */   {
/*  90 */     return this._config.getNodeFactory();
/*     */   }
/*     */ 
/*     */   public abstract ObjectBuffer leaseObjectBuffer();
/*     */ 
/*     */   public abstract void returnObjectBuffer(ObjectBuffer paramObjectBuffer);
/*     */ 
/*     */   public abstract ArrayBuilders getArrayBuilders();
/*     */ 
/*     */   public abstract Date parseDate(String paramString)
/*     */     throws IllegalArgumentException;
/*     */ 
/*     */   public abstract Calendar constructCalendar(Date paramDate);
/*     */ 
/*     */   public abstract boolean handleUnknownProperty(JsonParser paramJsonParser, JsonDeserializer<?> paramJsonDeserializer, Object paramObject, String paramString)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public abstract JsonMappingException mappingException(Class<?> paramClass);
/*     */ 
/*     */   public JsonMappingException mappingException(String message)
/*     */   {
/* 177 */     return JsonMappingException.from(getParser(), message);
/*     */   }
/*     */ 
/*     */   public abstract JsonMappingException instantiationException(Class<?> paramClass, Exception paramException);
/*     */ 
/*     */   public abstract JsonMappingException instantiationException(Class<?> paramClass, String paramString);
/*     */ 
/*     */   public abstract JsonMappingException weirdStringException(Class<?> paramClass, String paramString);
/*     */ 
/*     */   public abstract JsonMappingException weirdNumberException(Class<?> paramClass, String paramString);
/*     */ 
/*     */   public abstract JsonMappingException weirdKeyException(Class<?> paramClass, String paramString1, String paramString2);
/*     */ 
/*     */   public abstract JsonMappingException wrongTokenException(JsonParser paramJsonParser, JsonToken paramJsonToken, String paramString);
/*     */ 
/*     */   public abstract JsonMappingException unknownFieldException(Object paramObject, String paramString);
/*     */ 
/*     */   public abstract JsonMappingException unknownTypeException(JavaType paramJavaType, String paramString);
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.DeserializationContext
 * JD-Core Version:    0.6.0
 */