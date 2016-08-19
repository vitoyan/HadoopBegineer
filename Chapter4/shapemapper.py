#!/usr/bin/env python

import sys

for line in sys.stdin:
	words = line.split("\t")
	if (len(words) == 6):
		shape = words[3].strip()
		if shape:
			sys.stdout.write(shape + "\t1\n")
