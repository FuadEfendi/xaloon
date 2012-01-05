<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- imports the original docbook stylesheet -->
  <xsl:import href="urn:docbkx:stylesheet"/>

  <!-- set bellow all your custom xsl configuration -->

  

  <!--
    Important links:
    - http://www.sagehill.net/docbookxsl/
    - http://docbkx-tools.sourceforge.net/
  -->
	<xsl:attribute-set name="normal.para.spacing">
		<xsl:attribute name="text-indent">24pt</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>