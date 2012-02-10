/*    */ package org.codehaus.jackson;
/*    */ 
/*    */ public class Version
/*    */   implements Comparable<Version>
/*    */ {
/* 12 */   private static final Version UNKNOWN_VERSION = new Version(0, 0, 0, null);
/*    */   protected final int _majorVersion;
/*    */   protected final int _minorVersion;
/*    */   protected final int _patchLevel;
/*    */   protected final String _snapshotInfo;
/*    */ 
/*    */   public Version(int major, int minor, int patchLevel, String snapshotInfo)
/*    */   {
/* 29 */     this._majorVersion = major;
/* 30 */     this._minorVersion = minor;
/* 31 */     this._patchLevel = patchLevel;
/* 32 */     this._snapshotInfo = snapshotInfo;
/*    */   }
/*    */ 
/*    */   public static Version unknownVersion()
/*    */   {
/* 39 */     return UNKNOWN_VERSION;
/*    */   }
/* 41 */   public boolean isUknownVersion() { return this == UNKNOWN_VERSION; } 
/* 42 */   public boolean isSnapshot() { return (this._snapshotInfo != null) && (this._snapshotInfo.length() > 0); } 
/*    */   public int getMajorVersion() {
/* 44 */     return this._majorVersion; } 
/* 45 */   public int getMinorVersion() { return this._minorVersion; } 
/* 46 */   public int getPatchLevel() { return this._patchLevel;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 51 */     StringBuilder sb = new StringBuilder();
/* 52 */     sb.append(this._majorVersion).append('.');
/* 53 */     sb.append(this._minorVersion).append('.');
/* 54 */     sb.append(this._patchLevel);
/* 55 */     if (isSnapshot()) {
/* 56 */       sb.append('-').append(this._snapshotInfo);
/*    */     }
/* 58 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 63 */     return this._majorVersion + this._minorVersion + this._patchLevel;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 69 */     if (o == this) return true;
/* 70 */     if (o == null) return false;
/* 71 */     if (o.getClass() != getClass()) return false;
/* 72 */     Version other = (Version)o;
/* 73 */     return (other._majorVersion == this._majorVersion) && (other._minorVersion == this._minorVersion) && (other._patchLevel == this._patchLevel);
/*    */   }
/*    */ 
/*    */   public int compareTo(Version other)
/*    */   {
/* 81 */     int diff = this._majorVersion - other._majorVersion;
/* 82 */     if (diff == 0) {
/* 83 */       diff = this._minorVersion - other._minorVersion;
/* 84 */       if (diff == 0) {
/* 85 */         diff = this._patchLevel - other._patchLevel;
/*    */       }
/*    */     }
/* 88 */     return diff;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.Version
 * JD-Core Version:    0.6.0
 */