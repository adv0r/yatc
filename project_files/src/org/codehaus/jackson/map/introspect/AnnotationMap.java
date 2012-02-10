/*    */ package org.codehaus.jackson.map.introspect;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.HashMap;
/*    */ import org.codehaus.jackson.map.util.Annotations;
/*    */ 
/*    */ public final class AnnotationMap
/*    */   implements Annotations
/*    */ {
/*    */   protected HashMap<Class<? extends Annotation>, Annotation> _annotations;
/*    */ 
/*    */   public <A extends Annotation> A get(Class<A> cls)
/*    */   {
/* 23 */     if (this._annotations == null) {
/* 24 */       return null;
/*    */     }
/* 26 */     return (Annotation)this._annotations.get(cls);
/*    */   }
/*    */ 
/*    */   public int size() {
/* 30 */     return this._annotations == null ? 0 : this._annotations.size();
/*    */   }
/*    */ 
/*    */   public void addIfNotPresent(Annotation ann)
/*    */   {
/* 39 */     if ((this._annotations == null) || (!this._annotations.containsKey(ann.annotationType())))
/* 40 */       _add(ann);
/*    */   }
/*    */ 
/*    */   public void add(Annotation ann)
/*    */   {
/* 48 */     _add(ann);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 54 */     if (this._annotations == null) {
/* 55 */       return "[null]";
/*    */     }
/* 57 */     return this._annotations.toString();
/*    */   }
/*    */ 
/*    */   protected final void _add(Annotation ann)
/*    */   {
/* 68 */     if (this._annotations == null) {
/* 69 */       this._annotations = new HashMap();
/*    */     }
/* 71 */     this._annotations.put(ann.annotationType(), ann);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotationMap
 * JD-Core Version:    0.6.0
 */