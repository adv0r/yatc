/*    */ package org.codehaus.jackson.map.type;
/*    */ 
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public abstract class TypeBase extends JavaType
/*    */ {
/*    */   volatile String _canonicalName;
/*    */ 
/*    */   protected TypeBase(Class<?> raw, int hash)
/*    */   {
/* 13 */     super(raw, hash);
/*    */   }
/*    */ 
/*    */   public String toCanonical()
/*    */   {
/* 19 */     String str = this._canonicalName;
/* 20 */     if (str == null) {
/* 21 */       str = buildCanonicalName();
/*    */     }
/* 23 */     return str;
/*    */   }
/*    */ 
/*    */   protected abstract String buildCanonicalName();
/*    */ 
/*    */   protected final JavaType copyHandlers(JavaType fromType) {
/* 30 */     this._valueHandler = fromType.getValueHandler();
/* 31 */     this._typeHandler = fromType.getTypeHandler();
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */   public abstract StringBuilder getGenericSignature(StringBuilder paramStringBuilder);
/*    */ 
/*    */   public abstract StringBuilder getErasedSignature(StringBuilder paramStringBuilder);
/*    */ 
/*    */   protected static StringBuilder _classSignature(Class<?> cls, StringBuilder sb, boolean trailingSemicolon)
/*    */   {
/* 54 */     if (cls.isPrimitive()) {
/* 55 */       if (cls == Boolean.TYPE)
/* 56 */         sb.append('Z');
/* 57 */       else if (cls == Byte.TYPE) {
/* 58 */         sb.append('B');
/*    */       }
/* 60 */       else if (cls == Short.TYPE) {
/* 61 */         sb.append('S');
/*    */       }
/* 63 */       else if (cls == Character.TYPE) {
/* 64 */         sb.append('C');
/*    */       }
/* 66 */       else if (cls == Integer.TYPE) {
/* 67 */         sb.append('I');
/*    */       }
/* 69 */       else if (cls == Long.TYPE) {
/* 70 */         sb.append('J');
/*    */       }
/* 72 */       else if (cls == Float.TYPE) {
/* 73 */         sb.append('F');
/*    */       }
/* 75 */       else if (cls == Double.TYPE) {
/* 76 */         sb.append('D');
/*    */       }
/* 78 */       else if (cls == Void.TYPE)
/* 79 */         sb.append('V');
/*    */       else
/* 81 */         throw new IllegalStateException("Unrecognized primitive type: " + cls.getName());
/*    */     }
/*    */     else {
/* 84 */       sb.append('L');
/* 85 */       String name = cls.getName();
/* 86 */       int i = 0; for (int len = name.length(); i < len; i++) {
/* 87 */         char c = name.charAt(i);
/* 88 */         if (c == '.') c = '/';
/* 89 */         sb.append(c);
/*    */       }
/* 91 */       if (trailingSemicolon) {
/* 92 */         sb.append(';');
/*    */       }
/*    */     }
/* 95 */     return sb;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.TypeBase
 * JD-Core Version:    0.6.0
 */