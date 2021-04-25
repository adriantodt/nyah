package net.adriantodt.nyah

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("net.adriantodt.nyah")
		.start()
}

