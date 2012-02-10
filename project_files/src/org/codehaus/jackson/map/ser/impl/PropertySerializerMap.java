/*     */ package org.codehaus.jackson.map.ser.impl;
/*     */ 
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class PropertySerializerMap
/*     */ {
/*     */   public abstract JsonSerializer<Object> serializerFor(Class<?> paramClass);
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddSerializer(Class<?> type, SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  39 */     JsonSerializer serializer = provider.findValueSerializer(type, property);
/*  40 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddSerializer(JavaType type, SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  47 */     JsonSerializer serializer = provider.findValueSerializer(type, property);
/*  48 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
/*     */   }
/*     */   protected abstract PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer);
/*     */ 
/*     */   public static PropertySerializerMap emptyMap() {
/*  54 */     return Empty.instance;
/*     */   }
/*     */ 
/*     */   private static final class Multi extends PropertySerializerMap
/*     */   {
/*     */     private static final int MAX_ENTRIES = 8;
/*     */     private final PropertySerializerMap.TypeAndSerializer[] _entries;
/*     */ 
/*     */     public Multi(PropertySerializerMap.TypeAndSerializer[] entries)
/*     */     {
/* 201 */       this._entries = entries;
/*     */     }
/*     */ 
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 207 */       int i = 0; for (int len = this._entries.length; i < len; i++) {
/* 208 */         PropertySerializerMap.TypeAndSerializer entry = this._entries[i];
/* 209 */         if (entry.type == type) {
/* 210 */           return entry.serializer;
/*     */         }
/*     */       }
/* 213 */       return null;
/*     */     }
/*     */ 
/*     */     protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 219 */       int len = this._entries.length;
/*     */ 
/* 221 */       if (len == 8) {
/* 222 */         return this;
/*     */       }
/*     */ 
/* 225 */       PropertySerializerMap.TypeAndSerializer[] entries = new PropertySerializerMap.TypeAndSerializer[len + 1];
/* 226 */       System.arraycopy(this._entries, 0, entries, 0, len);
/* 227 */       entries[len] = new PropertySerializerMap.TypeAndSerializer(type, serializer);
/* 228 */       return new Multi(entries);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Double extends PropertySerializerMap
/*     */   {
/*     */     private final Class<?> _type1;
/*     */     private final Class<?> _type2;
/*     */     private final JsonSerializer<Object> _serializer1;
/*     */     private final JsonSerializer<Object> _serializer2;
/*     */ 
/*     */     public Double(Class<?> type1, JsonSerializer<Object> serializer1, Class<?> type2, JsonSerializer<Object> serializer2)
/*     */     {
/* 158 */       this._type1 = type1;
/* 159 */       this._serializer1 = serializer1;
/* 160 */       this._type2 = type2;
/* 161 */       this._serializer2 = serializer2;
/*     */     }
/*     */ 
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 167 */       if (type == this._type1) {
/* 168 */         return this._serializer1;
/*     */       }
/* 170 */       if (type == this._type2) {
/* 171 */         return this._serializer2;
/*     */       }
/* 173 */       return null;
/*     */     }
/*     */ 
/*     */     protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 179 */       PropertySerializerMap.TypeAndSerializer[] ts = new PropertySerializerMap.TypeAndSerializer[2];
/* 180 */       ts[0] = new PropertySerializerMap.TypeAndSerializer(this._type1, this._serializer1);
/* 181 */       ts[1] = new PropertySerializerMap.TypeAndSerializer(this._type2, this._serializer2);
/* 182 */       return new PropertySerializerMap.Multi(ts);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Single extends PropertySerializerMap
/*     */   {
/*     */     private final Class<?> _type;
/*     */     private final JsonSerializer<Object> _serializer;
/*     */ 
/*     */     public Single(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 131 */       this._type = type;
/* 132 */       this._serializer = serializer;
/*     */     }
/*     */ 
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 138 */       if (type == this._type) {
/* 139 */         return this._serializer;
/*     */       }
/* 141 */       return null;
/*     */     }
/*     */ 
/*     */     protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 146 */       return new PropertySerializerMap.Double(this._type, this._serializer, type, serializer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Empty extends PropertySerializerMap
/*     */   {
/* 106 */     protected static final Empty instance = new Empty();
/*     */ 
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 110 */       return null;
/*     */     }
/*     */ 
/*     */     protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 115 */       return new PropertySerializerMap.Single(type, serializer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class TypeAndSerializer
/*     */   {
/*     */     public final Class<?> type;
/*     */     public final JsonSerializer<Object> serializer;
/*     */ 
/*     */     public TypeAndSerializer(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/*  89 */       this.type = type;
/*  90 */       this.serializer = serializer;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SerializerAndMapResult
/*     */   {
/*     */     public final JsonSerializer<Object> serializer;
/*     */     public final PropertySerializerMap map;
/*     */ 
/*     */     public SerializerAndMapResult(JsonSerializer<Object> serializer, PropertySerializerMap map)
/*     */     {
/*  75 */       this.serializer = serializer;
/*  76 */       this.map = map;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.PropertySerializerMap
 * JD-Core Version:    0.6.0
 */