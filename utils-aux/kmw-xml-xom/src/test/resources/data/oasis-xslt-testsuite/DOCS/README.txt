Prototype XSLT/XPath 1.0 Conformance Test Suite

I. Introduction

This package is the complete test suite created by the OASIS
XSLT / XPath Conformance Technical Committee.  Its purpose is to
provide tests and tools from which to build a test harness for a
specific XSLT processor, running on a particular platform.  Note
that we have not provided a complete test harness here.

This test suite covers all of XSLT 1.0 and XPath 1.0, and consists
of 3243 tests.

II. Package Contents

There are 3 top-level directories -- DOCS, TESTS and TOOLS.

DOCS: -- documentation
    README.txt -- this document
    HowToSub.htm -- details about the catalog format (long)

TESTS: -- contains all test subdirectories
    MSFT_Conformance_Tests
    Xalan_Conformance_Tests

TOOLS: -- tools and scripts
    README.txt -- tools documentation
    infoset.xsl -- stylesheet to convert output to standard form

III. How to Use the Tests

A. Creating a test script

You will run the tests using a test harness (if you have one), or with a test
script.  You can use the catalog file, catalog.xml, in the TESTS directory.
Use it as sources of filenames to create the script, or to create a control file
for the test harness.  The format of the catalog files is simple, and you can
use a stylesheet, or your favorite scripting language, to process it.

B. Running the tests

To run the script, you need to decide where the output from the test will be
stored.  The two usual choices are to put them in amongst the tests themselves,
or to create a separate output subdirectory.  One other issue to be careful
about is to make sure your XSLT processor is executing in the proper directory
for each test.

C. Checking your output

XML Documents may be logically equivalent without being physically identical.
(See Canonical XML 1.0, http://www.w3.org/TR/2001/REC- xml-c14n-20010315) We
have provided reference output files in the TEST subdirectories.  In order to
check that test output matches the reference files, it will be necessary to
convert both your output and the reference files to a standard form.  We have
provided a simple stylesheet, infoset.xsl, which produces standardized (very
expanded) output.  There are also publicly available tools that create
canonical XML.  You must use one of these tools in order to do meaningful
output comparisons.  See README.txt in the TOOLS subdirectory for one source
of freely available XML and HTML canonicalizers.

IV. Issues

The tests provided by this testsuite have been used extensively by various
organizations, and are believed to be accurate.  The OASIS XSLT / XPath
Conformance Technical Committee will be happy to help resolve any cases in
which users believe a test to be inaccurate.


