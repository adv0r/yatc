/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import org.codehaus.jackson.map.type.TypeBindings;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class AnnotatedMethod extends AnnotatedWithParams
/*     */ {
/*     */   protected final Method _method;
/*     */   protected Class<?>[] _paramTypes;
/*     */ 
/*     */   public AnnotatedMethod(Method method, AnnotationMap classAnn, AnnotationMap[] paramAnnotations)
/*     */   {
/*  26 */     super(classAnn, paramAnnotations);
/*  27 */     this._method = method;
/*     */   }
/*     */ 
/*     */   public AnnotatedMethod withMethod(Method m)
/*     */   {
/*  38 */     return new AnnotatedMethod(m, this._annotations, this._paramAnnotations);
/*     */   }
/*     */ 
/*     */   public Method getAnnotated()
/*     */   {
/*  48 */     return this._method;
/*     */   }
/*     */   public int getModifiers() {
/*  51 */     return this._method.getModifiers();
/*     */   }
/*     */   public String getName() {
/*  54 */     return this._method.getName();
/*     */   }
/*     */ 
/*     */   public Type getGenericType()
/*     */   {
/*  63 */     return this._method.getGenericReturnType();
/*     */   }
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/*  73 */     return this._method.getReturnType();
/*     */   }
/*     */ 
/*     */   public JavaType getType(TypeBindings bindings)
/*     */   {
/*  83 */     TypeVariable[] localTypeParams = this._method.getTypeParameters();
/*     */ 
/*  85 */     if ((localTypeParams != null) && (localTypeParams.length > 0)) {
/*  86 */       bindings = bindings.childInstance();
/*  87 */       for (TypeVariable var : localTypeParams) {
/*  88 */         String name = var.getName();
/*     */ 
/*  90 */         bindings._addPlaceholder(name);
/*     */ 
/*  92 */         Type lowerBound = var.getBounds()[0];
/*  93 */         JavaType type = lowerBound == null ? TypeFactory.fastSimpleType(Object.class) : TypeFactory.type(lowerBound, bindings);
/*     */ 
/*  95 */         bindings.addBinding(var.getName(), type);
/*     */       }
/*     */     }
/*  98 */     return TypeFactory.type(getGenericType(), bindings);
/*     */   }
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 108 */     return this._method.getDeclaringClass();
/*     */   }
/*     */   public Member getMember() {
/* 111 */     return this._method;
/*     */   }
/*     */ 
/*     */   public AnnotatedParameter getParameter(int index)
/*     */   {
/* 121 */     return new AnnotatedParameter(this, getParameterType(index), this._paramAnnotations[index]);
/*     */   }
/*     */ 
/*     */   public int getParameterCount()
/*     */   {
/* 126 */     return getParameterTypes().length;
/*     */   }
/*     */ 
/*     */   public Type[] getParameterTypes() {
/* 130 */     return this._method.getGenericParameterTypes();
/*     */   }
/*     */ 
/*     */   public Class<?> getParameterClass(int index)
/*     */   {
/* 136 */     Class[] types = this._method.getParameterTypes();
/* 137 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */ 
/*     */   public Type getParameterType(int index)
/*     */   {
/* 143 */     Type[] types = this._method.getGenericParameterTypes();
/* 144 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */ 
/*     */   public Class<?>[] getParameterClasses()
/*     */   {
/* 149 */     if (this._paramTypes == null) {
/* 150 */       this._paramTypes = this._method.getParameterTypes();
/*     */     }
/* 152 */     return this._paramTypes;
/*     */   }
/*     */ 
/*     */   public String getFullName()
/*     */   {
/* 160 */     return getDeclaringClass().getName() + "#" + getName() + "(" + getParameterCount() + " params)";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 173 */     return "[method " + getName() + ", annotations: " + this._annotations + "]";
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedMethod
 * JD-Core Version:    0.6.0
 */