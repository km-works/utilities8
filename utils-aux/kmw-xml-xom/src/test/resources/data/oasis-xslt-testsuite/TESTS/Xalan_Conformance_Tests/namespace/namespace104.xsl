<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns="testguys.com">

  <!-- FileName: namespace104 -->
  <!-- Document: http://www.w3.org/TR/xslt -->
  <!-- DocVersion: 19991116 -->
  <!-- Section: 7.1.2 Creating Elements -->
  <!-- Creator: David Marston -->
  <!-- Purpose: Specify an empty namespace; stylesheet default NS set, and reset in xsl:element. -->

<xsl:template match = "/">
  <out>
    <xsl:element namespace="" name="foo" xmlns="">
      <yyy/>
    </xsl:element>
  </out>
</xsl:template>

</xsl:stylesheet>


