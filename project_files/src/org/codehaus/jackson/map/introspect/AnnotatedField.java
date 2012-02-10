/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ public final class AnnotatedField extends AnnotatedMember
/*     */ {
/*     */   protected final Field _field;
/*     */   protected final AnnotationMap _annotations;
/*     */ 
/*     */   public AnnotatedField(Field field, AnnotationMap annMap)
/*     */   {
/*  29 */     this._field = field;
/*  30 */     this._annotations = annMap;
/*     */   }
/*     */ 
/*     */   public void addOrOverride(Annotation a)
/*     */   {
/*  40 */     this._annotations.add(a);
/*     */   }
/*     */ 
/*     */   public Field getAnnotated()
/*     */   {
/*  50 */     return this._field;
/*     */   }
/*     */   public int getModifiers() {
/*  53 */     return this._field.getModifiers();
/*     */   }
/*     */   public String getName() {
/*  56 */     return this._field.getName();
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  61 */     return this._annotations.get(acls);
/*     */   }
/*     */ 
/*     */   public Type getGenericType()
/*     */   {
/*  66 */     return this._field.getGenericType();
/*     */   }
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/*  71 */     return this._field.getType();
/*     */   }
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/*  81 */     return this._field.getDeclaringClass();
/*     */   }
/*     */   public Member getMember() {
/*  84 */     return this._field;
/*     */   }
/*     */ 
/*     */   public String getFullName()
/*     */   {
/*  93 */     return getDeclaringClass().getName() + "#" + getName();
/*     */   }
/*     */   public int getAnnotationCount() {
/*  96 */     return this._annotations.size();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     return "[field " + getName() + ", annotations: " + this._annotations + "]";
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedField
 * JD-Core Version:    0.6.0
 */