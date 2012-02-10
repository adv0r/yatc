/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class DeserializerProvider
/*     */ {
/*     */   public abstract DeserializerProvider withAdditionalDeserializers(Deserializers paramDeserializers);
/*     */ 
/*     */   public abstract DeserializerProvider withDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier);
/*     */ 
/*     */   public abstract JsonDeserializer<Object> findValueDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract KeyDeserializer findKeyDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract boolean hasValueDeserializerFor(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType);
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<Object> findValueDeserializer(DeserializationConfig config, JavaType type, JavaType referrer, String refPropName)
/*     */     throws JsonMappingException
/*     */   {
/* 112 */     return findValueDeserializer(config, type, (BeanProperty)null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig config, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 130 */     return findTypedValueDeserializer(config, type, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final KeyDeserializer findKeyDeserializer(DeserializationConfig config, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 147 */     return findKeyDeserializer(config, type, null);
/*     */   }
/*     */ 
/*     */   public abstract int cachedDeserializersCount();
/*     */ 
/*     */   public abstract void flushCachedDeserializers();
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.DeserializerProvider
 * JD-Core Version:    0.6.0
 */