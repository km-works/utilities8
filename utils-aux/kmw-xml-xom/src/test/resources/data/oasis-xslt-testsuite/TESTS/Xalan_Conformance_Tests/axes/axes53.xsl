<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- FileName: AXES53 -->
  <!-- Document: http://www.w3.org/TR/xpath -->
  <!-- DocVersion: 19991116 -->
  <!-- Section: 2.2 -->
  <!-- Creator: David Marston -->
  <!-- Purpose: Test for named node followed by 'descendant::*' (i.e., all children and below). -->

<xsl:template match="/">
  <out>
    <xsl:for-each select="//near-north">
      <xsl:apply-templates select="center/descendant::*"/>
    </xsl:for-each>
  </out>
</xsl:template>

<xsl:template match="*">
  <xsl:value-of select="name(.)"/><xsl:text> </xsl:text>
</xsl:template>

</xsl:stylesheet>
