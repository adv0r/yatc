/*    */ package org.codehaus.jackson.map.util;
/*    */ 
/*    */ public final class LinkedNode<T>
/*    */ {
/*    */   final T _value;
/*    */   final LinkedNode<T> _next;
/*    */ 
/*    */   public LinkedNode(T value, LinkedNode<T> next)
/*    */   {
/* 17 */     this._value = value;
/* 18 */     this._next = next;
/*    */   }
/*    */   public LinkedNode<T> next() {
/* 21 */     return this._next;
/*    */   }
/* 23 */   public T value() { return this._value;
/*    */   }
/*    */ 
/*    */   public static <ST> boolean contains(LinkedNode<ST> node, ST value)
/*    */   {
/* 37 */     while (node != null) {
/* 38 */       if (node.value() == value) {
/* 39 */         return true;
/*    */       }
/* 41 */       node = node.next();
/*    */     }
/* 43 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.LinkedNode
 * JD-Core Version:    0.6.0
 */