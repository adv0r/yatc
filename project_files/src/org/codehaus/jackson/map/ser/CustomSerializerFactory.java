/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.SerializerFactory;
/*     */ import org.codehaus.jackson.map.SerializerFactory.Config;
/*     */ import org.codehaus.jackson.map.type.ClassKey;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class CustomSerializerFactory extends BeanSerializerFactory
/*     */ {
/*  56 */   protected HashMap<ClassKey, JsonSerializer<?>> _directClassMappings = null;
/*     */   protected JsonSerializer<?> _enumSerializerOverride;
/*  75 */   protected HashMap<ClassKey, JsonSerializer<?>> _transitiveClassMappings = null;
/*     */ 
/*  80 */   protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings = null;
/*     */ 
/*     */   public CustomSerializerFactory()
/*     */   {
/*  89 */     this(null);
/*     */   }
/*     */ 
/*     */   public CustomSerializerFactory(SerializerFactory.Config config) {
/*  93 */     super(config);
/*     */   }
/*     */ 
/*     */   public SerializerFactory withConfig(SerializerFactory.Config config)
/*     */   {
/* 102 */     if (getClass() != CustomSerializerFactory.class) {
/* 103 */       throw new IllegalStateException("Subtype of CustomSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
/*     */     }
/*     */ 
/* 107 */     return new CustomSerializerFactory(config);
/*     */   }
/*     */ 
/*     */   public <T> void addGenericMapping(Class<? extends T> type, JsonSerializer<T> ser)
/*     */   {
/* 137 */     ClassKey key = new ClassKey(type);
/* 138 */     if (type.isInterface()) {
/* 139 */       if (this._interfaceMappings == null) {
/* 140 */         this._interfaceMappings = new HashMap();
/*     */       }
/* 142 */       this._interfaceMappings.put(key, ser);
/*     */     } else {
/* 144 */       if (this._transitiveClassMappings == null) {
/* 145 */         this._transitiveClassMappings = new HashMap();
/*     */       }
/* 147 */       this._transitiveClassMappings.put(key, ser);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> void addSpecificMapping(Class<? extends T> forClass, JsonSerializer<T> ser)
/*     */   {
/* 165 */     ClassKey key = new ClassKey(forClass);
/*     */ 
/* 171 */     if (forClass.isInterface()) {
/* 172 */       throw new IllegalArgumentException("Can not add specific mapping for an interface (" + forClass.getName() + ")");
/*     */     }
/* 174 */     if (Modifier.isAbstract(forClass.getModifiers())) {
/* 175 */       throw new IllegalArgumentException("Can not add specific mapping for an abstract class (" + forClass.getName() + ")");
/*     */     }
/*     */ 
/* 178 */     if (this._directClassMappings == null) {
/* 179 */       this._directClassMappings = new HashMap();
/*     */     }
/* 181 */     this._directClassMappings.put(key, ser);
/*     */   }
/*     */ 
/*     */   public void setEnumSerializer(JsonSerializer<?> enumSer)
/*     */   {
/* 198 */     this._enumSerializerOverride = enumSer;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property)
/*     */   {
/* 212 */     JsonSerializer ser = findCustomSerializer(type.getRawClass(), config);
/* 213 */     if (ser != null) {
/* 214 */       return ser;
/*     */     }
/* 216 */     return super.createSerializer(config, type, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> findCustomSerializer(Class<?> type, SerializationConfig config)
/*     */   {
/* 227 */     JsonSerializer ser = null;
/* 228 */     ClassKey key = new ClassKey(type);
/*     */ 
/* 231 */     if (this._directClassMappings != null) {
/* 232 */       ser = (JsonSerializer)this._directClassMappings.get(key);
/* 233 */       if (ser != null) {
/* 234 */         return ser;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 239 */     if ((type.isEnum()) && 
/* 240 */       (this._enumSerializerOverride != null)) {
/* 241 */       return this._enumSerializerOverride;
/*     */     }
/*     */ 
/* 247 */     if (this._transitiveClassMappings != null) {
/* 248 */       for (Class curr = type; curr != null; curr = curr.getSuperclass()) {
/* 249 */         key.reset(curr);
/* 250 */         ser = (JsonSerializer)this._transitiveClassMappings.get(key);
/* 251 */         if (ser != null) {
/* 252 */           return ser;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     if (this._interfaceMappings != null)
/*     */     {
/* 260 */       key.reset(type);
/* 261 */       ser = (JsonSerializer)this._interfaceMappings.get(key);
/* 262 */       if (ser != null) {
/* 263 */         return ser;
/*     */       }
/* 265 */       for (Class curr = type; curr != null; curr = curr.getSuperclass()) {
/* 266 */         ser = _findInterfaceMapping(curr, key);
/* 267 */         if (ser != null) {
/* 268 */           return ser;
/*     */         }
/*     */       }
/*     */     }
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> _findInterfaceMapping(Class<?> cls, ClassKey key)
/*     */   {
/* 277 */     for (Class iface : cls.getInterfaces()) {
/* 278 */       key.reset(iface);
/* 279 */       JsonSerializer ser = (JsonSerializer)this._interfaceMappings.get(key);
/* 280 */       if (ser != null) {
/* 281 */         return ser;
/*     */       }
/*     */ 
/* 284 */       ser = _findInterfaceMapping(iface, key);
/* 285 */       if (ser != null) {
/* 286 */         return ser;
/*     */       }
/*     */     }
/* 289 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.CustomSerializerFactory
 * JD-Core Version:    0.6.0
 */