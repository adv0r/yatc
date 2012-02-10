/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Type;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class AnnotatedParameter extends AnnotatedMember
/*     */ {
/*     */   protected final AnnotatedMember _owner;
/*     */   protected final Type _type;
/*     */   protected final AnnotationMap _annotations;
/*     */ 
/*     */   public AnnotatedParameter(AnnotatedMember owner, Type type, AnnotationMap ann)
/*     */   {
/*  47 */     this._owner = owner;
/*  48 */     this._type = type;
/*  49 */     this._annotations = ann;
/*     */   }
/*     */ 
/*     */   public void addOrOverride(Annotation a)
/*     */   {
/*  54 */     this._annotations.add(a);
/*     */   }
/*     */ 
/*     */   public AnnotatedElement getAnnotated()
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   public int getModifiers()
/*     */   {
/*  75 */     return this._owner.getModifiers();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  82 */     return "";
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  91 */     return this._annotations.get(acls);
/*     */   }
/*     */ 
/*     */   public Type getGenericType()
/*     */   {
/*  96 */     return this._type;
/*     */   }
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/* 101 */     JavaType t = TypeFactory.type(this._type);
/* 102 */     return t.getRawClass();
/*     */   }
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 113 */     return this._owner.getDeclaringClass();
/*     */   }
/*     */ 
/*     */   public Member getMember()
/*     */   {
/* 121 */     return this._owner.getMember();
/*     */   }
/*     */ 
/*     */   public Type getParameterType()
/*     */   {
/* 130 */     return this._type;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedParameter
 * JD-Core Version:    0.6.0
 */