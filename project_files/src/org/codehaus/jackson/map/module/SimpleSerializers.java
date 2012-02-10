/*     */ package org.codehaus.jackson.map.module;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.map.BeanDescription;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.Serializers;
/*     */ import org.codehaus.jackson.map.type.ClassKey;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class SimpleSerializers
/*     */   implements Serializers
/*     */ {
/*  28 */   protected HashMap<ClassKey, JsonSerializer<?>> _classMappings = null;
/*     */ 
/*  33 */   protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings = null;
/*     */ 
/*     */   public void addSerializer(JsonSerializer<?> ser)
/*     */   {
/*  55 */     Class cls = ser.handledType();
/*  56 */     if ((cls == null) || (cls == Object.class)) {
/*  57 */       throw new IllegalArgumentException("JsonSerializer of type " + ser.getClass().getName() + " does not define valid handledType() (use alternative registration method?)");
/*     */     }
/*     */ 
/*  60 */     _addSerializer(cls, ser);
/*     */   }
/*     */ 
/*     */   public <T> void addSerializer(Class<? extends T> type, JsonSerializer<T> ser)
/*     */   {
/*  65 */     _addSerializer(type, ser);
/*     */   }
/*     */ 
/*     */   private void _addSerializer(Class<?> cls, JsonSerializer<?> ser)
/*     */   {
/*  70 */     ClassKey key = new ClassKey(cls);
/*     */ 
/*  72 */     if (cls.isInterface()) {
/*  73 */       if (this._interfaceMappings == null) {
/*  74 */         this._interfaceMappings = new HashMap();
/*     */       }
/*  76 */       this._interfaceMappings.put(key, ser);
/*     */     } else {
/*  78 */       if (this._classMappings == null) {
/*  79 */         this._classMappings = new HashMap();
/*     */       }
/*  81 */       this._classMappings.put(key, ser);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, BeanProperty property)
/*     */   {
/*  95 */     Class cls = type.getRawClass();
/*  96 */     ClassKey key = new ClassKey(cls);
/*  97 */     JsonSerializer ser = null;
/*     */ 
/* 100 */     if (cls.isInterface()) {
/* 101 */       if (this._interfaceMappings != null) {
/* 102 */         ser = (JsonSerializer)this._interfaceMappings.get(key);
/* 103 */         if (ser != null) {
/* 104 */           return ser;
/*     */         }
/*     */       }
/*     */     }
/* 108 */     else if (this._classMappings != null) {
/* 109 */       ser = (JsonSerializer)this._classMappings.get(key);
/* 110 */       if (ser != null) {
/* 111 */         return ser;
/*     */       }
/*     */ 
/* 114 */       for (Class curr = cls; curr != null; curr = curr.getSuperclass()) {
/* 115 */         key.reset(curr);
/* 116 */         ser = (JsonSerializer)this._classMappings.get(key);
/* 117 */         if (ser != null) {
/* 118 */           return ser;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 124 */     if (this._interfaceMappings != null) {
/* 125 */       return _findInterfaceMapping(cls, key);
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> _findInterfaceMapping(Class<?> cls, ClassKey key)
/*     */   {
/* 138 */     for (Class iface : cls.getInterfaces()) {
/* 139 */       key.reset(iface);
/* 140 */       JsonSerializer ser = (JsonSerializer)this._interfaceMappings.get(key);
/* 141 */       if (ser != null) {
/* 142 */         return ser;
/*     */       }
/* 144 */       ser = _findInterfaceMapping(iface, key);
/* 145 */       if (ser != null) {
/* 146 */         return ser;
/*     */       }
/*     */     }
/* 149 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.module.SimpleSerializers
 * JD-Core Version:    0.6.0
 */