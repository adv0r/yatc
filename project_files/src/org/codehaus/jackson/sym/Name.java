/*    */ package org.codehaus.jackson.sym;
/*    */ 
/*    */ public abstract class Name
/*    */ {
/*    */   protected final String _name;
/*    */   protected final int _hashCode;
/*    */ 
/*    */   protected Name(String name, int hashCode)
/*    */   {
/* 17 */     this._name = name;
/* 18 */     this._hashCode = hashCode;
/*    */   }
/*    */   public String getName() {
/* 21 */     return this._name;
/*    */   }
/*    */ 
/*    */   public abstract boolean equals(int paramInt);
/*    */ 
/*    */   public abstract boolean equals(int paramInt1, int paramInt2);
/*    */ 
/*    */   public abstract boolean equals(int[] paramArrayOfInt, int paramInt);
/*    */ 
/*    */   public String toString()
/*    */   {
/* 41 */     return this._name;
/*    */   }
/* 43 */   public final int hashCode() { return this._hashCode;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 48 */     return o == this;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.sym.Name
 * JD-Core Version:    0.6.0
 */