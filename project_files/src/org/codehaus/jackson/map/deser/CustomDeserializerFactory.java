/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializerFactory;
/*     */ import org.codehaus.jackson.map.DeserializerFactory.Config;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.type.ClassKey;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class CustomDeserializerFactory extends BeanDeserializerFactory
/*     */ {
/*  50 */   protected HashMap<ClassKey, JsonDeserializer<Object>> _directClassMappings = null;
/*     */   protected HashMap<ClassKey, Class<?>> _mixInAnnotations;
/*     */ 
/*     */   public CustomDeserializerFactory()
/*     */   {
/*  80 */     this(null);
/*     */   }
/*     */ 
/*     */   protected CustomDeserializerFactory(DeserializerFactory.Config config)
/*     */   {
/*  85 */     super(config);
/*     */   }
/*     */ 
/*     */   public DeserializerFactory withConfig(DeserializerFactory.Config config)
/*     */   {
/*  92 */     if (getClass() != CustomDeserializerFactory.class) {
/*  93 */       throw new IllegalStateException("Subtype of CustomDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
/*     */     }
/*     */ 
/*  97 */     return new CustomDeserializerFactory(config);
/*     */   }
/*     */ 
/*     */   public <T> void addSpecificMapping(Class<T> forClass, JsonDeserializer<? extends T> deser)
/*     */   {
/* 125 */     ClassKey key = new ClassKey(forClass);
/* 126 */     if (this._directClassMappings == null) {
/* 127 */       this._directClassMappings = new HashMap();
/*     */     }
/* 129 */     this._directClassMappings.put(key, deser);
/*     */   }
/*     */ 
/*     */   public void addMixInAnnotationMapping(Class<?> destinationClass, Class<?> classWithMixIns)
/*     */   {
/* 150 */     if (this._mixInAnnotations == null) {
/* 151 */       this._mixInAnnotations = new HashMap();
/*     */     }
/* 153 */     this._mixInAnnotations.put(new ClassKey(destinationClass), classWithMixIns);
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 167 */     Class cls = type.getRawClass();
/* 168 */     ClassKey key = new ClassKey(cls);
/*     */ 
/* 171 */     if (this._directClassMappings != null) {
/* 172 */       JsonDeserializer deser = (JsonDeserializer)this._directClassMappings.get(key);
/* 173 */       if (deser != null) {
/* 174 */         return deser;
/*     */       }
/*     */     }
/*     */ 
/* 178 */     return super.createBeanDeserializer(config, p, type, property);
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, DeserializerProvider p, ArrayType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 186 */     ClassKey key = new ClassKey(type.getRawClass());
/* 187 */     if (this._directClassMappings != null) {
/* 188 */       JsonDeserializer deser = (JsonDeserializer)this._directClassMappings.get(key);
/* 189 */       if (deser != null) {
/* 190 */         return deser;
/*     */       }
/*     */     }
/* 193 */     return super.createArrayDeserializer(config, p, type, property);
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType enumType, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 207 */     if (this._directClassMappings != null) {
/* 208 */       ClassKey key = new ClassKey(enumType.getRawClass());
/* 209 */       JsonDeserializer deser = (JsonDeserializer)this._directClassMappings.get(key);
/* 210 */       if (deser != null) {
/* 211 */         return deser;
/*     */       }
/*     */     }
/* 214 */     return super.createEnumDeserializer(config, p, enumType, property);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.CustomDeserializerFactory
 * JD-Core Version:    0.6.0
 */