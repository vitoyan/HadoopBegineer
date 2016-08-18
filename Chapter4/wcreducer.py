#!/usr/bin/env python
import sys

count = 0
current = None

for line in sys.stdin.readlines():
	word, count2 = line.split("\t")
	if word == current:
		count = int(count) + int(count2)
	else:
		if current != None:
			sys.stdout.write(current.strip() + "\t" + str(count)+"\n")
		current = word
		count = int(count2)
sys.stdout.write(current.strip() + "\t" + str(count)+"\n")