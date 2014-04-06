#!/usr/bin/env python2
# -*- coding: utf-8 -*-

import sys, os, math as m, random

os.chdir(os.path.dirname(sys.argv[0]))

print """{"""

print """
\\makeatletter
\\newcommand\\nobreakcline[1]{\\@nobreakcline#1\\@nil}%
\\def\\@nobreakcline#1-#2\\@nil{%
  \\omit
  \\@multicnt#1%
  \\advance\\@multispan\\m@ne
  \\ifnum\\@multicnt=\\@ne\\@firstofone{&\\omit}\\fi
  \\@multicnt#2%
  \\advance\@multicnt-#1%
  \\advance\@multispan\@ne
  \\leaders\\hrule\\@height\\arrayrulewidth\\hfill\\\\*
  \\noalign{\\vskip-\\arrayrulewidth}}
\\makeatother
"""

print """\\begingroup
\\setlength{\LTleft}{-20cm plus -1fill}
\\setlength{\LTright}{\LTleft}
\\ltabela{Wyniki przeprowadzonych prób}{|c|c|c|c|c|c|c|c|}{ \\hline"""

print """\\textbf{\\#} & \\textbf{Zestaw} & \\textbf{Zb. tr./ucz.} & \\textbf{Podobieństwo} & $\mathbf{k}$ & \\textbf{Kategoria} & \\textbf{TPR} & \\textbf{PPV} \\\\
\\hline
\\endhead"""

T = {'countries': 'Kraje', 'metric': '$R^n$', 'c\_allwords': 'wszystko', 'euclidean': '$d_e$',
     'jaccard': 'Jaccard', 'keywords': 'Wg. sł. kluczowych', 'chebyshev': '$d_\infty$',
     'taxicab': '$d_1$', 'topics': 'Tematy', 'c\_fuzzy': 'zb. rozm.', 'c\_keywords': 'wybr. sł.', 'sb': 'Autorzy', 'ngram': 'N-gramy'}
def t(s):
  return T[s] if s in T else s

h = open('trials.txt', 'rt')
i = 0
for l in h:
  l = l.strip()
  if not l or l.startswith('#'): continue
  name, size, rand, overl, simil, contents, metric, k = l.split()
  size = float(size)
  rand = True if rand == 'yes' else False
  overl = True if overl == 'yes' else False
  d = name[:name.find('-')]
  p = os.path.join(d, name + '2.csv')
  data = []
  g = open(p, 'rt')
  for gl in g.readlines()[1:]:
    gl = gl.strip()
    if not gl: continue
    cat, _, tpr, __, ppv = gl.split(',')
    tpr = float(tpr[:-1])
    ppv = float(ppv[:-1])
    if m.isnan(tpr): tpr = 0.0
    if m.isnan(ppv): ppv = 0.0
    data.append([cat, tpr, ppv])
  g.close()
  mr = lambda s: '\multirow{%d}{*}{%s}' % (len(data) + 1, s)
  ff = lambda f: ('%.2f' % f).replace('.', ',')
  msg_sets = '$%.0f\\%%$/$%.0f\\%%$' % (40 * size, 60 * size)
  if rand: msg_sets += ', losowanie'
  if overl: msg_sets += ', zachodzenie'
  msg_simil = t(simil)
  if simil == 'metric':
    msg_simil += ', ' + t('c\_' + contents) + ', ' + t(metric)
  data.sort(key = lambda x: x[0])
  print '%s & %s & %s & %s & %s & %s & $%s\\%%$ & $%s\\%%$ \\\\*' % (mr(str(i+1)), mr(t(d)), mr(msg_sets), mr(msg_simil), mr(str(k)), data[0][0], ff(data[0][1]), ff(data[0][2]))
  for j in xrange(1, len(data)):
    print ' & & & & & %s & $%s\\%%$ & $%s\\%%$ \\\\*' % (data[j][0], ff(data[j][1]), ff(data[j][2]))
  print '\\nobreakcline{6-8}'
  p = os.path.join(d, name + '1.csv')
  g = open(p, 'rt')
  count = 0
  good = 0
  for gl in g.readlines()[1:]:
    gl = gl.strip()
    if not gl: continue
    _, lll, rrr = gl.split(',')
    count += 1
    good += (lll == rrr)
  g.close()
  succ = 0.0 if count == 0 else (100.0 * good) / count
  print '& & & & & \\textbf{poprawne:} & \\multicolumn{2}{c|}{$\\mathbf{%s\\%%}$} \\\\' % (ff(succ))
  print '\\hline'
  i += 1
  
h.close()

print """}\\endgroup"""

print """}"""