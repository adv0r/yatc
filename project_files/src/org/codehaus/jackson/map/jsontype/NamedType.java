/*    */ package org.codehaus.jackson.map.jsontype;
/*    */ 
/*    */ public final class NamedType
/*    */ {
/*    */   protected final Class<?> _class;
/*    */   protected final int _hashCode;
/*    */   protected String _name;
/*    */ 
/*    */   public NamedType(Class<?> c)
/*    */   {
/* 17 */     this(c, null);
/*    */   }
/*    */ 
/*    */   public NamedType(Class<?> c, String name) {
/* 21 */     this._class = c;
/* 22 */     this._hashCode = c.getName().hashCode();
/* 23 */     setName(name);
/*    */   }
/*    */   public Class<?> getType() {
/* 26 */     return this._class; } 
/* 27 */   public String getName() { return this._name; } 
/*    */   public void setName(String name) {
/* 29 */     this._name = ((name == null) || (name.length() == 0) ? null : name);
/*    */   }
/*    */   public boolean hasName() {
/* 32 */     return this._name != null;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 40 */     if (o == this) return true;
/* 41 */     if (o == null) return false;
/* 42 */     if (o.getClass() != getClass()) return false;
/* 43 */     return this._class == ((NamedType)o)._class;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 47 */     return this._hashCode;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return "[NamedType, class " + this._class.getName() + ", name: " + (this._name == null ? "null" : new StringBuilder().append("'").append(this._name).append("'").toString()) + "]";
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.NamedType
 * JD-Core Version:    0.6.0
 */