/*    */ package org.codehaus.jackson.sym;
/*    */ 
/*    */ public final class Name1 extends Name
/*    */ {
/* 15 */   static final Name1 sEmptyName = new Name1("", 0, 0);
/*    */   final int mQuad;
/*    */ 
/*    */   Name1(String name, int hash, int quad)
/*    */   {
/* 21 */     super(name, hash);
/* 22 */     this.mQuad = quad;
/*    */   }
/*    */   static final Name1 getEmptyName() {
/* 25 */     return sEmptyName;
/*    */   }
/*    */ 
/*    */   public boolean equals(int quad)
/*    */   {
/* 30 */     return quad == this.mQuad;
/*    */   }
/*    */ 
/*    */   public boolean equals(int quad1, int quad2)
/*    */   {
/* 36 */     return (quad1 == this.mQuad) && (quad2 == 0);
/*    */   }
/*    */ 
/*    */   public boolean equals(int[] quads, int qlen)
/*    */   {
/* 42 */     return (qlen == 1) && (quads[0] == this.mQuad);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.sym.Name1
 * JD-Core Version:    0.6.0
 */