package dev.emirman.mp.core.model.addon.v2

class AddonRuntime(
    addon: Addon,
    port: Int
) {
    private var thread: Thread? = null
    var logs: List<String> = listOf()

    init {
        println("Addon runtime created")
        thread = Thread {
            val builder = ProcessBuilder()
            val command = addon.config().runCommand.split(" ").toMutableList()
            println(command)
            command.add("--server.port=$port")
            builder.command(command)
            builder.redirectErrorStream(true)
            val process = builder.start()
            process.waitFor()
            val output = process.inputStream.bufferedReader().readLines()
            logs = output
            output.forEach { println(it) }
            println("Addon runtime stopped")
        }
    }

    fun start() {
        println("Addon runtime started")
        thread?.start()
    }

    fun stop() {
        println("Addon runtime stopped")
        thread?.interrupt()
    }
}