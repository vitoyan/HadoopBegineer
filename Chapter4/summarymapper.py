#!/usr/bin/env python

import sys

for line in sys.stdin:
	words = line.split("\t")
	keys = ["sighted","recorded","location","shape","duration","description"]
	sys.stdout.write("total\t1\n")
	if(len(words) != 6):
		sys.stdout.write("badline\t1\n")
	for i in range(6 if len(words) >= 6 else 4):
		sys.stdout.write(keys[i] + "\t1\n")


