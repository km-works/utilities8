<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/bookstore/book/title">
-
        <xsl:value-of select="current()"></xsl:value-of>
    </xsl:template>
    <xsl:template match="*">
	<xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>