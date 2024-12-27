plugins {
    java
    application
}

application {
    mainClass.set("net.dunice.intensive.basics.UserGreeter")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

