/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.type.CollectionType;
/*     */ import org.codehaus.jackson.map.type.MapType;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract interface Deserializers
/*     */ {
/*     */   public abstract JsonDeserializer<?> findArrayDeserializer(ArrayType paramArrayType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BeanProperty paramBeanProperty, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> findCollectionDeserializer(CollectionType paramCollectionType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BeanDescription paramBeanDescription, BeanProperty paramBeanProperty, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> findEnumDeserializer(Class<?> paramClass, DeserializationConfig paramDeserializationConfig, BeanDescription paramBeanDescription, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> findMapDeserializer(MapType paramMapType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BeanDescription paramBeanDescription, BeanProperty paramBeanProperty, KeyDeserializer paramKeyDeserializer, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> paramClass, DeserializationConfig paramDeserializationConfig, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonDeserializer<?> findBeanDeserializer(JavaType paramJavaType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BeanDescription paramBeanDescription, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public static class None
/*     */     implements Deserializers
/*     */   {
/*     */     public JsonDeserializer<?> findArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */       throws JsonMappingException
/*     */     {
/* 191 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */       throws JsonMappingException
/*     */     {
/* 202 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc, BeanProperty property)
/*     */       throws JsonMappingException
/*     */     {
/* 211 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */       throws JsonMappingException
/*     */     {
/* 223 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType, DeserializationConfig config, BeanProperty property)
/*     */       throws JsonMappingException
/*     */     {
/* 232 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property)
/*     */       throws JsonMappingException
/*     */     {
/* 242 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.Deserializers
 * JD-Core Version:    0.6.0
 */