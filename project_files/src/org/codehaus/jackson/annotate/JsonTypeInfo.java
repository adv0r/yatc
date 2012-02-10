/*     */ package org.codehaus.jackson.annotate;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ 
/*     */ @Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @JacksonAnnotation
/*     */ public @interface JsonTypeInfo
/*     */ {
/*     */   public abstract Id use();
/*     */ 
/*     */   public abstract As include();
/*     */ 
/*     */   public abstract String property();
/*     */ 
/*     */   public static enum As
/*     */   {
/* 129 */     PROPERTY, 
/*     */ 
/* 142 */     WRAPPER_OBJECT, 
/*     */ 
/* 151 */     WRAPPER_ARRAY;
/*     */   }
/*     */ 
/*     */   public static enum Id
/*     */   {
/*  68 */     NONE(null), 
/*     */ 
/*  73 */     CLASS("@class"), 
/*     */ 
/*  92 */     MINIMAL_CLASS("@c"), 
/*     */ 
/*  98 */     NAME("@type"), 
/*     */ 
/* 105 */     CUSTOM(null);
/*     */ 
/*     */     private final String _defaultPropertyName;
/*     */ 
/*     */     private Id(String defProp) {
/* 111 */       this._defaultPropertyName = defProp;
/*     */     }
/*     */     public String getDefaultPropertyName() {
/* 114 */       return this._defaultPropertyName;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.annotate.JsonTypeInfo
 * JD-Core Version:    0.6.0
 */