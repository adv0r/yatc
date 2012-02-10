/*     */ package org.codehaus.jackson.map.annotate;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import org.codehaus.jackson.annotate.JacksonAnnotation;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ 
/*     */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PARAMETER})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @JacksonAnnotation
/*     */ public @interface JsonSerialize
/*     */ {
/*     */   public abstract Class<? extends JsonSerializer<?>> using();
/*     */ 
/*     */   public abstract Class<?> as();
/*     */ 
/*     */   public abstract Typing typing();
/*     */ 
/*     */   public abstract Inclusion include();
/*     */ 
/*     */   public static enum Typing
/*     */   {
/* 141 */     DYNAMIC, 
/*     */ 
/* 147 */     STATIC;
/*     */   }
/*     */ 
/*     */   public static enum Inclusion
/*     */   {
/* 111 */     ALWAYS, 
/*     */ 
/* 117 */     NON_NULL, 
/*     */ 
/* 127 */     NON_DEFAULT;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.annotate.JsonSerialize
 * JD-Core Version:    0.6.0
 */