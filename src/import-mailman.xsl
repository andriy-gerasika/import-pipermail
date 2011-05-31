<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml" xpath-default-namespace="http://www.w3.org/1999/xhtml">
	
	<xsl:output method="text"/>
	
	<xsl:template match="/">
		<xsl:value-of select="string-join(reverse(/html/body/table/tr/td[last()]/a/@href), '&#10;')" />
	</xsl:template>

</xsl:stylesheet>