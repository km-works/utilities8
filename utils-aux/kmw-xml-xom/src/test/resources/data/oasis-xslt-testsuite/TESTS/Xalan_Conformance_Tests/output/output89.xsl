<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- FileName: output89 -->
  <!-- Document: http://www.w3.org/TR/xslt -->
  <!-- DocVersion: 19991116 -->
  <!-- Section: 7.4 Creating Comments -->
  <!-- Creator: David Marston -->
  <!-- Purpose: Check recovery when requested comment string contains two hyphens together -->
  <!-- Discretionary: comment-content-contains-delimiter="add-space" -->

<xsl:output method="xml" encoding="UTF-8"/>

<xsl:template match="/">
  <out>
    <xsl:comment>This cannot be inserted as--is.</xsl:comment>
  </out>
</xsl:template>

</xsl:stylesheet>