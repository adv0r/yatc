/*     */ package org.codehaus.jackson.map.module;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.BeanDescription;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.Deserializers;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.KeyDeserializer;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.type.ClassKey;
/*     */ import org.codehaus.jackson.map.type.CollectionType;
/*     */ import org.codehaus.jackson.map.type.MapType;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class SimpleDeserializers
/*     */   implements Deserializers
/*     */ {
/*  27 */   protected HashMap<ClassKey, JsonDeserializer<?>> _classMappings = null;
/*     */ 
/*     */   public <T> void addDeserializer(Class<T> forClass, JsonDeserializer<? extends T> deser)
/*     */   {
/*  39 */     ClassKey key = new ClassKey(forClass);
/*  40 */     if (this._classMappings == null) {
/*  41 */       this._classMappings = new HashMap();
/*     */     }
/*  43 */     this._classMappings.put(key, deser);
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> findArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */   {
/*  59 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property)
/*     */   {
/*  67 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */   {
/*  77 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc, BeanProperty property)
/*     */   {
/*  84 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type));
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */   {
/*  95 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType, DeserializationConfig config, BeanProperty property)
/*     */   {
/* 102 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(nodeType));
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.module.SimpleDeserializers
 * JD-Core Version:    0.6.0
 */