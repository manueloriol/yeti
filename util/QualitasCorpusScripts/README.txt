Running large-scale experiments using the Qualitas Corpus
---------------------------------------------------------

The Qualitas Corpus (http://qualitascorpus.com/) is a group 
of projects maintained by Ewan Tempero.

To download it, contact directly Ewan Tempero.

This directory contains a set of scripts to run large-scale 
experiments on programs of the Qualitas Corpus using YETI.
To run the tests, first copy all the scripts in the Systems
subdirectory of the Qualitas Corpus.

We explain briefly what each script does.

Makefile : 
  - target "main" propagates Makefiles and creates test scripts
  - target "tests", run all tests
  - target "clean", cleans all directories

Makefile.template : is a Makefile that will be propagated to 
all in all projects.
As a first step, write (or reuse) a YetiExperiment.
Replace yeti.experimenter.YetiExperiment100PicksRandom in 
Makefile.template by this experiment.

propagateMakefile.sh : this script copies Makefile.template
in each subdirectory as a regular Makefile

createTestScripts.sh : this script calls all "main" targets
in the subdirectories. This creates the test scripts according 
to the chosen YetiExperiment. 

runTests.sh : runs all tests in an iterative manner.

cleanTests.sh : removes all duplicated Makefiles as well as 
test scripts and results.

runTestsComplement.sh : iterates through all projects and 
launches a test session if it was not launched already.

loopRunTests.sh : launches runTestComplement.sh every hour.
This is useful to perform tests on multicore machines with 
enough cores to run everything (like a MacPro) and make sure 
that all projects are tested.


