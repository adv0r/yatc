/*    */ package org.codehaus.jackson.sym;
/*    */ 
/*    */ public final class Name3 extends Name
/*    */ {
/*    */   final int mQuad1;
/*    */   final int mQuad2;
/*    */   final int mQuad3;
/*    */ 
/*    */   Name3(String name, int hash, int q1, int q2, int q3)
/*    */   {
/* 17 */     super(name, hash);
/* 18 */     this.mQuad1 = q1;
/* 19 */     this.mQuad2 = q2;
/* 20 */     this.mQuad3 = q3;
/*    */   }
/*    */ 
/*    */   public boolean equals(int quad)
/*    */   {
/* 25 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean equals(int quad1, int quad2) {
/* 29 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean equals(int[] quads, int qlen)
/*    */   {
/* 34 */     return (qlen == 3) && (quads[0] == this.mQuad1) && (quads[1] == this.mQuad2) && (quads[2] == this.mQuad3);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.sym.Name3
 * JD-Core Version:    0.6.0
 */