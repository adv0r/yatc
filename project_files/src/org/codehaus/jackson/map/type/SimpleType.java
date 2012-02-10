/*     */ package org.codehaus.jackson.map.type;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class SimpleType extends TypeBase
/*     */ {
/*     */   protected final JavaType[] _typeParameters;
/*     */   protected final String[] _typeNames;
/*     */ 
/*     */   protected SimpleType(Class<?> cls)
/*     */   {
/*  40 */     this(cls, null, null);
/*     */   }
/*     */ 
/*     */   protected SimpleType(Class<?> cls, String[] typeNames, JavaType[] typeParams)
/*     */   {
/*  45 */     super(cls, 0);
/*  46 */     if ((typeNames == null) || (typeNames.length == 0)) {
/*  47 */       this._typeNames = null;
/*  48 */       this._typeParameters = null;
/*     */     } else {
/*  50 */       this._typeNames = typeNames;
/*  51 */       this._typeParameters = typeParams;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  59 */     return new SimpleType(subclass, this._typeNames, this._typeParameters);
/*     */   }
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> subclass)
/*     */   {
/*  66 */     throw new IllegalArgumentException("Internal error: SimpleType.narrowContentsBy() should never be called");
/*     */   }
/*     */ 
/*     */   public static SimpleType construct(Class<?> cls)
/*     */   {
/*  74 */     if (Map.class.isAssignableFrom(cls)) {
/*  75 */       throw new IllegalArgumentException("Can not construct SimpleType for a Map (class: " + cls.getName() + ")");
/*     */     }
/*  77 */     if (Collection.class.isAssignableFrom(cls)) {
/*  78 */       throw new IllegalArgumentException("Can not construct SimpleType for a Collection (class: " + cls.getName() + ")");
/*     */     }
/*     */ 
/*  81 */     if (cls.isArray()) {
/*  82 */       throw new IllegalArgumentException("Can not construct SimpleType for an array (class: " + cls.getName() + ")");
/*     */     }
/*  84 */     return new SimpleType(cls);
/*     */   }
/*     */ 
/*     */   public SimpleType withTypeHandler(Object h)
/*     */   {
/*  91 */     SimpleType newInstance = new SimpleType(this._class, this._typeNames, this._typeParameters);
/*  92 */     newInstance._typeHandler = h;
/*  93 */     return newInstance;
/*     */   }
/*     */ 
/*     */   public JavaType withContentTypeHandler(Object h)
/*     */   {
/* 101 */     throw new IllegalArgumentException("Simple types have no content types; can not call withContenTypeHandler()");
/*     */   }
/*     */ 
/*     */   protected String buildCanonicalName()
/*     */   {
/* 107 */     StringBuilder sb = new StringBuilder();
/* 108 */     sb.append(this._class.getName());
/* 109 */     if ((this._typeParameters != null) && (this._typeParameters.length > 0)) {
/* 110 */       sb.append('<');
/* 111 */       boolean first = true;
/* 112 */       for (JavaType t : this._typeParameters) {
/* 113 */         if (first)
/* 114 */           first = false;
/*     */         else {
/* 116 */           sb.append(',');
/*     */         }
/* 118 */         sb.append(t.toCanonical());
/*     */       }
/* 120 */       sb.append('>');
/*     */     }
/* 122 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   public int containedTypeCount() {
/* 136 */     return this._typeParameters == null ? 0 : this._typeParameters.length;
/*     */   }
/*     */ 
/*     */   public JavaType containedType(int index)
/*     */   {
/* 142 */     if ((index < 0) || (this._typeParameters == null) || (index >= this._typeParameters.length)) {
/* 143 */       return null;
/*     */     }
/* 145 */     return this._typeParameters[index];
/*     */   }
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 151 */     if ((index < 0) || (this._typeNames == null) || (index >= this._typeNames.length)) {
/* 152 */       return null;
/*     */     }
/* 154 */     return this._typeNames[index];
/*     */   }
/*     */ 
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 159 */     return _classSignature(this._class, sb, true);
/*     */   }
/*     */ 
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 165 */     _classSignature(this._class, sb, false);
/* 166 */     if (this._typeParameters != null) {
/* 167 */       sb.append('<');
/* 168 */       for (JavaType param : this._typeParameters) {
/* 169 */         sb = param.getGenericSignature(sb);
/*     */       }
/* 171 */       sb.append('>');
/*     */     }
/* 173 */     sb.append(';');
/* 174 */     return sb;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 186 */     StringBuilder sb = new StringBuilder(40);
/* 187 */     sb.append("[simple type, class ").append(buildCanonicalName()).append(']');
/* 188 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 194 */     if (o == this) return true;
/* 195 */     if (o == null) return false;
/* 196 */     if (o.getClass() != getClass()) return false;
/*     */ 
/* 198 */     SimpleType other = (SimpleType)o;
/*     */ 
/* 201 */     if (other._class != this._class) return false;
/*     */ 
/* 204 */     JavaType[] p1 = this._typeParameters;
/* 205 */     JavaType[] p2 = other._typeParameters;
/* 206 */     if (p1 == null) {
/* 207 */       return (p2 == null) || (p2.length == 0);
/*     */     }
/* 209 */     if (p2 == null) return false;
/*     */ 
/* 211 */     if (p1.length != p2.length) return false;
/* 212 */     int i = 0; for (int len = p1.length; i < len; i++) {
/* 213 */       if (!p1[i].equals(p2[i])) {
/* 214 */         return false;
/*     */       }
/*     */     }
/* 217 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.SimpleType
 * JD-Core Version:    0.6.0
 */