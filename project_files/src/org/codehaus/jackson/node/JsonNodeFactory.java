/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class JsonNodeFactory
/*     */ {
/*  20 */   public static final JsonNodeFactory instance = new JsonNodeFactory();
/*     */ 
/*     */   public BooleanNode booleanNode(boolean v)
/*     */   {
/*  35 */     return v ? BooleanNode.getTrue() : BooleanNode.getFalse();
/*     */   }
/*     */ 
/*     */   public NullNode nullNode()
/*     */   {
/*  42 */     return NullNode.getInstance();
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(byte v)
/*     */   {
/*  54 */     return IntNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(short v)
/*     */   {
/*  60 */     return IntNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(int v)
/*     */   {
/*  66 */     return IntNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(long v)
/*     */   {
/*  72 */     return LongNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(BigInteger v)
/*     */   {
/*  78 */     return BigIntegerNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(float v)
/*     */   {
/*  84 */     return DoubleNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(double v)
/*     */   {
/*  90 */     return DoubleNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public NumericNode numberNode(BigDecimal v)
/*     */   {
/*  96 */     return DecimalNode.valueOf(v);
/*     */   }
/*     */ 
/*     */   public TextNode textNode(String text)
/*     */   {
/* 108 */     return TextNode.valueOf(text);
/*     */   }
/*     */ 
/*     */   public BinaryNode binaryNode(byte[] data)
/*     */   {
/* 115 */     return BinaryNode.valueOf(data);
/*     */   }
/*     */ 
/*     */   public BinaryNode binaryNode(byte[] data, int offset, int length)
/*     */   {
/* 123 */     return BinaryNode.valueOf(data, offset, length);
/*     */   }
/*     */ 
/*     */   public ArrayNode arrayNode()
/*     */   {
/* 135 */     return new ArrayNode(this);
/*     */   }
/*     */ 
/*     */   public ObjectNode objectNode()
/*     */   {
/* 140 */     return new ObjectNode(this);
/*     */   }
/*     */ 
/*     */   public POJONode POJONode(Object pojo)
/*     */   {
/* 148 */     return new POJONode(pojo);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.JsonNodeFactory
 * JD-Core Version:    0.6.0
 */