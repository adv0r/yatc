/*     */ package org.codehaus.jackson.map.type;
/*     */ 
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class CollectionType extends TypeBase
/*     */ {
/*     */   final JavaType _elementType;
/*     */ 
/*     */   private CollectionType(Class<?> collT, JavaType elemT)
/*     */   {
/*  24 */     super(collT, elemT.hashCode());
/*  25 */     this._elementType = elemT;
/*     */   }
/*     */ 
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  30 */     return new CollectionType(subclass, this._elementType);
/*     */   }
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/*  37 */     if (contentClass == this._elementType.getRawClass()) {
/*  38 */       return this;
/*     */     }
/*  40 */     JavaType newElementType = this._elementType.narrowBy(contentClass);
/*  41 */     return new CollectionType(this._class, newElementType).copyHandlers(this);
/*     */   }
/*     */ 
/*     */   public static CollectionType construct(Class<?> rawType, JavaType elemT)
/*     */   {
/*  47 */     return new CollectionType(rawType, elemT);
/*     */   }
/*     */ 
/*     */   public CollectionType withTypeHandler(Object h)
/*     */   {
/*  54 */     CollectionType newInstance = new CollectionType(this._class, this._elementType);
/*  55 */     newInstance._typeHandler = h;
/*  56 */     return newInstance;
/*     */   }
/*     */ 
/*     */   public CollectionType withContentTypeHandler(Object h)
/*     */   {
/*  63 */     return new CollectionType(this._class, this._elementType.withTypeHandler(h));
/*     */   }
/*     */ 
/*     */   protected String buildCanonicalName()
/*     */   {
/*  68 */     StringBuilder sb = new StringBuilder();
/*  69 */     sb.append(this._class.getName());
/*  70 */     if (this._elementType != null) {
/*  71 */       sb.append('<');
/*  72 */       sb.append(this._elementType.toCanonical());
/*  73 */       sb.append('>');
/*     */     }
/*  75 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/*  85 */     return this._elementType;
/*     */   }
/*  87 */   public int containedTypeCount() { return 1; }
/*     */ 
/*     */   public JavaType containedType(int index) {
/*  90 */     return index == 0 ? this._elementType : null;
/*     */   }
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/*  99 */     if (index == 0) return "E";
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 105 */     return _classSignature(this._class, sb, true);
/*     */   }
/*     */ 
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 110 */     _classSignature(this._class, sb, false);
/* 111 */     sb.append('<');
/* 112 */     this._elementType.getGenericSignature(sb);
/* 113 */     sb.append(">;");
/* 114 */     return sb;
/*     */   }
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 135 */     return "[collection type; class " + this._class.getName() + ", contains " + this._elementType + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 141 */     if (o == this) return true;
/* 142 */     if (o == null) return false;
/* 143 */     if (o.getClass() != getClass()) return false;
/*     */ 
/* 145 */     CollectionType other = (CollectionType)o;
/* 146 */     return (this._class == other._class) && (this._elementType.equals(other._elementType));
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.CollectionType
 * JD-Core Version:    0.6.0
 */