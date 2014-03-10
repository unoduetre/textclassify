document=main.tex
includedocuments=packages.tex
figures=
epsfigures=$(addsuffix .eps,$(basename $(figures)))
pdffigures=$(addsuffix .pdf,$(basename $(figures)))
CONVERT=convert
#CONVERTFLAGS=
LATEX=latex
#LATEXFLAGS=
PDFLATEX=pdflatex
#PDFLATEXFLAGS=
DVIPS=dvips
#DVIPSFLAGS=

documentbasename=$(basename $(document))

.PHONY: all dvi ps pdf clean

%.eps: %.xpm
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.xpm
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.eps: %.xbm
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.xbm
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.eps: %.gif
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.gif
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.eps: %.jpg
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.jpg
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.eps: %.jpeg
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.jpeg
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.eps: %.png
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.png
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.eps: %.tiff
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.tiff
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.eps: %.tif
	$(CONVERT) $(CONVERTFLAGS) $< $@
%.pdf: %.tif
	$(CONVERT) $(CONVERTFLAGS) $< $@

%.dvi: %.tex $(includedocuments) $(epsfigures)
	$(LATEX) $(LATEXFLAGS) $<
	$(LATEX) $(LATEXFLAGS) $<

%.ps: %.dvi $(epsfigures)
	$(DVIPS) $(DVIPSFLAGS) $<

%.pdf: %.tex $(includedocuments) $(pdffigures)
	$(PDFLATEX) $(PDFLATEXFLAGS) $<
	$(PDFLATEX) $(PDFLATEXFLAGS) $<

all: dvi ps pdf
dvi: $(documentbasename).dvi
ps: $(documentbasename).ps
pdf: $(documentbasename).pdf
$(documentbasename).dvi: $(includedocuments) $(epsfigures)
$(documentbasename).ps: $(epsfigures)
$(documentbasename).pdf: $(pdffigures)

clean:
	rm -f $(documentbasename).dvi 
	rm -f $(documentbasename).aux 
	rm -f $(documentbasename).log 
	rm -f $(documentbasename).ps 
	rm -f $(documentbasename).pdf 
	rm -f $(documentbasename).out 
	rm -f $(documentbasename).snm 
	rm -f $(documentbasename).toc 
	rm -f $(documentbasename).nav 
	rm -f $(documentbasename).bbl 
	rm -f $(documentbasename).blg
