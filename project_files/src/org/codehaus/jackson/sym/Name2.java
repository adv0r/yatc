/*    */ package org.codehaus.jackson.sym;
/*    */ 
/*    */ public final class Name2 extends Name
/*    */ {
/*    */   final int mQuad1;
/*    */   final int mQuad2;
/*    */ 
/*    */   Name2(String name, int hash, int quad1, int quad2)
/*    */   {
/* 21 */     super(name, hash);
/* 22 */     this.mQuad1 = quad1;
/* 23 */     this.mQuad2 = quad2;
/*    */   }
/*    */ 
/*    */   public boolean equals(int quad) {
/* 27 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean equals(int quad1, int quad2)
/*    */   {
/* 32 */     return (quad1 == this.mQuad1) && (quad2 == this.mQuad2);
/*    */   }
/*    */ 
/*    */   public boolean equals(int[] quads, int qlen)
/*    */   {
/* 38 */     return (qlen == 2) && (quads[0] == this.mQuad1) && (quads[1] == this.mQuad2);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.sym.Name2
 * JD-Core Version:    0.6.0
 */