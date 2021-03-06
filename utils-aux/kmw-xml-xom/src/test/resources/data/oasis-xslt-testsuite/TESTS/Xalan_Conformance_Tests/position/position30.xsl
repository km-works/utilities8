<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- FileName: position30 -->
  <!-- Document: http://www.w3.org/TR/xpath -->
  <!-- DocVersion: 19991116 -->
  <!-- Section: 2.4 -->
  <!-- Creator: Paul Dick -->
  <!-- Purpose: Test of shorthand positional indexing with wildcard. Look for first. -->

<xsl:template match="doc">
  <out>
    <xsl:value-of select="*[1]"/>
  </out>
</xsl:template>

</xsl:stylesheet>
