<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- FileName: namespace111 -->
  <!-- Document: http://www.w3.org/TR/xslt -->
  <!-- DocVersion: 19991116 -->
  <!-- Section: 7.1.2 Creating Elements -->
  <!-- Creator: David Marston -->
  <!-- Purpose: Set and reset default namespace, no prefixes involved. -->

<xsl:template match = "/">
  <out xmlns="abc">
    <xsl:element namespace="abc" name="foo" xmlns="">
      <xsl:element name="yyy"/>
    </xsl:element>
  </out>
</xsl:template>

</xsl:stylesheet>


