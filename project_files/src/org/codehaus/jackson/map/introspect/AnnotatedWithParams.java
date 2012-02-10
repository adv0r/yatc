/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ public abstract class AnnotatedWithParams extends AnnotatedMember
/*     */ {
/*     */   protected final AnnotationMap _annotations;
/*     */   protected final AnnotationMap[] _paramAnnotations;
/*     */ 
/*     */   protected AnnotatedWithParams(AnnotationMap classAnn, AnnotationMap[] paramAnnotations)
/*     */   {
/*  33 */     this._annotations = classAnn;
/*  34 */     this._paramAnnotations = paramAnnotations;
/*     */   }
/*     */ 
/*     */   public final void addOrOverride(Annotation a)
/*     */   {
/*  43 */     this._annotations.add(a);
/*     */   }
/*     */ 
/*     */   public final void addOrOverrideParam(int paramIndex, Annotation a)
/*     */   {
/*  54 */     AnnotationMap old = this._paramAnnotations[paramIndex];
/*  55 */     if (old == null) {
/*  56 */       old = new AnnotationMap();
/*  57 */       this._paramAnnotations[paramIndex] = old;
/*     */     }
/*  59 */     old.add(a);
/*     */   }
/*     */ 
/*     */   public final void addIfNotPresent(Annotation a)
/*     */   {
/*  69 */     this._annotations.addIfNotPresent(a);
/*     */   }
/*     */ 
/*     */   public final <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  81 */     return this._annotations.get(acls);
/*     */   }
/*     */ 
/*     */   public final AnnotationMap getParameterAnnotations(int index)
/*     */   {
/*  92 */     if ((this._paramAnnotations != null) && 
/*  93 */       (index >= 0) && (index <= this._paramAnnotations.length)) {
/*  94 */       return this._paramAnnotations[index];
/*     */     }
/*     */ 
/*  97 */     return null; } 
/*     */   public abstract AnnotatedParameter getParameter(int paramInt);
/*     */ 
/*     */   public abstract int getParameterCount();
/*     */ 
/*     */   public abstract Class<?> getParameterClass(int paramInt);
/*     */ 
/*     */   public abstract Type getParameterType(int paramInt);
/*     */ 
/* 108 */   public final int getAnnotationCount() { return this._annotations.size();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedWithParams
 * JD-Core Version:    0.6.0
 */