/*     */ package org.codehaus.jackson.map.type;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class ArrayType extends TypeBase
/*     */ {
/*     */   final JavaType _componentType;
/*     */   final Object _emptyArray;
/*     */ 
/*     */   private ArrayType(JavaType componentType, Object emptyInstance)
/*     */   {
/*  29 */     super(emptyInstance.getClass(), componentType.hashCode());
/*  30 */     this._componentType = componentType;
/*  31 */     this._emptyArray = emptyInstance;
/*     */   }
/*     */ 
/*     */   public static ArrayType construct(JavaType componentType)
/*     */   {
/*  42 */     Object emptyInstance = Array.newInstance(componentType.getRawClass(), 0);
/*  43 */     return new ArrayType(componentType, emptyInstance);
/*     */   }
/*     */ 
/*     */   public ArrayType withTypeHandler(Object h)
/*     */   {
/*  50 */     ArrayType newInstance = new ArrayType(this._componentType, this._emptyArray);
/*  51 */     newInstance._typeHandler = h;
/*  52 */     return newInstance;
/*     */   }
/*     */ 
/*     */   public ArrayType withContentTypeHandler(Object h)
/*     */   {
/*  59 */     return new ArrayType(this._componentType.withTypeHandler(h), this._emptyArray);
/*     */   }
/*     */ 
/*     */   protected String buildCanonicalName()
/*     */   {
/*  64 */     return this._class.getName();
/*     */   }
/*     */ 
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  83 */     if (!subclass.isArray()) {
/*  84 */       throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + toString() + " to class " + subclass.getName());
/*     */     }
/*     */ 
/*  89 */     Class newCompClass = subclass.getComponentType();
/*  90 */     JavaType newCompType = TypeFactory.type(newCompClass);
/*  91 */     return construct(newCompType);
/*     */   }
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/* 102 */     if (contentClass == this._componentType.getRawClass()) {
/* 103 */       return this;
/*     */     }
/* 105 */     JavaType newComponentType = this._componentType.narrowBy(contentClass);
/* 106 */     return construct(newComponentType).copyHandlers(this);
/*     */   }
/*     */ 
/*     */   public boolean isArrayType()
/*     */   {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isAbstract()
/*     */   {
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isConcrete()
/*     */   {
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasGenericTypes()
/*     */   {
/* 137 */     return this._componentType.hasGenericTypes();
/*     */   }
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 148 */     if (index == 0) return "E";
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 159 */     return true;
/*     */   }
/*     */   public JavaType getContentType() {
/* 162 */     return this._componentType;
/*     */   }
/*     */   public int containedTypeCount() {
/* 165 */     return 1;
/*     */   }
/*     */   public JavaType containedType(int index) {
/* 168 */     return index == 0 ? this._componentType : null;
/*     */   }
/*     */ 
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 173 */     sb.append('[');
/* 174 */     return this._componentType.getGenericSignature(sb);
/*     */   }
/*     */ 
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 179 */     sb.append('[');
/* 180 */     return this._componentType.getErasedSignature(sb);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 192 */     return "[array type, component type: " + this._componentType + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 198 */     if (o == this) return true;
/* 199 */     if (o == null) return false;
/* 200 */     if (o.getClass() != getClass()) return false;
/*     */ 
/* 202 */     ArrayType other = (ArrayType)o;
/* 203 */     return this._componentType.equals(other._componentType);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.ArrayType
 * JD-Core Version:    0.6.0
 */