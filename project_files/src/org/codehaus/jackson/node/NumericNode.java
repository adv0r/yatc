/*    */ package org.codehaus.jackson.node;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import org.codehaus.jackson.JsonParser.NumberType;
/*    */ 
/*    */ public abstract class NumericNode extends ValueNode
/*    */ {
/*    */   public final boolean isNumber()
/*    */   {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   public abstract JsonParser.NumberType getNumberType();
/*    */ 
/*    */   public abstract Number getNumberValue();
/*    */ 
/*    */   public abstract int getIntValue();
/*    */ 
/*    */   public abstract long getLongValue();
/*    */ 
/*    */   public abstract double getDoubleValue();
/*    */ 
/*    */   public abstract BigDecimal getDecimalValue();
/*    */ 
/*    */   public abstract BigInteger getBigIntegerValue();
/*    */ 
/*    */   public abstract String getValueAsText();
/*    */ 
/*    */   public int getValueAsInt()
/*    */   {
/* 48 */     return getIntValue();
/*    */   }
/*    */ 
/*    */   public int getValueAsInt(int defaultValue) {
/* 52 */     return getIntValue();
/*    */   }
/*    */ 
/*    */   public long getValueAsLong()
/*    */   {
/* 57 */     return getLongValue();
/*    */   }
/*    */ 
/*    */   public long getValueAsLong(long defaultValue) {
/* 61 */     return getLongValue();
/*    */   }
/*    */ 
/*    */   public double getValueAsDouble()
/*    */   {
/* 66 */     return getDoubleValue();
/*    */   }
/*    */ 
/*    */   public double getValueAsDouble(double defaultValue) {
/* 70 */     return getDoubleValue();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.NumericNode
 * JD-Core Version:    0.6.0
 */