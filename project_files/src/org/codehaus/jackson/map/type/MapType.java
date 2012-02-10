/*     */ package org.codehaus.jackson.map.type;
/*     */ 
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class MapType extends TypeBase
/*     */ {
/*     */   final JavaType _keyType;
/*     */   final JavaType _valueType;
/*     */ 
/*     */   private MapType(Class<?> mapType, JavaType keyT, JavaType valueT)
/*     */   {
/*  29 */     super(mapType, keyT.hashCode() ^ valueT.hashCode());
/*  30 */     this._keyType = keyT;
/*  31 */     this._valueType = valueT;
/*     */   }
/*     */ 
/*     */   public static MapType construct(Class<?> rawType, JavaType keyT, JavaType valueT)
/*     */   {
/*  37 */     return new MapType(rawType, keyT, valueT);
/*     */   }
/*     */ 
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  43 */     return new MapType(subclass, this._keyType, this._valueType);
/*     */   }
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/*  50 */     if (contentClass == this._valueType.getRawClass()) {
/*  51 */       return this;
/*     */     }
/*  53 */     JavaType newValueType = this._valueType.narrowBy(contentClass);
/*  54 */     return new MapType(this._class, this._keyType, newValueType).copyHandlers(this);
/*     */   }
/*     */ 
/*     */   public JavaType narrowKey(Class<?> keySubclass)
/*     */   {
/*  60 */     if (keySubclass == this._keyType.getRawClass()) {
/*  61 */       return this;
/*     */     }
/*  63 */     JavaType newKeyType = this._keyType.narrowBy(keySubclass);
/*  64 */     return new MapType(this._class, newKeyType, this._valueType).copyHandlers(this);
/*     */   }
/*     */ 
/*     */   public MapType withTypeHandler(Object h)
/*     */   {
/*  71 */     MapType newInstance = new MapType(this._class, this._keyType, this._valueType);
/*  72 */     newInstance._typeHandler = h;
/*  73 */     return newInstance;
/*     */   }
/*     */ 
/*     */   public MapType withContentTypeHandler(Object h)
/*     */   {
/*  80 */     return new MapType(this._class, this._keyType, this._valueType.withTypeHandler(h));
/*     */   }
/*     */ 
/*     */   protected String buildCanonicalName()
/*     */   {
/*  85 */     StringBuilder sb = new StringBuilder();
/*  86 */     sb.append(this._class.getName());
/*  87 */     if (this._keyType != null) {
/*  88 */       sb.append('<');
/*  89 */       sb.append(this._keyType.toCanonical());
/*  90 */       sb.append(',');
/*  91 */       sb.append(this._valueType.toCanonical());
/*  92 */       sb.append('>');
/*     */     }
/*  94 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 104 */     return true;
/*     */   }
/*     */   public JavaType getKeyType() {
/* 107 */     return this._keyType;
/*     */   }
/*     */   public JavaType getContentType() {
/* 110 */     return this._valueType;
/*     */   }
/*     */   public int containedTypeCount() {
/* 113 */     return 2;
/*     */   }
/*     */ 
/*     */   public JavaType containedType(int index) {
/* 117 */     if (index == 0) return this._keyType;
/* 118 */     if (index == 1) return this._valueType;
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 129 */     if (index == 0) return "K";
/* 130 */     if (index == 1) return "V";
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 136 */     return _classSignature(this._class, sb, true);
/*     */   }
/*     */ 
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 142 */     _classSignature(this._class, sb, false);
/* 143 */     sb.append('<');
/* 144 */     this._keyType.getGenericSignature(sb);
/* 145 */     this._valueType.getGenericSignature(sb);
/* 146 */     sb.append(">;");
/* 147 */     return sb;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 159 */     return "[map type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 165 */     if (o == this) return true;
/* 166 */     if (o == null) return false;
/* 167 */     if (o.getClass() != getClass()) return false;
/*     */ 
/* 169 */     MapType other = (MapType)o;
/* 170 */     return (this._class == other._class) && (this._keyType.equals(other._keyType)) && (this._valueType.equals(other._valueType));
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.MapType
 * JD-Core Version:    0.6.0
 */