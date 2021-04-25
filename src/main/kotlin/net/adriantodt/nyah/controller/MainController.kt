package net.adriantodt.nyah.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import net.adriantodt.nyah.Nyah

@Controller
class MainController(private val nyah: Nyah) {
    @Get("trigger")
    fun trigger(): MutableHttpResponse<Void> {
        nyah.trigger()
        return HttpResponse.ok()
    }
}
