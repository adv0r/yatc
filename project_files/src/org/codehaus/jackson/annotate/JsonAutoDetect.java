/*    */ package org.codehaus.jackson.annotate;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ @Target({java.lang.annotation.ElementType.TYPE})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @JacksonAnnotation
/*    */ public @interface JsonAutoDetect
/*    */ {
/*    */   public abstract JsonMethod[] value();
/*    */ 
/*    */   public abstract Visibility getterVisibility();
/*    */ 
/*    */   public abstract Visibility isGetterVisibility();
/*    */ 
/*    */   public abstract Visibility setterVisibility();
/*    */ 
/*    */   public abstract Visibility creatorVisibility();
/*    */ 
/*    */   public abstract Visibility fieldVisibility();
/*    */ 
/*    */   public static enum Visibility
/*    */   {
/* 50 */     ANY, 
/*    */ 
/* 55 */     NON_PRIVATE, 
/*    */ 
/* 61 */     PROTECTED_AND_PUBLIC, 
/*    */ 
/* 66 */     PUBLIC_ONLY, 
/*    */ 
/* 72 */     NONE, 
/*    */ 
/* 79 */     DEFAULT;
/*    */ 
/*    */     public boolean isVisible(Member m) {
/* 82 */       switch (JsonAutoDetect.1.$SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[ordinal()]) {
/*    */       case 1:
/* 84 */         return true;
/*    */       case 2:
/* 86 */         return false;
/*    */       case 3:
/* 88 */         return !Modifier.isPrivate(m.getModifiers());
/*    */       case 4:
/* 90 */         if (!Modifier.isProtected(m.getModifiers())) break;
/* 91 */         return true;
/*    */       case 5:
/* 95 */         return Modifier.isPublic(m.getModifiers());
/*    */       }
/* 97 */       return false;
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.annotate.JsonAutoDetect
 * JD-Core Version:    0.6.0
 */