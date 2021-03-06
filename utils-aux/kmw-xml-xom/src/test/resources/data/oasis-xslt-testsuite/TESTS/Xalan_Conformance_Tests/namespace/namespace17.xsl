<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
      xmlns:anamespace="foo.com"
      exclude-result-prefixes="anamespace">

<xsl:output indent="yes"/>

  <!-- FileName: namespace17 -->
  <!-- Document: http://www.w3.org/TR/xslt -->
  <!-- DocVersion: 19991116 -->
  <!-- Section: 2.4 -->
  <!-- Creator: David Marston -->
  <!-- Purpose: Test xsl:exclude-result-prefixes, stylesheet level -->

<xsl:template match="/">
  <out>
    <p><xsl:attribute name="test" namespace="foo.com">true</xsl:attribute></p>
  </out>
</xsl:template>

</xsl:stylesheet>
