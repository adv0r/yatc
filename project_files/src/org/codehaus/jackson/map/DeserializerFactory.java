/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.type.CollectionType;
/*     */ import org.codehaus.jackson.map.type.MapType;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class DeserializerFactory
/*     */ {
/*  40 */   protected static final Deserializers[] NO_DESERIALIZERS = new Deserializers[0];
/*     */ 
/*     */   public abstract Config getConfig();
/*     */ 
/*     */   public abstract DeserializerFactory withConfig(Config paramConfig);
/*     */ 
/*     */   public final DeserializerFactory withAdditionalDeserializers(Deserializers additional)
/*     */   {
/* 112 */     return withConfig(getConfig().withAdditionalDeserializers(additional));
/*     */   }
/*     */ 
/*     */   public final DeserializerFactory withDeserializerModifier(BeanDeserializerModifier modifier)
/*     */   {
/* 122 */     return withConfig(getConfig().withDeserializerModifier(modifier));
/*     */   }
/*     */ 
/*     */   public abstract JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> createArrayDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, ArrayType paramArrayType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, CollectionType paramCollectionType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> createEnumDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> createMapDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, MapType paramMapType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> createTreeDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType, BeanProperty property)
/*     */   {
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType)
/*     */   {
/* 222 */     return findTypeDeserializer(config, baseType, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, JavaType type, DeserializerProvider p)
/*     */     throws JsonMappingException
/*     */   {
/* 237 */     return createBeanDeserializer(config, p, type, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, ArrayType type, DeserializerProvider p)
/*     */     throws JsonMappingException
/*     */   {
/* 253 */     return createArrayDeserializer(config, p, type, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig config, CollectionType type, DeserializerProvider p)
/*     */     throws JsonMappingException
/*     */   {
/* 269 */     return createCollectionDeserializer(config, p, type, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, Class<?> enumClass, DeserializerProvider p)
/*     */     throws JsonMappingException
/*     */   {
/* 285 */     return createEnumDeserializer(config, p, TypeFactory.type(enumClass), null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<?> createMapDeserializer(DeserializationConfig config, MapType type, DeserializerProvider p)
/*     */     throws JsonMappingException
/*     */   {
/* 301 */     return createMapDeserializer(config, p, type, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, Class<? extends JsonNode> nodeClass, DeserializerProvider p)
/*     */     throws JsonMappingException
/*     */   {
/* 317 */     return createTreeDeserializer(config, p, TypeFactory.type(nodeClass), null);
/*     */   }
/*     */ 
/*     */   public static abstract class Config
/*     */   {
/*     */     public abstract Config withAdditionalDeserializers(Deserializers paramDeserializers);
/*     */ 
/*     */     public abstract Config withDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier);
/*     */ 
/*     */     public abstract Iterable<Deserializers> deserializers();
/*     */ 
/*     */     public abstract Iterable<BeanDeserializerModifier> deserializerModifiers();
/*     */ 
/*     */     public abstract boolean hasDeserializers();
/*     */ 
/*     */     public abstract boolean hasDeserializerModifiers();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.DeserializerFactory
 * JD-Core Version:    0.6.0
 */