/*       */ package org.codehaus.jackson.map.ser;
/*       */ 
/*       */ import java.util.HashMap;
/*       */ import org.codehaus.jackson.map.JsonSerializer;
/*       */ import org.codehaus.jackson.map.ser.impl.ReadOnlyClassToSerializerMap;
/*       */ import org.codehaus.jackson.type.JavaType;
/*       */ 
/*       */ public final class SerializerCache
/*       */ {
/*    32 */   private HashMap<TypeKey, JsonSerializer<Object>> _sharedMap = new HashMap(64);
/*       */ 
/*    37 */   private ReadOnlyClassToSerializerMap _readOnlyMap = null;
/*       */ 
/*       */   public ReadOnlyClassToSerializerMap getReadOnlyLookupMap()
/*       */   {
/*       */     ReadOnlyClassToSerializerMap m;
/*    49 */     synchronized (this) {
/*    50 */       m = this._readOnlyMap;
/*    51 */       if (m == null) {
/*    52 */         this._readOnlyMap = (m = ReadOnlyClassToSerializerMap.from(this._sharedMap));
/*       */       }
/*       */     }
/*    55 */     return m.instance();
/*       */   }
/*       */ 
/*       */   public JsonSerializer<Object> untypedValueSerializer(Class<?> type)
/*       */   {
/*    64 */     synchronized (this) {
/*    65 */       return (JsonSerializer)this._sharedMap.get(new TypeKey(type, false));
/*       */     }
/*       */   }
/*       */ 
/*       */   public JsonSerializer<Object> untypedValueSerializer(JavaType type)
/*       */   {
/*    74 */     synchronized (this) {
/*    75 */       return (JsonSerializer)this._sharedMap.get(new TypeKey(type, false));
/*       */     }
/*       */   }
/*       */ 
/*       */   public JsonSerializer<Object> typedValueSerializer(JavaType type)
/*       */   {
/*    81 */     synchronized (this) {
/*    82 */       return (JsonSerializer)this._sharedMap.get(new TypeKey(type, true));
/*       */     }
/*       */   }
/*       */ 
/*       */   public JsonSerializer<Object> typedValueSerializer(Class<?> cls)
/*       */   {
/*    88 */     synchronized (this) {
/*    89 */       return (JsonSerializer)this._sharedMap.get(new TypeKey(cls, true));
/*       */     }
/*       */   }
/*       */ 
/*       */   public void addTypedSerializer(JavaType type, JsonSerializer<Object> ser)
/*       */   {
/*   100 */     synchronized (this) {
/*   101 */       if (this._sharedMap.put(new TypeKey(type, true), ser) == null)
/*       */       {
/*   103 */         this._readOnlyMap = null;
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public void addTypedSerializer(Class<?> cls, JsonSerializer<Object> ser)
/*       */   {
/*   110 */     synchronized (this) {
/*   111 */       if (this._sharedMap.put(new TypeKey(cls, true), ser) == null)
/*       */       {
/*   113 */         this._readOnlyMap = null;
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public void addNonTypedSerializer(Class<?> type, JsonSerializer<Object> ser)
/*       */   {
/*   120 */     synchronized (this) {
/*   121 */       if (this._sharedMap.put(new TypeKey(type, false), ser) == null)
/*       */       {
/*   123 */         this._readOnlyMap = null;
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public void addNonTypedSerializer(JavaType type, JsonSerializer<Object> ser)
/*       */   {
/*   133 */     synchronized (this) {
/*   134 */       if (this._sharedMap.put(new TypeKey(type, false), ser) == null)
/*       */       {
/*   136 */         this._readOnlyMap = null;
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public synchronized int size()
/*       */   {
/*   145 */     return this._sharedMap.size();
/*       */   }
/*       */ 
/*       */   public synchronized void flush() {
/*   149 */     this._sharedMap.clear();
/*       */   }
/*       */ 
/*       */   public static final class TypeKey
/*       */   {
/*       */     protected int _hashCode;
/*       */     protected Class<?> _class;
/*       */     protected JavaType _type;
/*       */     protected boolean _isTyped;
/*       */ 
/*       */     public TypeKey(Class<?> key, boolean typed)
/*       */     {
/*   179 */       this._class = key;
/*   180 */       this._type = null;
/*   181 */       this._isTyped = typed;
/*   182 */       this._hashCode = hash(key, typed);
/*       */     }
/*       */ 
/*       */     public TypeKey(JavaType key, boolean typed) {
/*   186 */       this._type = key;
/*   187 */       this._class = null;
/*   188 */       this._isTyped = typed;
/*   189 */       this._hashCode = hash(key, typed);
/*       */     }
/*       */ 
/*       */     private static final int hash(Class<?> cls, boolean typed) {
/*   193 */       int hash = cls.getName().hashCode();
/*   194 */       if (typed) {
/*   195 */         hash++;
/*       */       }
/*   197 */       return hash;
/*       */     }
/*       */ 
/*       */     private static final int hash(JavaType type, boolean typed) {
/*   201 */       int hash = type.hashCode() - 1;
/*   202 */       if (typed) {
/*   203 */         hash--;
/*       */       }
/*   205 */       return hash;
/*       */     }
/*       */ 
/*       */     public void resetTyped(Class<?> cls) {
/*   209 */       this._type = null;
/*   210 */       this._class = cls;
/*   211 */       this._isTyped = true;
/*   212 */       this._hashCode = hash(cls, true);
/*       */     }
/*       */ 
/*       */     public void resetUntyped(Class<?> cls) {
/*   216 */       this._type = null;
/*   217 */       this._class = cls;
/*   218 */       this._isTyped = false;
/*   219 */       this._hashCode = hash(cls, false);
/*       */     }
/*       */ 
/*       */     public void resetTyped(JavaType type) {
/*   223 */       this._type = type;
/*   224 */       this._class = null;
/*   225 */       this._isTyped = true;
/*   226 */       this._hashCode = hash(type, true);
/*       */     }
/*       */ 
/*       */     public void resetUntyped(JavaType type) {
/*   230 */       this._type = type;
/*   231 */       this._class = null;
/*   232 */       this._isTyped = false;
/*   233 */       this._hashCode = hash(type, false);
/*       */     }
/*       */     public final int hashCode() {
/*   236 */       return this._hashCode;
/*       */     }
/*       */     public final String toString() {
/*   239 */       if (this._class != null) {
/*   240 */         return "{class: " + this._class.getName() + ", typed? " + this._isTyped + "}";
/*       */       }
/*   242 */       return "{type: " + this._type + ", typed? " + this._isTyped + "}";
/*       */     }
/*       */ 
/*       */     public final boolean equals(Object o)
/*       */     {
/*   248 */       if (o == this) return true;
/*   249 */       TypeKey other = (TypeKey)o;
/*   250 */       if (other._isTyped == this._isTyped) {
/*   251 */         if (this._class != null) {
/*   252 */           return other._class == this._class;
/*       */         }
/*   254 */         return this._type.equals(other._type);
/*       */       }
/*   256 */       return false;
/*       */     }
/*       */   }
/*       */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.SerializerCache
 * JD-Core Version:    0.6.0
 */