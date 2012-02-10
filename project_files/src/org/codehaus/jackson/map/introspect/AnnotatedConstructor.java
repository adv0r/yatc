/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import org.codehaus.jackson.map.type.TypeBindings;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class AnnotatedConstructor extends AnnotatedWithParams
/*     */ {
/*     */   protected final Constructor<?> _constructor;
/*     */ 
/*     */   public AnnotatedConstructor(Constructor<?> constructor, AnnotationMap classAnn, AnnotationMap[] paramAnn)
/*     */   {
/*  26 */     super(classAnn, paramAnn);
/*  27 */     if (constructor == null) {
/*  28 */       throw new IllegalArgumentException("Null constructor not allowed");
/*     */     }
/*  30 */     this._constructor = constructor;
/*     */   }
/*     */ 
/*     */   public Constructor<?> getAnnotated()
/*     */   {
/*  40 */     return this._constructor;
/*     */   }
/*     */   public int getModifiers() {
/*  43 */     return this._constructor.getModifiers();
/*     */   }
/*     */   public String getName() {
/*  46 */     return this._constructor.getName();
/*     */   }
/*     */ 
/*     */   public Type getGenericType() {
/*  50 */     return getRawType();
/*     */   }
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/*  55 */     return this._constructor.getDeclaringClass();
/*     */   }
/*     */ 
/*     */   public JavaType getType(TypeBindings bindings)
/*     */   {
/*  66 */     TypeVariable[] localTypeParams = this._constructor.getTypeParameters();
/*     */ 
/*  68 */     if ((localTypeParams != null) && (localTypeParams.length > 0)) {
/*  69 */       bindings = bindings.childInstance();
/*  70 */       for (TypeVariable var : localTypeParams) {
/*  71 */         String name = var.getName();
/*     */ 
/*  73 */         bindings._addPlaceholder(name);
/*     */ 
/*  75 */         Type lowerBound = var.getBounds()[0];
/*  76 */         JavaType type = lowerBound == null ? TypeFactory.fastSimpleType(Object.class) : TypeFactory.type(lowerBound, bindings);
/*     */ 
/*  78 */         bindings.addBinding(var.getName(), type);
/*     */       }
/*     */     }
/*  81 */     return TypeFactory.type(getGenericType(), bindings);
/*     */   }
/*     */ 
/*     */   public AnnotatedParameter getParameter(int index)
/*     */   {
/*  92 */     return new AnnotatedParameter(this, getParameterType(index), this._paramAnnotations[index]);
/*     */   }
/*     */ 
/*     */   public int getParameterCount()
/*     */   {
/*  97 */     return this._constructor.getParameterTypes().length;
/*     */   }
/*     */ 
/*     */   public Class<?> getParameterClass(int index)
/*     */   {
/* 103 */     Class[] types = this._constructor.getParameterTypes();
/* 104 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */ 
/*     */   public Type getParameterType(int index)
/*     */   {
/* 110 */     Type[] types = this._constructor.getGenericParameterTypes();
/* 111 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 121 */     return this._constructor.getDeclaringClass();
/*     */   }
/*     */   public Member getMember() {
/* 124 */     return this._constructor;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 134 */     return "[constructor for " + getName() + ", annotations: " + this._annotations + "]";
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedConstructor
 * JD-Core Version:    0.6.0
 */