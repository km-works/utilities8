<?xml version="1.0"?> 
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-16"/>

<!-- XSLT - Numbering - format= "&#x0041;" (Finnish,Norwegian,Swedish,Denmark,US-Upper) -->
<!-- & = "&#x0061;" (Finnish,Norwegian,Swedish,Denmark,US-Lower) - lang = "no" & "sv" & "da" & "en" -->
<!-- letter-value="alphabetic" | "traditional" -->
<!-- Created : Khalil Jabrane -->
<!-- Date: 04/12/2000 -->

<xsl:template match="/">  
	<TABLE>  
		<xsl:for-each select="//humanoid">
			<TR><TD>  
				<TD>
					<!-- Finnish-Upper -->
					<xsl:number format="&#x0041;" letter-value="traditional"/> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>
					<!-- Finnish-Lower -->
					<xsl:number format="&#x0061;" lang="fi" letter-value="traditional" /> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>|---|</TD>
				<TD>
					<!-- Swedish-Upper -->
					<xsl:number format="&#x0041;" lang="sv" letter-value="traditional"/> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>
					<!-- Swedish-Lower -->
					<xsl:number format="&#x0061;" lang="sv" letter-value="traditional" /> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>|---|</TD>				
				<TD>
					<!-- Norwegian-Upper -->
					<xsl:number format="&#x0041;" lang="no" letter-value="traditional"/> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>
					<!-- Norwegian-Lower -->
					<xsl:number format="&#x0061;" lang="no" letter-value="traditional" /> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>|---|</TD>				
				<TD>
					<!-- Denmark-Upper -->
					<xsl:number format="&#x0041;" lang="da" letter-value="traditional"/> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>
					<!-- Denmark-Lower -->
					<xsl:number format="&#x0061;" lang="da" letter-value="traditional" /> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>|---|</TD>				
				<TD>
					<!-- US-Upper -->
					<xsl:number format="&#x0041;" lang="en" letter-value="traditional"/> *					
					<xsl:value-of select = "text()"/>
				</TD>
				<TD>
					<!-- US-Lower -->
					<xsl:number format="&#x0061;" lang="en" letter-value="traditional" /> *					
					<xsl:value-of select = "text()"/>
				</TD>
			</TD></TR>
		</xsl:for-each>  
	</TABLE>
</xsl:template>  

</xsl:stylesheet>  

