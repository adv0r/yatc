/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.util.Annotations;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract interface BeanProperty
/*     */ {
/*     */   public abstract String getName();
/*     */ 
/*     */   public abstract JavaType getType();
/*     */ 
/*     */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*     */ 
/*     */   public abstract <A extends Annotation> A getContextAnnotation(Class<A> paramClass);
/*     */ 
/*     */   public abstract AnnotatedMember getMember();
/*     */ 
/*     */   public static class Std
/*     */     implements BeanProperty
/*     */   {
/*     */     protected final String _name;
/*     */     protected final JavaType _type;
/*     */     protected final AnnotatedMember _member;
/*     */     protected final Annotations _contextAnnotations;
/*     */ 
/*     */     public Std(String name, JavaType type, Annotations contextAnnotations, AnnotatedMember member)
/*     */     {
/*  76 */       this._name = name;
/*  77 */       this._type = type;
/*  78 */       this._member = member;
/*  79 */       this._contextAnnotations = contextAnnotations;
/*     */     }
/*     */ 
/*     */     public Std withType(JavaType type) {
/*  83 */       return new Std(this._name, type, this._contextAnnotations, this._member);
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls) {
/*  87 */       return this._member.getAnnotation(acls);
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
/*  91 */       return this._contextAnnotations == null ? null : this._contextAnnotations.get(acls);
/*     */     }
/*     */ 
/*     */     public String getName() {
/*  95 */       return this._name;
/*     */     }
/*     */ 
/*     */     public JavaType getType() {
/*  99 */       return this._type;
/*     */     }
/*     */ 
/*     */     public AnnotatedMember getMember() {
/* 103 */       return this._member;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.BeanProperty
 * JD-Core Version:    0.6.0
 */