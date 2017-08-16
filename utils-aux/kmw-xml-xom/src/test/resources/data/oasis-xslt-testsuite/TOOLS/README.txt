
Checking your output (copied from the toplevel README)

XML Documents may be logically equivalent without being physically identical.
(See Canonical XML 1.0, http://www.w3.org/TR/2001/REC- xml-c14n-20010315) We
have provided reference output files in the TEST subdirectories.  In order to
check that test output matches the reference files, it will be necessary to
convert both your output and the reference files to a standard form.  We have
provided a simple stylesheet, infoset.xsl, which produces standardized (very
expanded) output.  There are also publicly available tools that create
canonical XML.  You must use one of these tools in order to do meaningful
output comparisons.

One source of freely available XML and HTML canonicalizers is:

    http://www.datapower.com/xmldev/xsltmark.html

This actually provides a complete test harness that will accomodate any XSLT
processor, and can be built on both Linux and Windows systems.

If you need only the XML and HTML canonicalizers, you only need to build the
dgnorm and htmlnorm programs.  Build instructions are provided.  You can also
build the entire test harness.  Creating the control files from the catalogs
we have provided is straghtforward.



