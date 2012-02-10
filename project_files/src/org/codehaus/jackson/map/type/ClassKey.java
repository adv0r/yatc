/*    */ package org.codehaus.jackson.map.type;
/*    */ 
/*    */ public final class ClassKey
/*    */   implements Comparable<ClassKey>
/*    */ {
/*    */   String _className;
/*    */   Class<?> _class;
/*    */   int _hashCode;
/*    */ 
/*    */   public ClassKey()
/*    */   {
/* 34 */     this._class = null;
/* 35 */     this._className = null;
/* 36 */     this._hashCode = 0;
/*    */   }
/*    */ 
/*    */   public ClassKey(Class<?> clz)
/*    */   {
/* 41 */     this._class = clz;
/* 42 */     this._className = clz.getName();
/* 43 */     this._hashCode = this._className.hashCode();
/*    */   }
/*    */ 
/*    */   public void reset(Class<?> clz)
/*    */   {
/* 48 */     this._class = clz;
/* 49 */     this._className = clz.getName();
/* 50 */     this._hashCode = this._className.hashCode();
/*    */   }
/*    */ 
/*    */   public int compareTo(ClassKey other)
/*    */   {
/* 62 */     return this._className.compareTo(other._className);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 74 */     if (o == this) return true;
/* 75 */     if (o == null) return false;
/* 76 */     if (o.getClass() != getClass()) return false;
/* 77 */     ClassKey other = (ClassKey)o;
/*    */ 
/* 86 */     return other._class == this._class;
/*    */   }
/*    */   public int hashCode() {
/* 89 */     return this._hashCode;
/*    */   }
/* 91 */   public String toString() { return this._className;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.ClassKey
 * JD-Core Version:    0.6.0
 */