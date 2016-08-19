#!/usr/bin/env ruby

while line = gets
	puts "total\t1"
	parts = line.split("\t")
	puts "badline\t1" if parts.size != 6
	puts "sighted\t1" if !parts[0].nil?
	puts "recorded\t1" if !parts[1].nil?
	puts "location\t1" if !parts[2].nil?
	puts "shape\t1" if !parts[3].nil?
	puts "duration\t1" if !parts[4].nil?
	puts "description\t1" if !parts[5].nil?
end