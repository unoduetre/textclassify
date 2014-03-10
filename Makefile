# Change the variable below to make them work for your system if neccessary
#

prefix=.
bindir=$(prefix)/bin
srcdir=$(prefix)/src
unittestsdir=$(prefix)/tests/unit
integrationtestsdir=$(prefix)/tests/integration
testsbindir=$(prefix)/tests/bin
libdir=$(prefix)/lib
datadir=$(prefix)/data
docdir=$(prefix)/doc
manifest=$(prefix)/manifest.txt
jarfile=$(prefix)/application.jar

SHELL=/bin/sh
JAVA=java
#JAVAFLAGS=
JAVAC=javac
JAVACFLAGS=-Xlint:unchecked
JAR=jar
#JARFLAGS=
RM=rm -f
#RMFLAGS=
INSTALL=install
#INSTALLFLAGS=
INSTALL_PROGRAM=$(INSTALL)
INSTALL_DATA=$(INSTALL) -m 644
ECHO=echo
#ECHOFLAGS=
override ECHOFLAGS+= -e

# Do not modify anything below this line

rwildcard=$(foreach path,$(wildcard $1/*),$(call rwildcard,$(path),$2) $(filter $2,$(path)))
classes=$(subst $(srcdir),$(bindir),$(patsubst %.java,%.class,$(call rwildcard,$(srcdir),%.java)))
testclasses=$(patsubst %.java,%.class,$(subst $(unittestsdir),$(testsbindir),$(call rwildcard,$(unittestsdir),%.java))) $(patsubst %.java,%.class,$(subst $(integrationtestsdir),$(testsbindir),$(call rwildcard,$(integrationtestsdir),%.java)))
nontestclasses=$(subst $(bindir),$(testsbindir),$(classes))
runtests=$(foreach testclass,$(subst /,.,$(subst .class,,$(subst $(testsbindir)/,,$(testclasses)))),$(JAVA) -classpath $(libdir)/*:$(testsbindir) org.junit.runner.JUnitCore $(testclass);)

.SUFFIXES:
.SUFFIXES: .java .class .jar 

.DELETE_ON_ERROR:

.PHONY: help run dist all test clean doc dvi ps pdf TAGS

$(bindir)/%.class: $(srcdir)/%.java
	$(JAVAC) $(JAVACFLAGS) -classpath $(libdir)/*:$(srcdir) -d $(bindir) $<

$(testsbindir)/%.class: $(srcdir)/%.java
	$(JAVAC) $(JAVACFLAGS) -classpath $(libdir)/*:$(srcdir) -d $(testsbindir) $<

$(testsbindir)/%.class: $(unittestsdir)/%.java
	$(JAVAC) $(JAVACFLAGS) -classpath $(libdir)/*:$(srcdir):$(unittestsdir) -d $(testsbindir) $<

$(testsbindir)/%.class: $(integrationtestsdir)/%.java
	$(JAVAC) $(JAVACFLAGS) -classpath $(libdir)/*:$(srcdir):$(integrationtestsdir) -d $(testsbindir) $<

%.jar: 
	$(JAR) $(JARFLAGS) -cfm $(jarfile) $(manifest) -C $(bindir) .

help:
	@$(ECHO) $(ECHOFLAGS)
	@$(ECHO) $(ECHOFLAGS) "Usage: make target"
	@$(ECHO) $(ECHOFLAGS)
	@$(ECHO) $(ECHOFLAGS) "The possible targets for the make command are as follows:"
	@$(ECHO) $(ECHOFLAGS) "\thelp - print this help information"
	@$(ECHO) $(ECHOFLAGS) "\trun - run the program (compile and create jar if needed)"
	@$(ECHO) $(ECHOFLAGS) "\tdist - create a runnable jar file with the whole application"
	@$(ECHO) $(ECHOFLAGS) "\tall - compile everything"
	@$(ECHO) $(ECHOFLAGS) "\ttest - run tests"
	@$(ECHO) $(ECHOFLAGS) "\tclean - remove all compiled files and the jar file"
	@$(ECHO) $(ECHOFLAGS) "\tdoc - generate documentation in all formats"
	@$(ECHO) $(ECHOFLAGS) "\tdvi - generate documentation in the dvi format"
	@$(ECHO) $(ECHOFLAGS) "\tps - generate documentation in the ps format"
	@$(ECHO) $(ECHOFLAGS) "\tpdf - generate documentation in the pdf format"
	@$(ECHO) $(ECHOFLAGS) "\tTAGS - create a tags file to use in vim"
	@$(ECHO) $(ECHOFLAGS)

run: $(jarfile)
	@$(JAVA) -jar $(jarfile)

dist: $(jarfile)

$(jarfile): $(classes)

all: $(classes)

test: $(testclasses) $(nontestclasses)
	@$(runtests)
	

clean:
	rm -f $(jarfile)
	rm -rf $(bindir)/*
	rm -rf $(testsbindir)/*
	$(MAKE) -C doc clean

doc: dvi ps pdf

dvi:
	$(MAKE) -C doc dvi

ps:
	$(MAKE) -C doc ps

pdf:
	$(MAKE) -C doc pdf

TAGS:
	ctags --languages=Java -R .
