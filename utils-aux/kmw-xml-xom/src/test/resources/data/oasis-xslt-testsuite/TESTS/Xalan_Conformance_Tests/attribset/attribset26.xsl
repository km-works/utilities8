<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- FileName: attribset26 -->
  <!-- Document: http://www.w3.org/TR/xslt -->
  <!-- DocVersion: 19991116 -->
  <!-- Section: 7.1.4 Named Attribute Sets -->
  <!-- Paragraph: 3 -->
  <!-- Purpose: Use xsl:copy with multiple attribute sets that inherit,
    but have conflicts. -->
  <!-- Author: Carmelo Montanez --><!-- ResultTree004 in NIST suite -->

<xsl:template match="/">
  <out>
    <xsl:apply-templates/>
  </out>
</xsl:template>

<xsl:template match="doc">
  <xsl:copy use-attribute-sets="set1"/>
</xsl:template>

<xsl:attribute-set name="set1" use-attribute-sets="set2">
  <xsl:attribute name="color">black</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="set2" use-attribute-sets="set3">
  <xsl:attribute name="color">blue</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="set3">
  <xsl:attribute name="color">green</xsl:attribute>
</xsl:attribute-set>

</xsl:stylesheet>