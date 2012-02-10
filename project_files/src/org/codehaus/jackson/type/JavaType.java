/*     */ package org.codehaus.jackson.type;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public abstract class JavaType
/*     */ {
/*     */   protected final Class<?> _class;
/*     */   protected final int _hashCode;
/*     */   protected Object _valueHandler;
/*     */   protected Object _typeHandler;
/*     */ 
/*     */   protected JavaType(Class<?> clz, int hash)
/*     */   {
/*  56 */     this._class = clz;
/*  57 */     String name = clz.getName();
/*  58 */     this._hashCode = (name.hashCode() + hash);
/*     */   }
/*     */ 
/*     */   public abstract JavaType withTypeHandler(Object paramObject);
/*     */ 
/*     */   public abstract JavaType withContentTypeHandler(Object paramObject);
/*     */ 
/*     */   public final JavaType narrowBy(Class<?> subclass)
/*     */   {
/*  93 */     if (subclass == this._class) {
/*  94 */       return this;
/*     */     }
/*     */ 
/*  97 */     _assertSubclass(subclass, this._class);
/*  98 */     JavaType result = _narrow(subclass);
/*  99 */     if (this._valueHandler != null) {
/* 100 */       result.setValueHandler(this._valueHandler);
/*     */     }
/* 102 */     if (this._typeHandler != null) {
/* 103 */       result = result.withTypeHandler(this._typeHandler);
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */   public final JavaType forcedNarrowBy(Class<?> subclass)
/*     */   {
/* 117 */     if (subclass == this._class) {
/* 118 */       return this;
/*     */     }
/* 120 */     JavaType result = _narrow(subclass);
/* 121 */     if (this._valueHandler != null) {
/* 122 */       result.setValueHandler(this._valueHandler);
/*     */     }
/* 124 */     if (this._typeHandler != null) {
/* 125 */       result = result.withTypeHandler(this._typeHandler);
/*     */     }
/* 127 */     return result;
/*     */   }
/*     */ 
/*     */   public final JavaType widenBy(Class<?> superclass)
/*     */   {
/* 142 */     if (superclass == this._class) {
/* 143 */       return this;
/*     */     }
/*     */ 
/* 146 */     _assertSubclass(this._class, superclass);
/* 147 */     return _widen(superclass);
/*     */   }
/*     */ 
/*     */   protected abstract JavaType _narrow(Class<?> paramClass);
/*     */ 
/*     */   protected JavaType _widen(Class<?> superclass)
/*     */   {
/* 158 */     return _narrow(superclass);
/*     */   }
/*     */ 
/*     */   public abstract JavaType narrowContentsBy(Class<?> paramClass);
/*     */ 
/*     */   public void setValueHandler(Object h)
/*     */   {
/* 171 */     if ((h != null) && (this._valueHandler != null)) {
/* 172 */       throw new IllegalStateException("Trying to reset value handler for type [" + toString() + "]; old handler of type " + this._valueHandler.getClass().getName() + ", new handler of type " + h.getClass().getName());
/*     */     }
/*     */ 
/* 175 */     this._valueHandler = h;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setTypeHandler(Object h)
/*     */   {
/* 195 */     if ((h != null) && (this._typeHandler != null)) {
/* 196 */       throw new IllegalStateException("Trying to reset type handler for type [" + toString() + "]; old handler of type " + this._typeHandler.getClass().getName() + ", new handler of type " + h.getClass().getName());
/*     */     }
/*     */ 
/* 199 */     this._typeHandler = h;
/*     */   }
/*     */ 
/*     */   public final Class<?> getRawClass()
/*     */   {
/* 208 */     return this._class;
/*     */   }
/*     */ 
/*     */   public final boolean hasRawClass(Class<?> clz)
/*     */   {
/* 216 */     return this._class == clz;
/*     */   }
/*     */ 
/*     */   public abstract boolean isContainerType();
/*     */ 
/*     */   public boolean isAbstract()
/*     */   {
/* 226 */     return Modifier.isAbstract(this._class.getModifiers());
/*     */   }
/*     */ 
/*     */   public boolean isConcrete()
/*     */   {
/* 233 */     int mod = this._class.getModifiers();
/* 234 */     if ((mod & 0x600) == 0) {
/* 235 */       return true;
/*     */     }
/*     */ 
/* 241 */     return this._class.isPrimitive();
/*     */   }
/*     */ 
/*     */   public boolean isThrowable()
/*     */   {
/* 247 */     return Throwable.class.isAssignableFrom(this._class);
/*     */   }
/*     */   public boolean isArrayType() {
/* 250 */     return false;
/*     */   }
/* 252 */   public final boolean isEnumType() { return this._class.isEnum(); } 
/*     */   public final boolean isInterface() {
/* 254 */     return this._class.isInterface();
/*     */   }
/* 256 */   public final boolean isPrimitive() { return this._class.isPrimitive(); } 
/*     */   public final boolean isFinal() {
/* 258 */     return Modifier.isFinal(this._class.getModifiers());
/*     */   }
/*     */ 
/*     */   public boolean hasGenericTypes()
/*     */   {
/* 268 */     return containedTypeCount() > 0;
/*     */   }
/*     */ 
/*     */   public JavaType getKeyType()
/*     */   {
/* 275 */     return null;
/*     */   }
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 282 */     return null;
/*     */   }
/*     */ 
/*     */   public int containedTypeCount()
/*     */   {
/* 291 */     return 0;
/*     */   }
/*     */ 
/*     */   public JavaType containedType(int index)
/*     */   {
/* 304 */     return null;
/*     */   }
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 319 */     return null;
/*     */   }
/*     */ 
/*     */   public <T> T getValueHandler()
/*     */   {
/* 327 */     return this._valueHandler;
/*     */   }
/*     */ 
/*     */   public <T> T getTypeHandler()
/*     */   {
/* 335 */     return this._typeHandler;
/*     */   }
/*     */ 
/*     */   public abstract String toCanonical();
/*     */ 
/*     */   public String getGenericSignature()
/*     */   {
/* 366 */     StringBuilder sb = new StringBuilder(40);
/* 367 */     getGenericSignature(sb);
/* 368 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public abstract StringBuilder getGenericSignature(StringBuilder paramStringBuilder);
/*     */ 
/*     */   public String getErasedSignature()
/*     */   {
/* 391 */     StringBuilder sb = new StringBuilder(40);
/* 392 */     getErasedSignature(sb);
/* 393 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public abstract StringBuilder getErasedSignature(StringBuilder paramStringBuilder);
/*     */ 
/*     */   protected void _assertSubclass(Class<?> subclass, Class<?> superClass)
/*     */   {
/* 419 */     if (!this._class.isAssignableFrom(subclass))
/* 420 */       throw new IllegalArgumentException("Class " + subclass.getName() + " is not assignable to " + this._class.getName());
/*     */   }
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 437 */     return this._hashCode;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.type.JavaType
 * JD-Core Version:    0.6.0
 */