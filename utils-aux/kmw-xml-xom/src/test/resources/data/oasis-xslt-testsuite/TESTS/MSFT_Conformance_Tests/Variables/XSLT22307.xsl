<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="x">1</xsl:param>
    <xsl:template match="text()"></xsl:template>
    <xsl:template match="book">
        <xsl:param name="x">Book</xsl:param>
        <xsl:for-each select="title">
            
******
            <xsl:value-of select="$x"></xsl:value-of>
            ******
            <xsl:value-of select="text()"></xsl:value-of>
        </xsl:for-each>
    </xsl:template>
    <xsl:template match="magazine">
        <xsl:param name="x">Magazine</xsl:param>
        <xsl:for-each select="title">
            
***
            <xsl:value-of select="$x"></xsl:value-of>
            ***
            <xsl:value-of select="text()"></xsl:value-of>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
