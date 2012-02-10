/*    */ package org.codehaus.jackson.map.ser.impl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.codehaus.jackson.map.JsonSerializer;
/*    */ import org.codehaus.jackson.map.ser.SerializerCache.TypeKey;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public final class ReadOnlyClassToSerializerMap
/*    */ {
/*    */   protected final JsonSerializerMap _map;
/* 28 */   protected final SerializerCache.TypeKey _cacheKey = new SerializerCache.TypeKey(getClass(), false);
/*    */ 
/*    */   private ReadOnlyClassToSerializerMap(JsonSerializerMap map)
/*    */   {
/* 32 */     this._map = map;
/*    */   }
/*    */ 
/*    */   public ReadOnlyClassToSerializerMap instance()
/*    */   {
/* 37 */     return new ReadOnlyClassToSerializerMap(this._map);
/*    */   }
/*    */ 
/*    */   public static ReadOnlyClassToSerializerMap from(HashMap<SerializerCache.TypeKey, JsonSerializer<Object>> src)
/*    */   {
/* 47 */     return new ReadOnlyClassToSerializerMap(new JsonSerializerMap(src));
/*    */   }
/*    */ 
/*    */   public JsonSerializer<Object> typedValueSerializer(JavaType type)
/*    */   {
/* 52 */     this._cacheKey.resetTyped(type);
/* 53 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */ 
/*    */   public JsonSerializer<Object> typedValueSerializer(Class<?> cls)
/*    */   {
/* 58 */     this._cacheKey.resetTyped(cls);
/* 59 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */ 
/*    */   public JsonSerializer<Object> untypedValueSerializer(Class<?> cls)
/*    */   {
/* 64 */     this._cacheKey.resetUntyped(cls);
/* 65 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */ 
/*    */   public JsonSerializer<Object> untypedValueSerializer(JavaType type)
/*    */   {
/* 70 */     this._cacheKey.resetUntyped(type);
/* 71 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.ReadOnlyClassToSerializerMap
 * JD-Core Version:    0.6.0
 */