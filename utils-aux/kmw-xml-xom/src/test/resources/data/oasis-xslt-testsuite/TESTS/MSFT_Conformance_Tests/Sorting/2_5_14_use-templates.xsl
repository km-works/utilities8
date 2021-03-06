<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="/">
	<xsl:apply-templates select="//fruit">
		<xsl:sort select="elem/@a" order="ascending"/>
	</xsl:apply-templates>
</xsl:template>	

<xsl:template match="fruit">
	<xsl:value-of select="@name" />,<xsl:value-of select="elem/@a" />;
</xsl:template>

</xsl:stylesheet>