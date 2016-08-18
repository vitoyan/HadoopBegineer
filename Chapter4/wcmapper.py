#!/usr/bin/env python

import sys

for line in sys.stdin:
	words = line.split("\t")
	for word in words:
		sys.stdout.write(word.strip() + "\t"+"1\n")